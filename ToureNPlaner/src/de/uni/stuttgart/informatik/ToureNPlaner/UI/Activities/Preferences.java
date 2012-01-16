package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.layout.settings_mapscreen);
    }

}
