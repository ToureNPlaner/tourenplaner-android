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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class RequestHandler extends SessionNetworkHandler {
	private int version;

	public RequestHandler(Observer listener, Session session) {
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
		Result result = Result.parse(type, inputStream);
		result.setVersion(version);
		return result;
	}
}
