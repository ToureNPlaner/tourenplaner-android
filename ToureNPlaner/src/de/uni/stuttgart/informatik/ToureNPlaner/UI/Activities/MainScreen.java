package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.RawHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.ServerInfoHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Dialogs.MyProgressDialog;

public class MainScreen extends SherlockFragmentActivity implements Observer {
	private ServerInfoHandler handler;

	private static final String url = "http://gerbera.informatik.uni-stuttgart.de";

	public static class ConnectionProgressDialog extends MyProgressDialog {
		static final String IDENTIFIER = "connecting";

		public static ConnectionProgressDialog newInstance(String title, String message) {
			return (ConnectionProgressDialog) MyProgressDialog.newInstance(new ConnectionProgressDialog(), title, message);
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			((MainScreen) getActivity()).cancelConnection();
		}
	}

	private void cancelConnection() {
		if (handler != null)
			handler.cancel(true);
		finish();
	}

	@Override
	public void onCompleted(RawHandler caller, Object object) {
		Session session = (Session) object;
		Intent myIntent;
		if (session.getServerInfo().getServerType() == ServerInfo.ServerType.PUBLIC) {
			myIntent = new Intent(getBaseContext(), AlgorithmScreen.class);
		} else {
			myIntent = new Intent(getBaseContext(), LoginScreen.class);
		}

		myIntent.putExtra(Session.IDENTIFIER, session);
		startActivity(myIntent);
	}

	@Override
	public void onError(RawHandler caller, Object object) {
		Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializeHandler();
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return handler;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (handler != null)
			handler.setListener(null);
	}

	private void initializeHandler() {
		handler = (ServerInfoHandler) getLastCustomNonConfigurationInstance();

		if (handler != null)
			handler.setListener(this);
		else {
			handler = Session.createSession(url, this);
			LoginScreen.ConnectionProgressDialog.newInstance(getResources().getString(R.string.connecting), url)
					.show(getSupportFragmentManager(), ConnectionProgressDialog.IDENTIFIER);
		}
	}
}
