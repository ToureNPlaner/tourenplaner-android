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
import com.carrotsearch.hppc.IntArrayDeque;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.ClientGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.ShortestPath;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.SimpleGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.IntegerConstraint;
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
		Result result = null;
		ClientGraph graph = null;

		HttpURLConnection urlConnection = session.openPostConnection("/algupdowng");
		try {
			// TODO don't hard code
			int level = 40;
			writeSubgraphRequest(level, urlConnection);

			InputStream stream = getCorrectStream(urlConnection);

			JacksonManager.ContentType type = JacksonManager.ContentType.parse(urlConnection.getContentType());

			checkStatus(urlConnection, stream, type);
			long start = System.currentTimeMillis();
			SimpleGraph core = session.getCoreGraph();
			if(core == null){
				throw new Exception("Core Graph unreadable, please retry later");
			}
			graph = ClientGraph.readClientGraph(core, type, stream);
			long endOfCreate = System.currentTimeMillis();
			Log.d(TAG, "Time: " +(endOfCreate - start) + " ms for creating the Graph");
		} finally {
			urlConnection.disconnect();
		}
		IntArrayDeque pathOfNodes = computeSubgraphPath(graph);
		
		urlConnection = session.openPostConnection("/algwaybynodeids");
		try {
			writeWayByNodeIdsRequest(urlConnection, pathOfNodes);

			InputStream stream = getCorrectStream(urlConnection);

			JacksonManager.ContentType type = JacksonManager.ContentType.parse(urlConnection.getContentType());

			checkStatus(urlConnection, stream, type);

			result = readResult(type, stream);


		} finally {
			urlConnection.disconnect();
		}


			return result;

	}

	private void writeWayByNodeIdsRequest(HttpURLConnection urlConnection, IntArrayDeque pathOfNodes) throws Exception{
		ObjectMapper mapper = JacksonManager.getJsonMapper();
		JsonGenerator generator = mapper.getJsonFactory()
				.createJsonGenerator(urlConnection.getOutputStream());
		generator.writeStartObject();
		generator.writeArrayFieldStart("nodes");
		for (IntCursor cursor : pathOfNodes){
			generator.writeNumber(cursor.value);
		}
		generator.writeEndArray();
		generator.writeEndObject();
		generator.close();
	}

	private void writeSubgraphRequest(int level, HttpURLConnection urlConnection) throws IOException {
		ObjectMapper mapper = JacksonManager.getJsonMapper();
		version = session.getNodeModel().getVersion();
		Constraint levelConstraint = new Constraint(new IntegerConstraint("maxSearchLevel","","maxSearchLevel",0,Integer.MAX_VALUE));
		levelConstraint.setValue(level);
		// TODO: Don't add if already added
		getConstraints().add(levelConstraint);
		JsonNode root = Request.generate(mapper.getNodeFactory(),
				getNodes(),
				getConstraints());
		JsonGenerator generator = mapper.getJsonFactory()
				.createJsonGenerator(urlConnection.getOutputStream());
		mapper.writeTree(generator, root);
		generator.close();
	}


	protected IntArrayDeque computeSubgraphPath(ClientGraph graph) throws Exception {
		IntArrayDeque pathOfNodes;
		IntIntOpenHashMap dists = new IntIntOpenHashMap(graph.getNodeCount());
		IntIntOpenHashMap predEdges = new IntIntOpenHashMap(graph.getEdgeCount());

		long start = System.currentTimeMillis();

		boolean res = ShortestPath.dijkstraStopAtDest(graph, dists, predEdges);
		if (res) {
			pathOfNodes = ShortestPath.backtrack(graph, predEdges);
		} else {
			// No path found
			pathOfNodes = new IntArrayDeque(0);
		}
		long end = System.currentTimeMillis();


		Log.d(TAG, "That's "+(end-start)+" ms for the Dijkstra");
		Log.d(TAG, "Did it? " + Boolean.toString(res));
		Log.d(TAG, "Distance " + dists.get(graph.getOrigTarget()));
		return pathOfNodes;
	}

	private Result readResult(JacksonManager.ContentType type, InputStream inputStream) throws Exception {
		Result result = Result.parse(type, inputStream);
		result.getMisc().setAlgorithm(session.getSelectedAlgorithm());
		result.setVersion(version);
		return result;
	}
}
