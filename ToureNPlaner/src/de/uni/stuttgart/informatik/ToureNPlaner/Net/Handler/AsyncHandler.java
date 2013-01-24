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
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;

import java.lang.ref.WeakReference;

/**
 * @author Niklas Schnelle
 */
public abstract class AsyncHandler extends AsyncTask<Void, Void, Object> {
	protected WeakReference<Observer> listener;

	public AsyncHandler(Observer listener) {
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
		if (l == null) {
			Log.d("tp","The listener has been collected");
			return;
		}

		if (object instanceof Exception) {
			l.onError(this, object);
		} else {
			l.onCompleted(this, object);
		}
	}
}
