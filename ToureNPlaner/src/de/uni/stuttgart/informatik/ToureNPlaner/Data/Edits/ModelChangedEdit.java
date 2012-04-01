package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class ModelChangedEdit extends Edit {
	public ModelChangedEdit(Session session) {
		super(session);
	}

	@Override
	public void perform() {
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE));
	}
}
