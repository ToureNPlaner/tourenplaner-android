package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class SetResultEdit extends Edit {
	private final Result result;

	public SetResultEdit(Session session, Result result) {
		super(session);
		this.result = result;
	}

	@Override
	public void perform() {
		session.setResult(result);
		session.notifyChangeListerners(Session.Change.RESULT_CHANGE);
	}
}
