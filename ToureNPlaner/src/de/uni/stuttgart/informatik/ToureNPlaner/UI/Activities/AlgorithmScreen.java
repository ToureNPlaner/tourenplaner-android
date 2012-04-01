package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen.MapScreen;

import java.util.ArrayList;
import java.util.Collections;

public class AlgorithmScreen extends SherlockListActivity {
	private Session session;
	private boolean started;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);

		setupListView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		started = false;
	}

	private void setupListView() {
		ArrayList<AlgorithmInfo> algorithms = new ArrayList<AlgorithmInfo>();
		for (AlgorithmInfo alg : session.getServerInfo().getAlgorithms()) {
			if (!alg.isHidden())
				algorithms.add(alg);
		}

		Collections.sort(algorithms);

		ArrayAdapter<AlgorithmInfo> adapter = new ArrayAdapter<AlgorithmInfo>(this,
				R.layout.list_item, algorithms);
		setListAdapter(adapter);

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if (started)
					return;
				started = true;
				AlgorithmInfo alg = (AlgorithmInfo) adapterView.getItemAtPosition(i);
				session.setSelectedAlgorithm(alg);
				Intent myIntent = new Intent(view.getContext(), MapScreen.class);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.algorithmscreenmenu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (session.getServerInfo().getServerType() == ServerInfo.ServerType.PUBLIC) {
			menu.findItem(R.id.billing).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.billing:
				Intent myIntent = new Intent(this, BillingScreen.class);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivity(myIntent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
