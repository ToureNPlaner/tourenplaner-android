package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class RemoveNodeEdit extends Edit {
	private final int index;

	public RemoveNodeEdit(Session session, int index) {
		super(session);
		this.index = index;
	}

	@Override
	public void perform() {
		session.getNodeModel().remove(index);
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE));
	}
}
