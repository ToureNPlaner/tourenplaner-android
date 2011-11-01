package com.ToureNPlaner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class server extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ----- creating an shared Preference -----
	
	
		setContentView(R.layout.server);

		// ---the button is wired to an event handler---
		Button btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				
				// generates an intent from the class algorithms
				Intent myIntent = new Intent(view.getContext(),
						loginScreen.class);
				startActivity(myIntent);

				
			
			
			}});

	}
}