package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.AuthRequestHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.BillingListHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.RawHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.LoginScreen.ConnectionProgressDialog;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Dialogs.MyProgressDialog;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.util.ArrayList;
import java.util.Collections;


public class AlgorithmScreen extends FragmentActivity implements Observer {
	private Session session;
    private BillingListHandler handler;

    @Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}

	public static class ConnectionProgressDialog extends MyProgressDialog {
		public static ConnectionProgressDialog newInstance(String title, String message) {
			return (ConnectionProgressDialog) MyProgressDialog.newInstance(new ConnectionProgressDialog(), title, message);
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			((AlgorithmScreen) getActivity()).cancelConnection();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.algorithmscreen);

		// If we get created for the first time we get our data from the intent
		if (savedInstanceState != null) {
			session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
		} else {
			session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
		}
		initializeHandler();
		setupListView();
		setupBillingButton();
	}

	private void setupListView() {
		ListView listView = (ListView) findViewById(R.id.listViewAlgorithm);
		ArrayList<AlgorithmInfo> algorithms = new ArrayList<AlgorithmInfo>();
		for (AlgorithmInfo alg : session.getServerInfo().getAlgorithms()) {
			if (!alg.isHidden())
				algorithms.add(alg);
		}

		Collections.sort(algorithms);

		ArrayAdapter<AlgorithmInfo> adapter = new ArrayAdapter<AlgorithmInfo>(this,
				android.R.layout.simple_list_item_1, algorithms);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				AlgorithmInfo alg = (AlgorithmInfo) adapterView.getItemAtPosition(i);
				if (!alg.equals(session.getSelectedAlgorithm()))
					session.setResult(null);
				session.setSelectedAlgorithm(alg);
				Intent myIntent = new Intent(view.getContext(), MapScreen.class);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivity(myIntent);
			}
		});
	}

	private void setupBillingButton() {
		Button btnBilling = (Button) findViewById(R.id.btnBilling);
		if (session.getServerInfo().getServerType() == ServerInfo.ServerType.PUBLIC) {
			btnBilling.setVisibility(View.GONE);
		} else {
			btnBilling.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// generates an intent from the class BillingScreen
//					Intent myIntent = new Intent(view.getContext(), BillingScreen.class);
//					startActivity(myIntent);
					handler = new BillingListHandler(AlgorithmScreen.this, session);
					handler.execute();
				}
			});
		}
	}
	private void initializeHandler() {
		handler = (BillingListHandler) getLastCustomNonConfigurationInstance();

		if (handler != null)
			handler.setListener(this);
		else {
			MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("login");
			if (dialog != null)
				dialog.dismiss();
		}
	}

	private void cancelConnection() {
		handler.cancel(true);
		handler = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (handler != null)
			handler.setListener(null);
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return handler;
	}
	@Override
	public void onCompleted(RawHandler caller, Object object) {
		handler = null;
//		MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("login");
//		dialog.dismiss();
		Intent myIntent = new Intent(getBaseContext(), BillingScreen.class);
		myIntent.putExtra(Session.IDENTIFIER, session);
		startActivity(myIntent);
	}

	@Override
	public void onError(RawHandler caller, Object object) {
		handler = null;
//		MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("login");
//		dialog.dismiss();
		Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
	}

}
