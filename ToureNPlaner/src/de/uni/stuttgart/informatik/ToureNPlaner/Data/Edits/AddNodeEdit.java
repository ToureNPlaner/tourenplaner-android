package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class AddNodeEdit extends Edit {
	private final Node node;
	private final Position position;

	public static enum Position {
		BEGINNING, END
	}

	public AddNodeEdit(Session session, Node node, Position position) {
		super(session);
		this.node = node;
		this.position = position;
	}

	@Override
	public void perform() {
		switch (position) {
			case BEGINNING:
				session.getNodeModel().addBeginning(node);
				break;
			case END:
				session.getNodeModel().add(node);
				break;
		}
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE | Session.ADD_CHANGE));
	}
}
