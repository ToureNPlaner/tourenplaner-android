package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.SessionData;

public class ServerScreen extends Activity {

	private String spinnerArray[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serverscreen);

		// TODO get amount of available servers

		// TODO should be dynamic
		spinnerArray = new String[2];
		spinnerArray[0] = "Free-Server";
		spinnerArray[1] = "Pay-Server";

		// loads the spinnerArray into the spinnerdropdown
		Spinner spinner = (Spinner) findViewById(R.id.spinner_server);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int pos, long id) {

				SessionData.setChoosenAlgorithm(adapter.getItemAtPosition(pos)
						.toString());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
							}

		});

		Button btnconfirm = (Button) findViewById(R.id.btnconfirm);
		Button btnSetURL = (Button) findViewById(R.id.btnSetUrl);

		btnconfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// generates an intent from the class MapScreen
				Intent myIntent = new Intent(view.getContext(),
						LoginScreen.class);
				startActivity(myIntent);

			}

		});
		//  User can change the server URL
		btnSetURL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						ServerScreen.this);

				alert.setTitle("type your URL");
				alert.setMessage("URL");

				// Set an EditText view to get user input
				final EditText input = new EditText(ServerScreen.this);
				input.setText(SessionData.getServerURL());
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								SessionData.setServerURL(input.getText()
										.toString());
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				alert.show();
			}

		});
	}
}
