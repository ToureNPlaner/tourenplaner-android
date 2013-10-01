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
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerInfoHandler extends SimpleNetworkHandler {
	private final String url;

	public ServerInfoHandler(Observer listener, String url) {
		super(listener);
		this.url = url;
	}

	@Override
	protected HttpURLConnection getHttpUrlConnection() throws Exception {
		URL info_url = new URL(url + "/info");
		HttpURLConnection urlConnection = (HttpURLConnection) info_url.openConnection();
		if (urlConnection instanceof HttpsURLConnection) {
			((HttpsURLConnection) urlConnection).setSSLSocketFactory(ToureNPlanerApplication.getSslContext().getSocketFactory());
		}
		return urlConnection;
	}

	@Override
	protected void handleOutput(OutputStream connection) throws Exception {}

	@Override
	protected Object handleInput(JacksonManager.ContentType type, InputStream inputStream) throws Exception {
		ObjectMapper mapper = JacksonManager.getMapper(type);
		Session session = new Session();
		session.setServerInfo(ServerInfo.parse(mapper.readValue(inputStream, JsonNode.class)));
		session.setUrl(url);
		return session;
	}

}
