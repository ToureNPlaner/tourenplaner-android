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

package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import android.os.AsyncTask;
import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Error;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.DoneHandlerInputStream;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

public abstract class RawHandler extends AsyncTask<Void, Void, Object> {
	private WeakReference<Observer> listener;

	public RawHandler(Observer listener) {
		setListener(listener);
	}

	public void setListener(Observer listener) {
		this.listener = new WeakReference<Observer>(listener);
	}

	/**
	 * Will be run in UI thread
	 *
	 * @param object
	 */
	@Override
	public void onPostExecute(Object object) {
		if (listener == null) {
			Log.w("TP", "Null Listener!");
			return;
		}

		Observer l = listener.get();

		// The listener has been collected
		if (l == null)
			return;

		if (object instanceof Exception) {
			l.onError(this, object);
		} else {
			l.onCompleted(this, object);
		}
	}

	@Override
	protected Object doInBackground(Void... voids) {
		try {
			HttpURLConnection urlConnection = getHttpUrlConnection();

			try {
				InputStream stream;
				try {
					stream = new DoneHandlerInputStream(urlConnection.getInputStream());
				} catch (IOException exception) {
					stream = urlConnection.getErrorStream();
					if (stream == null) {
						throw exception;
					}
					stream = new DoneHandlerInputStream(stream);
				}
				JacksonManager.ContentType type = JacksonManager.ContentType.parse(urlConnection.getContentType());

				if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					ObjectMapper mapper = JacksonManager.getMapper(type);
					throw Error.parse(mapper.readValue(stream, JsonNode.class));
				}

				return handleInput(type, stream);
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
