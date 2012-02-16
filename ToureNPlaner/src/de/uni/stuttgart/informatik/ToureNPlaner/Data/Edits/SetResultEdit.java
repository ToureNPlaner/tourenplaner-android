package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ResultNode;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.util.ArrayList;

public class SetResultEdit extends Edit {
	private final Result result;

	public SetResultEdit(Session session, Result result) {
		super(session);
		this.result = result;
	}

	@Override
	public void perform() {
		session.setResult(result);
		// reorder nodes
		/*if (result != null) {
			ArrayList<Node> nodes = session.getNodeModel().getNodeVector();
			ArrayList<ResultNode> resultNodes = result.getPoints();
			if (resultNodes.size() != nodes.size()) {
				return;
			}
			// TODO algorithm is O(n^2)
			ArrayList<Node> copy = new ArrayList<Node>(resultNodes.size());
			for (int i = 0; i < resultNodes.size(); i++) {
				for (int n = 0; n < nodes.size(); n++) {
					if (resultNodes.get(i).getId() == nodes.get(n).getId()) {
						copy.add(nodes.get(n));
						break;
					}
				}
			}
			if (copy.size() == nodes.size())
				session.getNodeModel().setNodeVector(copy);
		}*/

		session.notifyChangeListerners(Session.RESULT_CHANGE);
	}
}
