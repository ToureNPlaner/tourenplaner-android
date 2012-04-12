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

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.util.ArrayList;

/**
 * RequestNN are not persistent, so we don't need to update the session etc.
 */
public class RequestNN extends RequestHandler {
	private final Node node;

	public RequestNN(Observer listener, Session session, Node node) {
		super(listener, session);
		this.node = node;
	}

	@Override
	protected String getSuffix() {
		return "/algnns";
	}

	public Node getNode() {
		return node;
	}

	@Override
	protected ArrayList<Node> getNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>(1);
		nodes.add(node);
		return nodes;
	}

	@Override
	protected ArrayList<Constraint> getConstraints() {
		return null;
	}
}
