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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.AsyncHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import java.util.List;

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
		if (session.gettbtResult() != null && session.gettbtResult().getStreets() != null) {
			TextView tv = (TextView) view.findViewById(R.id.tbtdist);
			tv.setText("Distance " + session.gettbtResult().getDist());
			List streets = session.gettbtResult().getStreets();
			ListAdapter adapter = new ArrayAdapter<String>(ToureNPlanerApplication.getContext(), R.layout.list_item, streets);
			ListView listView = (ListView) view.findViewById(android.R.id.list);
			listView.setAdapter(adapter);
		}
		return view;
	}

	@Override
	public void onCompleted(AsyncHandler caller, Object object) {
//		JsonNode result = null;
//
//		try {
//			result = ((ObjectNode) caller.get()).get("streets");
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//
//		Log.d("tp", "resultstring: " + result.toString());
//		List streets = new ArrayList<String>();
//		int distall = 0;
//		int dist = 0;

//		Toast.makeText(ToureNPlanerApplication.getContext(), "dist: , " + distall, Toast.LENGTH_LONG).show();

		if (session.gettbtResult() != null && session.gettbtResult().getStreets() != null) {
			List streets = session.gettbtResult().getStreets();
			ListAdapter adapter = new ArrayAdapter<String>(ToureNPlanerApplication.getContext(), R.layout.list_item, streets);
			ListView listView = (ListView) view.findViewById(android.R.id.list);
			listView.setAdapter(adapter);
		}
//		for (ArrayList<Node> nl : tbtway) {
//			for (Node n : nl) {
//				session.getNodeModel().getNodeVector().add(n);
//			}
//		}
//		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE | Session.ADD_CHANGE));
	}

	@Override
	public void onError(AsyncHandler caller, Object object) {
		Log.d("tp", "error on request: Caller=" + caller.getClass().getName() + " error=" + object.toString());
	}
}
