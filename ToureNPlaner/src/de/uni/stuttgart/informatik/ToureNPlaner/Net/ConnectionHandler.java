package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;
import android.util.Log;

public abstract class ConnectionHandler extends AsyncTask<Void, Void, Object> {
	private Observer listener;

	public ConnectionHandler(Observer listener) {
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
}