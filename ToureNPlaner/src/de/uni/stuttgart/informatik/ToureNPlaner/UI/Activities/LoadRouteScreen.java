/*
 * Copyright 2013 ToureNPlaner
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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.LocalStorage.RoutesStorageDbHelper;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.LocalStorage.StoredRoute;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import java.util.List;

public class LoadRouteScreen extends SherlockActivity{
	List<StoredRoute> routes;
	private Session session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storedroutes);
		RoutesStorageDbHelper helper = new RoutesStorageDbHelper(ToureNPlanerApplication.getContext());
		routes = helper.LoadRoutes();
		ListView listview = (ListView) findViewById(R.id.routeslist);

		ArrayAdapter<StoredRoute> adapter = new ArrayAdapter<StoredRoute>(ToureNPlanerApplication.getContext(), R.layout.list_item,routes);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final StoredRoute item = (StoredRoute) parent.getItemAtPosition(position);
				// this approach doesn't really work
				//session.setResult(item.result);
				//Log.d("TP", "Set route " + item.toString());
				//session.notifyChangeListerners(new Session.Change(Session.RESULT_CHANGE));
				Intent i = new Intent();
				i.putExtra("storedroute", item);
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
