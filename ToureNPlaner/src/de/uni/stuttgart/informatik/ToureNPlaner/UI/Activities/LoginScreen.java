package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class LoginScreen extends Activity {

	// generates the Application preferences for all activities
	public static final String TPpreferences = "ToureNPlanerPreferences";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginscreen);

			// ---the button is wired to an event handler---
		Button btnlogin = (Button) findViewById(R.id.btnlogin);
		btnlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// gets Values of TextEditors
				EditText emailTextfield = (EditText) findViewById(R.id.emailTextfield);
				EditText passwordTextfield = (EditText) findViewById(R.id.passwordTextfield);

				// set userdata
				//SessionData.Instance.setEmail(emailTextfield.getText().toString());
				//SessionData.Instance.setPassword(passwordTextfield.getText().toString());

				// TODO DB check if user exist and have permissions

				// generates an intent from the class algorithms
				Intent myIntent = new Intent(view.getContext(),
						AlgorithmScreen.class);
				startActivity(myIntent);

			}
		});
	}
}
