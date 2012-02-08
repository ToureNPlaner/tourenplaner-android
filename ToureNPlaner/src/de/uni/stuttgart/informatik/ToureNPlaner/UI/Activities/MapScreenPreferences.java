package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class MapScreenPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private EditTextPreference tileServer;
	private EditTextPreference offlineMapLoc;
	private CheckBoxPreference isOfflineMap;
	private CheckBoxPreference isInstantRequest;
	private CheckBoxPreference backIsDeleteMarker;

	private String defaultTileServer = "gerbera.informatik.uni-stuttgart.de/osm/tiles";
	private String defaultMapLocation = "/sdcard/...";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings_mapscreen);
		tileServer = (EditTextPreference) getPreferenceScreen().findPreference("tile_server");
		offlineMapLoc = (EditTextPreference) getPreferenceScreen().findPreference("offline_map_location");
		isOfflineMap = (CheckBoxPreference) getPreferenceScreen().findPreference("is_offline_map");
		isInstantRequest = (CheckBoxPreference) getPreferenceScreen().findPreference("is_instant_request");
		backIsDeleteMarker = (CheckBoxPreference) getPreferenceScreen().findPreference("back_is_delete_marker");

		// Initialize components
		String tileServerText = tileServer.getSharedPreferences().getString("tile_server", defaultTileServer);
		tileServer.setSummary(tileServerText);
		tileServer.setText(tileServerText);

		String offlineMapText = offlineMapLoc.getSharedPreferences().getString("offline_map_location", defaultMapLocation);
		offlineMapLoc.setSummary(offlineMapText);
		offlineMapLoc.setText(offlineMapText);

		offlineMapLoc.setEnabled(isOfflineMap.isChecked());


	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	                                      String key) {
		// Let's do something a preference value changes
		if (key.equals("offline_map_location")) {
			offlineMapLoc.setSummary(sharedPreferences.getString("offline_map_location", defaultMapLocation));
		}
		if (key.equals("is_offline_map")) {
			offlineMapLoc.setEnabled(isOfflineMap.isChecked());
		}
		if (key.equals("tile_server")) {
			tileServer.setSummary(sharedPreferences.getString("tile_server", defaultTileServer));
		}


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
