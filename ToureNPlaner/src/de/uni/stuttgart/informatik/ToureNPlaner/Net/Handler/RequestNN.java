package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraint;
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