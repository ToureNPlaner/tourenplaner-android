package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.util.ArrayList;

public class ChangeNodeModelEdit extends Edit {
	private final ArrayList<Node> nodeVector;

	public ChangeNodeModelEdit(Session session, ArrayList<Node> nodeVector) {
		super(session);
		this.nodeVector = nodeVector;
	}

	@Override
	public void perform() {
		session.getNodeModel().setNodeVector(nodeVector);
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE));
	}
}
