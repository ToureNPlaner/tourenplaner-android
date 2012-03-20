package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class ClearEdit extends Edit {
	public ClearEdit(Session session) {
		super(session);
	}

	@Override
	public void perform() {
		session.getNodeModel().clear();
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE));
	}
}
