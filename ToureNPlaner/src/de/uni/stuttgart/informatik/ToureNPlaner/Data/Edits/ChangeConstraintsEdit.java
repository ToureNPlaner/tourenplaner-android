package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.util.ArrayList;

public class ChangeConstraintsEdit extends Edit {
	private final ArrayList<Constraint> constraints;

	public ChangeConstraintsEdit(Session session, ArrayList<Constraint> constraints) {
		super(session);
		this.constraints = constraints;
	}

	@Override
	public void perform() {
		session.setConstraints(constraints);
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE));
	}
}
