package com.ToureNPlaner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginScreen extends Activity {
	/** Called when the activity is first created. */
	// nach tutorial von
	// http://mobiforge.com/developing/story/getting-started-with-android-development

	// generates the Applicationpreferences for all activities
	public static final String prefName = "ToureNPlanerPreferences";

	// generates all variables which are used in this application
	static String choosenAlgorithm = "";
	static String username = "";
	static String password = "";
	static boolean isAcceptedUser = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ----- creating an shared Preference -----
		SharedPreferences applicationPreferences = getSharedPreferences(
				prefName, MODE_PRIVATE);
		final SharedPreferences.Editor prefEditor = applicationPreferences.edit();

	
		setContentView(R.layout.main);

		// ---the button is wired to an event handler---
		Button btn1 = (Button) findViewById(R.id.btnlogin);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				//  gets Values of TextEditors
				EditText ETname = (EditText) findViewById(R.id.ETname);
				EditText ETpassword = (EditText) findViewById(R.id.ETpassword);
				
				username = ETname.getText().toString();
				password = ETpassword.getText().toString();
				
				//TODO DB check if user exist and have rights
				
				
				// set Variables for the ApplicationPreferences
				prefEditor.putString("userName", username);
				prefEditor.putString("password",password);
				prefEditor.putBoolean("isAcceptedUser", isAcceptedUser);
				prefEditor.commit();

				
				
				// generates an intent from the class algorithms
				Intent myIntent = new Intent(view.getContext(),
						algorithmScreen.class);
				startActivity(myIntent);

				
				
				Toast.makeText(getBaseContext(),
						username + " you are brought to the next page",
						Toast.LENGTH_SHORT).show();
				onPause();
			
			}});

	}
}