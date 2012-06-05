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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
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
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @author Niklas Schnelle
 */
public class ClientComputeHandler extends RequestHandler {
	private static final String TAG = "ToureNPlaner";

	private int version;

	public ClientComputeHandler(Observer listener, Session session) {
		super(listener, session);
	}

	@Override
	protected boolean isPost() {
		return true;
	}

	@Override
	protected String getSuffix() {
		return "/alg" + session.getSelectedAlgorithm().getUrlsuffix();
	}

	protected ArrayList<Node> getNodes() {
		return session.getNodeModel().getNodeVector();
	}

	protected ArrayList<Constraint> getConstraints() {
		return session.getConstraints();
	}

	@Override
	protected void handleOutput(OutputStream outputStream) throws Exception {
		ObjectMapper mapper = JacksonManager.getJsonMapper();
		version = session.getNodeModel().getVersion();
		JsonNode root = Request.generate(mapper.getNodeFactory(),
				getNodes(),
				getConstraints());
		JsonGenerator generator = mapper.getJsonFactory()
				.createJsonGenerator(outputStream);
		mapper.writeTree(generator, root);
		generator.close();
	}




	@Override
	protected Object handleInput(JacksonManager.ContentType type, InputStream inputStream) throws Exception {

		Result result = new Result();//Result.parse(type, inputStream);

		result.setVersion(version);
		try {
			ClientGraph graph =  readClientGraph(type, inputStream);

			IntIntOpenHashMap dists = new IntIntOpenHashMap(graph.getNodeCount());
			boolean res = false;

			res = ShortestPath.dijkstraStopAtDest(graph, dists);
			Log.d(TAG, "Did it? " + Boolean.toString(res));
			Log.d(TAG, "Distance " + dists.get(graph.getOrigTarget()));
		} catch (Exception e){
			e.printStackTrace();
		}

		return result;
	}

	private ClientGraph readClientGraph(JacksonManager.ContentType type, InputStream inputStream) throws IOException {
		ClientGraph graph = new ClientGraph();
		ObjectMapper mapper = JacksonManager.getMapper(type);
		final JsonParser jp = mapper.getJsonFactory().createJsonParser(inputStream);
		if (jp.nextToken() != JsonToken.START_OBJECT) {
			throw new JsonParseException("Request contains no json object", jp.getCurrentLocation());
		}

		String fieldname;
		JsonToken token;
		int srcId, trgtId, dist;
		boolean finished = false;
		while (!finished) {
			//move to next field or END_OBJECT/EOF
			token = jp.nextToken();
			if (token == JsonToken.FIELD_NAME) {
				fieldname = jp.getCurrentName();
				token = jp.nextToken(); // move to value, or
				// START_OBJECT/START_ARRAY
				if ("edges".equals(fieldname)) {
					while (jp.nextToken() != JsonToken.END_ARRAY && jp.getCurrentToken() != null){
						srcId = jp.getIntValue();
						jp.nextToken();
						trgtId = jp.getIntValue();
						jp.nextToken();
					    dist = jp.getIntValue();
						graph.addEdge(srcId, trgtId, dist);
					}
				} else if ("srcId".equals(fieldname)){
					graph.setOrigSource(jp.getIntValue());
				} else if ("trgtId".equals(fieldname)){
					graph.setOrigTarget(jp.getIntValue());
				} else {
					// ignore for now TODO: user version string etc.
					if ((token == JsonToken.START_ARRAY) || (token == JsonToken.START_OBJECT)) {
						jp.skipChildren();
					}
				}
			} else if (token == JsonToken.END_OBJECT) {
				// Normal end of request
				finished = true;
			} else if (token == null) {
				//EOF
				throw new JsonParseException("Unexpected EOF in Request", jp.getCurrentLocation());
			} else {
				throw new JsonParseException("Unexpected token " + token, jp.getCurrentLocation());
			}

		}
		return graph;
	}
}
