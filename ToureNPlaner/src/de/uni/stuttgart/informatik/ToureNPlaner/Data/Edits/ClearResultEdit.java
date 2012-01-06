package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class ClearResultEdit extends Edit {
	public ClearResultEdit(Session session) {
		super(session);
	}

	@Override
	public void perform() {
		session.setResult(null);
		session.notifyChangeListerners(Session.RESULT_CHANGE);
	}
}
