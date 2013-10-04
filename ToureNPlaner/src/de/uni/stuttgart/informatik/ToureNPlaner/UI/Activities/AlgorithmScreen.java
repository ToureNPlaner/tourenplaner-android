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

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen.MapScreen;

import java.util.ArrayList;
import java.util.Collections;

public class AlgorithmScreen extends ListActivity {
	private Session session;
	private boolean started;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);
		setupListView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		started = false;
	}

	private void setupListView() {
		final ArrayList<AlgorithmInfo> algorithms = new ArrayList<AlgorithmInfo>();
		for (AlgorithmInfo alg : session.getServerInfo().getAlgorithms()) {
			if (!alg.isHidden())
				algorithms.add(alg);
		}

		Collections.sort(algorithms);

		ArrayAdapter<AlgorithmInfo> adapter = new ArrayAdapter<AlgorithmInfo>(this, R.layout.list_item2, algorithms) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view;
				if (convertView == null) {
					view = getLayoutInflater().inflate(R.layout.list_item2, parent, false);
				} else {
					view = convertView;
				}

				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				text1.setText(algorithms.get(position).getName());
				TextView text2 = (TextView) view.findViewById(android.R.id.text2);
				text2.setText(algorithms.get(position).getDescription());
				return view;
			}
		};
		setListAdapter(adapter);

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if (started)
					return;
				started = true;
				AlgorithmInfo alg = (AlgorithmInfo) adapterView.getItemAtPosition(i);
				session.setSelectedAlgorithm(alg);
				Intent myIntent = new Intent(view.getContext(), MapScreen.class);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.algorithmscreenmenu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (session.getServerInfo().getServerType() == ServerInfo.ServerType.PUBLIC) {
			menu.findItem(R.id.billing).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.billing:
				Intent myIntent = new Intent(this, BillingScreen.class);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivity(myIntent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
