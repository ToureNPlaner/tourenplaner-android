package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class ChangeNodeModelEdit extends Edit {
	private final NodeModel nodeModel;

	public ChangeNodeModelEdit(Session session, NodeModel nodeModel) {
		super(session);
		this.nodeModel = nodeModel;
	}

	@Override
	public void perform() {
		session.setNodeModel(nodeModel);
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE));
	}
}
