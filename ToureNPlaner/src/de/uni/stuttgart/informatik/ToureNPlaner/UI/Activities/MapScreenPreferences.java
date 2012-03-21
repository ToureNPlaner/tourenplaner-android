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
import android.preference.PreferenceActivity;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.Intents;

public class MapScreenPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static enum MapGenerator {
		MAPNIK, OPENCYCLE, FILE, CUSTOM
	}

	CharSequence[] map_generators = new CharSequence[MapGenerator.values().length];

	{
		for (MapGenerator m : MapGenerator.values()) {
			map_generators[m.ordinal()] = m.name();
		}
	}

	private ListPreference mapGenerator;
	private EditTextPreference tileServer;
	private EditTextPreference offlineMapLoc;

	public static final String defaultTileServer = "http://gerbera.informatik.uni-stuttgart.de/osm/tiles/%1$d/%2$d/%3$d.png";
	public static final String defaultMapLocation = Environment.getExternalStorageDirectory().toString();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_mapscreen);

		tileServer = (EditTextPreference) getPreferenceScreen().findPreference("tile_server");
		offlineMapLoc = (EditTextPreference) getPreferenceScreen().findPreference("offline_map_location");
		offlineMapLoc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				getWindow().getDecorView().getRootView().post(
						new Runnable() {
							@Override
							public void run() {
								Intent intent = new Intent("org.openintents.action.PICK_FILE");
								intent.setData(Uri.parse("file://" + Environment.getExternalStorageDirectory()));
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

		// Initialize components
		String tileServerText = tileServer.getSharedPreferences().getString("tile_server", defaultTileServer);
		tileServer.setSummary(tileServerText);
		tileServer.setText(tileServerText);

		String offlineMapText = offlineMapLoc.getSharedPreferences().getString("offline_map_location", defaultMapLocation);
		offlineMapLoc.setSummary(offlineMapText);
		offlineMapLoc.setText(offlineMapText);


		updateUI(getPreferenceManager().getSharedPreferences());
	}

	private void updateUI(SharedPreferences sp) {
		MapGenerator m = MapGenerator.valueOf(sp.getString("map_generator", MapGenerator.MAPNIK.name()));

		tileServer.setEnabled(m == MapGenerator.CUSTOM);
		offlineMapLoc.setEnabled(m == MapGenerator.FILE);

		mapGenerator.setSummary(mapGenerator.getEntry());
		tileServer.setSummary(sp.getString("tile_server", defaultTileServer));
		offlineMapLoc.setSummary(sp.getString("offline_map_location", defaultMapLocation));
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
