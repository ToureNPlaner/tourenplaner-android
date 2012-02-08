package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class BillingScreenPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private EditTextPreference limit;
	private EditTextPreference offset;
	private int defaultLimit = 25;
	private int defaultOffset = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings_billingscreen);
		limit = (EditTextPreference) getPreferenceScreen().findPreference("limit");
		offset = (EditTextPreference) getPreferenceScreen().findPreference("offset");
		

		// Initialize components
		String limitValue = limit.getSharedPreferences().getString("limit", String.valueOf(defaultLimit));
		limit.setSummary(limitValue);
		limit.setText(limitValue);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	                                      String key) {
		if (key.equals("limit")) {
limit.setSummary(sharedPreferences.getString("limit", String.valueOf(defaultLimit)));
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
