/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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

		session.notifyChangeListerners(new Session.Change(Session.RESULT_CHANGE));
	}
}
