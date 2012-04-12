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

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.io.OutputStream;
import java.net.HttpURLConnection;

public abstract class ConnectionHandler extends RawHandler {
	protected final Session session;

	public ConnectionHandler(Observer listener, Session session) {
		super(listener);
		this.session = session;
	}

	protected abstract boolean isPost();

	protected abstract String getSuffix();

	protected void handleOutput(OutputStream outputStream) throws Exception {
	}

	@Override
	protected HttpURLConnection getHttpUrlConnection() throws Exception {
		if (!isPost())
			return session.openGetConnection(getSuffix());

		HttpURLConnection connection = session.openPostConnection(getSuffix());

		try {
			handleOutput(connection.getOutputStream());
		} catch (Exception e) {
			connection.disconnect();
			throw e;
		}

		return connection;
	}
}
