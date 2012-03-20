package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

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
	public static final String defaultMapLocation = "/sdcard/...";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings_mapscreen);

		tileServer = (EditTextPreference) getPreferenceScreen().findPreference("tile_server");
		offlineMapLoc = (EditTextPreference) getPreferenceScreen().findPreference("offline_map_location");

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
