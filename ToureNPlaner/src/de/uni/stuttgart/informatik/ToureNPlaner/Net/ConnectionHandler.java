package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.os.AsyncTask;

public abstract class ConnectionHandler extends AsyncTask<Void, Void, Object> {
	private Observer listener;

	public ConnectionHandler(Observer listener) {
		this.listener = listener;
	}

	public void setListener(Observer listener) {
		this.listener = listener;
	}

	@Override
	public void onPostExecute(Object object) {
		if (object instanceof Exception) {
			listener.onError(object);
		} else {
			listener.onCompleted(object);
		}
	}
}