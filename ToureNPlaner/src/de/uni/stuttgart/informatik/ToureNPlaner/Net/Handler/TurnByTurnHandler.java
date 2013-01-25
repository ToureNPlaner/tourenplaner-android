/*
 * Copyright 2013 ToureNPlaner
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

import de.uni.stuttgart.informatik.ToureNPlaner.Data.TBTResult;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TurnByTurnHandler extends SimpleNetworkHandler {

	private final String ip;
	private final ArrayList<ArrayList<int[]>> nodes;


	public TurnByTurnHandler(Observer listener, String ip, ArrayList<ArrayList<int[]>> nodes) {
		super(listener);
		this.nodes = nodes;
		this.ip = ip;



	}

	@Override
	protected HttpURLConnection getHttpUrlConnection() throws Exception {
		URL tbt_url = new URL("http://"+ip+"/streetname/");
		HttpURLConnection urlConnection = (HttpURLConnection) tbt_url.openConnection();
		urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

		if (urlConnection instanceof HttpsURLConnection) {
			((HttpsURLConnection) urlConnection).setSSLSocketFactory(ToureNPlanerApplication.getSslContext().getSocketFactory());
		}
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("POST");
		handleOutput(urlConnection.getOutputStream());
		return urlConnection;
	}

	@Override
	protected void handleOutput(OutputStream connection) throws Exception {
		//connection.write("nodes=".getBytes());
		JacksonManager.getJsonMapper().writeValue(connection, nodes);
	}

	@Override
	protected Object handleInput(JacksonManager.ContentType type, InputStream inputStream) throws Exception {
		return TBTResult.parse(type, inputStream);
	}
}
