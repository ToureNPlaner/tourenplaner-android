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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.Intents;

@SuppressWarnings("deprecation")
public class MapScreenPreferences extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
	public static enum MapGenerator {
		MAPQUEST, MAPNIK, OPENCYCLE, FILE, CUSTOM
	}

	public static enum Instant {
		NEVER, SOMETIMES, ALWAYS
	}

	CharSequence[] map_generators = new CharSequence[MapGenerator.values().length];

	{
		for (MapGenerator m : MapGenerator.values()) {
			map_generators[m.ordinal()] = m.name();
		}
	}

	CharSequence[] instant_options = new CharSequence[Instant.values().length];

	{
		for (Instant o : Instant.values()) {
			instant_options[o.ordinal()] = o.name();
		}
	}


	private ListPreference mapGenerator;
	private ListPreference instant;
	private EditTextPreference tileServer;
	private EditTextPreference offlineMapLoc;
	private EditTextPreference tbtip;

	public static final String defaultTileServer = "http://gerbera.informatik.uni-stuttgart.de/osm/tiles/%1$d/%2$d/%3$d.png";
	public static final String defaultMapLocation = Environment.getExternalStorageDirectory().toString();
	public static final String defaulttbtip = "192.168.1.105:8080";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_mapscreen);

		tileServer = (EditTextPreference) getPreferenceScreen().findPreference("tile_server");
		tbtip = (EditTextPreference) getPreferenceScreen().findPreference("tbtip");
		offlineMapLoc = (EditTextPreference) getPreferenceScreen().findPreference("offline_map_location");
		offlineMapLoc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				getWindow().getDecorView().getRootView().post(
						new Runnable() {
							@Override
							public void run() {
								Intent intent = new Intent("org.openintents.action.PICK_FILE");
								intent.setData(Uri.parse("file://" + offlineMapLoc));
								intent.putExtra("org.openintents.extra.TITLE", "Please select a file");
								try {
									startActivityForResult(intent, 1);
								} catch (ActivityNotFoundException e) {
									Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
								}
							}
						});
				// Doesn't Work
				return Intents.isIntentAvailable(MapScreenPreferences.this, "org.openintents.action.PICK_FILE");
			}
		});

		mapGenerator = (ListPreference) getPreferenceScreen().findPreference("map_generator");
		mapGenerator.setEntries(R.array.map_generators);
		mapGenerator.setEntryValues(map_generators);

		instant = (ListPreference) getPreferenceScreen().findPreference("instant_request");
		instant.setEntries(R.array.instant_options);
		instant.setEntryValues(instant_options);

		// Initialize components
		String tileServerText = tileServer.getSharedPreferences().getString("tile_server", defaultTileServer);
		tileServer.setSummary(tileServerText);
		tileServer.setText(tileServerText);

		String offlineMapText = offlineMapLoc.getSharedPreferences().getString("offline_map_location", defaultMapLocation);
		offlineMapLoc.setSummary(offlineMapText);
		offlineMapLoc.setText(offlineMapText);

		String tbtipText = tbtip.getSharedPreferences().getString("tbtip", defaulttbtip);
		tbtip.setSummary(tbtipText);
		tbtip.setText(tbtipText);

		updateUI(getPreferenceManager().getSharedPreferences());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.mapscreenpreferences, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
			case R.id.about:
				startActivity(new Intent(this, AboutScreen.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateUI(SharedPreferences sp) {
		MapGenerator m = MapGenerator.valueOf(sp.getString("map_generator", MapGenerator.MAPQUEST.name()));

		tileServer.setEnabled(m == MapGenerator.CUSTOM);
		offlineMapLoc.setEnabled(m == MapGenerator.FILE);

		mapGenerator.setSummary(mapGenerator.getEntry());
		tileServer.setSummary(sp.getString("tile_server", defaultTileServer));
		offlineMapLoc.setSummary(sp.getString("offline_map_location", defaultMapLocation));
		tbtip.setSummary(sp.getString("tbtip", defaulttbtip));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			offlineMapLoc.setText(data.getData().getPath());
			offlineMapLoc.getEditText().setText(offlineMapLoc.getText());
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	                                      String key) {
		updateUI(sharedPreferences);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

}
