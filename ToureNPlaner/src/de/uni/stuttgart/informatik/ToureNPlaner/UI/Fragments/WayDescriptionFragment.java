/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.AsyncHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.TurnByTurnHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WayDescriptionFragment extends SherlockFragment implements Observer {
	private Session session;
	private WayDescriptionFragment wd;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wd = this;
		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getActivity().getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);
	}

	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.way_description, null);
		final ArrayList<int[][]> nodes = new ArrayList<int[][]>();
		final ArrayList<ArrayList<Coordinate>> sendnodes = new ArrayList<ArrayList<Coordinate>>();
		nodes.add(session.getResult().getWay());
		final TextView tv = (TextView) view.findViewById(R.id.tbtip);
		tv.setVisibility(TextView.VISIBLE);
		tv.setEnabled(true);
		final Button button = (Button) view.findViewById(R.id.tbtbt);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				for (int[][] i : nodes) {
					ArrayList<Coordinate> subways = new ArrayList<Coordinate>();
					for (int[] subway : i) {
						for (int idx = 0; idx < subway.length; idx += 2) {
							Coordinate c = new Coordinate();
							c.ln = subway[idx]*10;
							c.lt = subway[idx + 1]*10;
							subways.add(c);
						}
					}
					sendnodes.add(subways);
				}
				TurnByTurnHandler tbt = new TurnByTurnHandler(wd,tv.getText().toString(),sendnodes);
				tbt.execute();
			}
		});
		return view;
	}

	private final double calcDirectDistance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6370.97327862;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;
		return dist * 1000;
	}

	@Override
	public void onCompleted(AsyncHandler caller, Object object) {
		JsonNode result = null;

		try {
			result = ((ObjectNode) caller.get()).get("streets");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		Log.d("tp", "resultstring: " + result.toString());
		List streets = new ArrayList<String>();
		int distall = 0;
		int dist = 0;
		for (JsonNode n : result) {
			for (int i = 0; i < n.get("coordinates").size() - 1; i += 2) {
				double lt = n.get("coordinates").get(i).get("lt").asDouble();
				double ln = n.get("coordinates").get(i).get("ln").asDouble();
				double lt2 = n.get("coordinates").get(i + 1).get("lt").asDouble();
				double ln2 = n.get("coordinates").get(i + 1).get("ln").asDouble();
				dist += calcDirectDistance(lt, ln, lt2, ln2);
			}
			streets.add(n.get("name") + " (" + dist + " meter)");
			distall += dist;
			dist = 0;
		}
		Toast.makeText(ToureNPlanerApplication.getContext(), "dist: , " + distall, Toast.LENGTH_LONG).show();

		ListAdapter adapter = new ArrayAdapter<String>(ToureNPlanerApplication.getContext(), R.layout.list_item, streets);
		ListView listView = (ListView) view.findViewById(android.R.id.list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onError(AsyncHandler caller, Object object) {
		Log.d("tp", "error on request: Caller=" + caller.getClass().getName() + " error=" + object.toString());
	}

	public class Coordinate{
		public int lt;
		public int ln;
	}
}
