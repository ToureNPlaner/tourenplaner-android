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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ResultNode;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.ResultNodeAdapter;

import java.util.Map;

import static de.uni.stuttgart.informatik.ToureNPlaner.UI.Formatter.formatDistance;
import static de.uni.stuttgart.informatik.ToureNPlaner.UI.Formatter.formatTime;

public class InfoFragment extends SherlockFragment {
	private Session session;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getActivity().getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.route_details, null);
		TextView txtView = (TextView) view.findViewById(R.id.text);

		Result.Misc misc = session.getResult().getMisc();

		String txt = "";
		txt += getString(R.string.amount_of_points) + ": " + session.getResult().getPoints().size() + "\n";

		txt += formatDistance(getActivity(), misc.getDistance()) + "\n";
		txt += formatTime(getActivity(), misc.getTime()) + "\n";

		String msg = misc.getMessage();
		if (msg != null)
			txt += getString(R.string.message) + ": " + msg + "\n";

		for (Map.Entry<String, String> e : misc.info.entrySet()) {
			msg += e.getKey() + ": " + e.getValue() + "\n";
		}

		// cut of last \n
		txt = txt.substring(0, txt.length() - 1);

		txtView.setText(txt);

		ListView listView = (ListView) view.findViewById(android.R.id.list);
		ArrayAdapter<ResultNode> adapter = new ResultNodeAdapter(getActivity(), session.getResult().getPoints());
		listView.setAdapter(adapter);

		return view;
	}
}
