package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import android.app.Activity;
import android.os.Bundle;

public class RouteDetails extends Activity{
	private Session session;
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.route_details);
	// If we get created for the first time we get our data from the intent
	Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
	session = (Session) data.getSerializable(Session.IDENTIFIER);

	
}
}
