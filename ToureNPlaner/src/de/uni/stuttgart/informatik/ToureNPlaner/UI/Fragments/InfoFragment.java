package de.uni.stuttgart.informatik.ToureNPlaner.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ResultNode;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.ResultNodeAdapter;

import java.util.Map;

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
		View view = inflater.inflate(R.layout.route_details, null);
		TextView txtView = (TextView) view.findViewById(R.id.text);

		Result.Misc misc = session.getResult().getMisc();

		String distanceUnit = getString(R.string.meter);
		double distance = misc.getDistance();
		if (distance > 1000) {
			distance = distance / 1000;
			distanceUnit = getString(R.string.kilometer);
		}

		String txt = "";
		txt += getString(R.string.amount_of_points) + ": " + session.getResult().getPoints().size() + "\n";
		txt += getString(R.string.traveldistance) + ": " + distance + " " + distanceUnit + "\n";
		txt += getString(R.string.time) + ": " + misc.getTime() + " " + getString(R.string.minute_short) + "\n";
		String msg = misc.getMessage();
		if (msg != null)
			txt += getString(R.string.message) + ": " + msg + "\n";

		for (Map.Entry<String, String> e : misc.info.entrySet()) {
			msg += e.getKey() + ": " + e.getValue() + "\n";
		}

		// cut of last \n
		txt = txt.substring(0, txt.length() - 1);

		txtView.setText(txt);

		ListView listView = (ListView) view.findViewById(android.R.id.list);
		ArrayAdapter<ResultNode> adapter = new ResultNodeAdapter(getActivity(), session.getResult().getPoints());
		listView.setAdapter(adapter);

		return view;
	}
}
