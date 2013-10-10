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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.LocalStorage.RoutesStorageDbHelper;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.LocalStorage.StoredRoute;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import java.util.List;

public class LoadRouteScreen extends FragmentActivity{
	List<StoredRoute> routes;
	private Session session;

	ListView listview;
	ArrayAdapter<StoredRoute> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storedroutes);
		RoutesStorageDbHelper helper = new RoutesStorageDbHelper(ToureNPlanerApplication.getContext());
		routes = helper.LoadRoutes();
		listview = (ListView) findViewById(R.id.routeslist);

		adapter = new ArrayAdapter<StoredRoute>(ToureNPlanerApplication.getContext(), R.layout.list_item,routes);
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

		registerForContextMenu(listview);

	}


	private final int deleteid = 5;
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId()==R.id.routeslist) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			//menu.setHeaderTitle(Countries[info.position]);
			//String[] menuItems = getResources().getStringArray(R.array.menu);
			StoredRoute item = routes.get(info.position);
			menu.add(item.toString());
			menu.add(Menu.NONE, deleteid, 0, "Delete this route");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == deleteid) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			Toast.makeText(ToureNPlanerApplication.getContext(), "Deleting Item...", Toast.LENGTH_LONG).show();
			RoutesStorageDbHelper helper = new RoutesStorageDbHelper(ToureNPlanerApplication.getContext());
			StoredRoute r = routes.get(info.position);
			helper.deleteRoute(r);
			routes.remove(info.position);
			adapter.notifyDataSetChanged();
		}
		return true;
	}
}
