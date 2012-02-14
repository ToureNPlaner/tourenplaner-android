package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.ConstraintType;
import org.codehaus.jackson.JsonNode;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static ArrayList<Node> parse(JsonNode object) {

		ArrayList<Node> nodeList = new ArrayList<Node>();
		JsonNode points = object.get("points");
		for (JsonNode pointlist : points) {
			nodeList.add(parsePoints(pointlist));
		}
		return nodeList;
	}

	public static Node parsePoints(JsonNode object) {
		String name = object.get("name").asText();
		int lt = object.get("lt").asInt();
		int ln = object.get("ln").asInt();

		ArrayList<ConstraintType> alct = new ArrayList<ConstraintType>();
		Node node = new Node(0, name, lt, ln, alct);
		return node;

	}

}
