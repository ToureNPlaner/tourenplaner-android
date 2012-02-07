package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import android.os.AsyncTask;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.DoneHandlerInputStream;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;

import java.io.InputStream;
import java.net.HttpURLConnection;

public abstract class RawHandler extends AsyncTask<Void, Void, Object> {
	private Observer listener;

	public RawHandler(Observer listener) {
		this.listener = listener;
	}

	public void setListener(Observer listener) {
		this.listener = listener;
	}

	/**
	 * Will be run in UI thread
	 *
	 * @param object
	 */
	@Override
	public void onPostExecute(Object object) {
		if (listener == null) {
			// TODO remove
			Log.w("TP", "Null Listener!");
			return;
		}

		if (object instanceof Exception) {
			listener.onError(this, object);
		} else {
			listener.onCompleted(this, object);
		}
	}

	@Override
	protected Object doInBackground(Void... voids) {
		try {
			HttpURLConnection urlConnection = getHttpUrlConnection();

			try {
				InputStream stream = new DoneHandlerInputStream(urlConnection.getInputStream());
				return handleInput(JacksonManager.ContentType.parse(urlConnection.getContentType()), stream);
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			return e;
		}
	}

	protected abstract HttpURLConnection getHttpUrlConnection() throws Exception;

	protected abstract Object handleInput(JacksonManager.ContentType type, InputStream inputStream) throws Exception;
}
