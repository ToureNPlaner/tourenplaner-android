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

import android.util.Log;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.ClientGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.ShortestPath;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * @author Niklas Schnelle
 */
public class ClientComputeHandler extends SessionAwareHandler {
	private static final String TAG = "ToureNPlaner";

	private int version;

	public ClientComputeHandler(Observer listener, Session session) {
		super(listener, session);
	}

	protected ArrayList<Node> getNodes() {
		return session.getNodeModel().getNodeVector();
	}

	protected ArrayList<Constraint> getConstraints() {
		return session.getConstraints();
	}

	protected Object doInBackground(Void... voids) {
		try {
			return sendSubgraphRequest();
		} catch (Exception e) {
			return e;
		}
	}

	private InputStream getCorrectStream(HttpURLConnection urlConnection) throws IOException {
		InputStream stream;
		try {
			// TODO: Only works > 4.0 else we need DoneHandlerInputStream
			stream = urlConnection.getInputStream();
		} catch (IOException exception) {
			stream = urlConnection.getErrorStream();
			if (stream == null) {
				throw exception;
			}
		}
		return stream;
	}

	private void checkStatus(HttpURLConnection urlConnection, InputStream stream, JacksonManager.ContentType type) throws IOException, de.uni.stuttgart.informatik.ToureNPlaner.Data.Error {
		if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			ObjectMapper mapper = JacksonManager.getMapper(type);
			throw de.uni.stuttgart.informatik.ToureNPlaner.Data.Error.parse(mapper.readValue(stream, JsonNode.class));
		}
	}

	protected Object sendSubgraphRequest() throws Exception {
		Result result = new Result();//Result.parse(type, inputStream);
		result.setVersion(version);

		HttpURLConnection urlConnection = session.openPostConnection("/algupdowng");

		try {
			writeSubgraphRequest(urlConnection);

			InputStream stream = getCorrectStream(urlConnection);

			JacksonManager.ContentType type = JacksonManager.ContentType.parse(urlConnection.getContentType());

			checkStatus(urlConnection, stream, type);

			computeSubgraphPath(type, stream);

			return result;
		} finally {
			urlConnection.disconnect();
		}
	}

	private void writeSubgraphRequest(HttpURLConnection urlConnection) throws IOException {
		ObjectMapper mapper = JacksonManager.getJsonMapper();
		version = session.getNodeModel().getVersion();
		JsonNode root = Request.generate(mapper.getNodeFactory(),
				getNodes(),
				getConstraints());
		JsonGenerator generator = mapper.getJsonFactory()
				.createJsonGenerator(urlConnection.getOutputStream());
		mapper.writeTree(generator, root);
		generator.close();
	}


	protected void computeSubgraphPath(JacksonManager.ContentType type, InputStream inputStream) throws Exception {

		long start = System.currentTimeMillis();
		ClientGraph graph =  ClientGraph.readClientGraph(type, inputStream);

		IntIntOpenHashMap dists = new IntIntOpenHashMap(graph.getNodeCount());
		long endOfCreate = System.currentTimeMillis();
		boolean res = ShortestPath.dijkstraStopAtDest(graph, dists);
		long end = System.currentTimeMillis();

		Log.d(TAG, "Time: "+(end-start)+" ms total, and "+(endOfCreate-start)+" ms for creating the Graph");
		Log.d(TAG, "That's "+(end-endOfCreate)+" ms for the Dijkstra");
		Log.d(TAG, "Did it? " + Boolean.toString(res));
		Log.d(TAG, "Distance " + dists.get(graph.getOrigTarget()));
	}
}
