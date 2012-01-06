package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class ReverseNodesEdit extends Edit {
	public ReverseNodesEdit(Session session) {
		super(session);
	}

	@Override
	public void perform() {
		session.getNodeModel().reverseNodes();
		session.notifyChangeListerners(Session.MODEL_CHANGE);
	}
}
