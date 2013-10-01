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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.DoneHandlerInputStream;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public abstract class SessionNetworkHandler extends SessionAwareHandler {

	public SessionNetworkHandler(Observer listener, Session session) {
		super(listener, session);
		this.session = session;
	}


	protected abstract Object handleInput(JacksonManager.ContentType type, InputStream inputStream) throws Exception;

	protected abstract void handleOutput(OutputStream outputStream) throws Exception;

	protected abstract boolean isPost();

	protected abstract String getSuffix();


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
					throw de.uni.stuttgart.informatik.ToureNPlaner.Data.Error.parse(mapper.readValue(stream, JsonNode.class));
				}

				return handleInput(type, stream);
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			return e;
		}
	}


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
