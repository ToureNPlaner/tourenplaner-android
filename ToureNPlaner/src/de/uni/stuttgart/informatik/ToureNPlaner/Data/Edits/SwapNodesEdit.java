package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class SwapNodesEdit extends Edit {
	private final int from;
	private final int to;

	public SwapNodesEdit(Session session, int from, int to) {
		super(session);
		this.from = from;
		this.to = to;
	}

	@Override
	public void perform() {
		Node tmp = session.getNodeModel().getNodeVector().remove(from);
		session.getNodeModel().getNodeVector().add(to, tmp);
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE));
	}
}
