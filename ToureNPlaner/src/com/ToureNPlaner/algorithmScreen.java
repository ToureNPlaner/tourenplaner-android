package com.ToureNPlaner;

import java.io.File;

import com.ToureNPlaner.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class algorithmScreen extends Activity {
	/** Called when the activity is first created. */

	;

	public void onCreate(Bundle savedInstanceState) {

		final SharedPreferences applicationPreferences = getSharedPreferences(
				loginScreen.prefName, MODE_PRIVATE);
		final SharedPreferences.Editor prefEditor = applicationPreferences
				.edit();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.algorithm);
		final Button btnTSP = (Button) findViewById(R.id.btnTSP);
		final Button btnCSP = (Button) findViewById(R.id.btnCSP);

		btnCSP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loginScreen.choosenAlgorithm = btnCSP.getText().toString();
				prefEditor.putString("choosenAlgorithm",
						loginScreen.choosenAlgorithm);
				prefEditor.commit();

				Toast.makeText(
						getBaseContext(),
						"you have choosen the "
								+ applicationPreferences.getString(
										"choosenAlgorithm", "a non existing")
								+ " algorithm", Toast.LENGTH_SHORT).show();

			}
		});
		btnTSP.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				File f = new File("testfile.txt");
				
				Intent myIntent = new Intent(view.getContext(), mapView.class);
				startActivity(myIntent);
				
				loginScreen.choosenAlgorithm = btnTSP.getText().toString();
				prefEditor.putString("choosenAlgorithm",
						loginScreen.choosenAlgorithm);
				prefEditor.commit();
				Toast.makeText(
						getBaseContext(),
						"you have choosen the "
								+ applicationPreferences.getString(
										"choosenAlgorithm",
										"a non existing") + " algorithm"+ f.getAbsolutePath(),
						Toast.LENGTH_SHORT).show();
				onPause();
			}

		});

	}
}
