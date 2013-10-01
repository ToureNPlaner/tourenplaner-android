/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.AsyncHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.ServerInfoHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Dialogs.MyProgressDialog;

public class MainScreen extends SherlockFragmentActivity implements Observer {
	private ServerInfoHandler handler;

	private static final String url = "http://tourenplaner.informatik.uni-stuttgart.de";

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
	public void onCompleted(AsyncHandler caller, Object object) {
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
	public void onError(AsyncHandler caller, Object object) {
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
			ConnectionProgressDialog.newInstance(getResources().getString(R.string.connecting), url)
					.show(getSupportFragmentManager(), ConnectionProgressDialog.IDENTIFIER);
		}
	}
}
