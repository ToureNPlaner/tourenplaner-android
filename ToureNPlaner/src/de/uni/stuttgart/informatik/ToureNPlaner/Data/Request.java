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

package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

import java.util.ArrayList;

public class Request {

	private static JsonNode generate(JsonNodeFactory factory, Node node) {
		ObjectNode o = new ObjectNode(factory);
		o.put("lt", node.getLaE7());
		o.put("ln", node.getLoE7());
		o.put("id", node.getId());
		o.put("name", node.getName());
		o.put("shortname", node.getShortName());

		generateConstraints(factory, node.getConstraintList(), o);
		return o;
	}

	public static ObjectNode generate(JsonNodeFactory factory, ArrayList<Node> nodes, ArrayList<Constraint> constraints) {
		ObjectNode root = new ObjectNode(factory);
		ArrayNode nodesArray = new ArrayNode(factory);

		for (int i = 0; i < nodes.size(); i++) {
			nodesArray.add(generate(factory, nodes.get(i)));
		}
		root.put("points", nodesArray);

		ObjectNode n = new ObjectNode(factory);
		generateConstraints(factory, constraints, n);
		root.put("constraints", n);

		return root;
	}

	private static void generateConstraints(JsonNodeFactory factory, ArrayList<Constraint> constraints, ObjectNode root) {
		if (constraints != null && !constraints.isEmpty()) {
			ObjectNode constraintsNode = new ObjectNode(factory);
			for (int i = 0; i < constraints.size(); i++) {
				if (constraints.get(i).getValue() != null) {
					constraints.get(i).generate(constraintsNode);
				}
			}
			root.putAll(constraintsNode);
		}
	}
}
