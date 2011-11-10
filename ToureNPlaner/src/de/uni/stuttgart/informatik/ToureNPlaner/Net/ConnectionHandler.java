package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionHandler extends AsyncTask<Void, Void, Object> {
	Observer listener;
	String url;

	public void setListener(Observer listener) {
		this.listener = listener;
	}

	@Override
	protected void onPostExecute(Object object) {
		if (object instanceof Session) {
			listener.onCompleted(object);
		} else {
			listener.onError(object);
		}
	}

	ConnectionHandler(String url, Observer listener) {
		super();
		this.listener = listener;
		this.url = url;
	}

	@Override
	protected Object doInBackground(Void... voids) {
		try {
			URL uri = new URL(url + "/info");
			HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();

			try {
				InputStream stream = new DoneHandlerInputStream(urlConnection.getInputStream());
				String content = Util.streamToString(stream);
				Session session = new Session();
				session.setServerInfo(ServerInfo.parse(new JSONObject(content)));
				session.setUrl(url);
				return session;
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			return e;
		}
	}
}
