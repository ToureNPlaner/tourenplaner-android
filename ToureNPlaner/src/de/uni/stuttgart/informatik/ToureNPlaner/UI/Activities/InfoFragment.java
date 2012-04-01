package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class InfoFragment extends SherlockFragment {
	private Session session;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getActivity().getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		String distanceUnit = getResources().getString(R.string.meter);
		double distance = session.getResult().getMisc().getDistance();
		if (distance > 1000) {
			distance = distance / 1000;
			distanceUnit = getResources().getString(R.string.kilometer);
		}

		View view = inflater.inflate(R.layout.route_details, null);

		TextView txtmarkercount = (TextView) view.findViewById(R.id.details_markercount);
		TextView txtdistance = (TextView) view.findViewById(R.id.details_distance);
		TextView txtmsg = (TextView) view.findViewById(R.id.details_message);

		txtmarkercount.setText(getResources().getString(R.string.amount_of_points) + ": " + session.getResult().getPoints().size());
		txtdistance.setText(getResources().getString(R.string.distance) + ": " + distance + " " + distanceUnit);
		String msg = session.getResult().getMisc().getMessage();
		if (msg != null)
			txtmsg.setText(getResources().getString(R.string.message) + ": " + msg);

		txtmsg.setText(session.getResult().getMisc().info.toString());

		return view;
	}
}
