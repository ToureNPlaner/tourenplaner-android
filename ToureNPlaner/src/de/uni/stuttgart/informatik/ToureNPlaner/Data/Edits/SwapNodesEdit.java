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

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public class SwapNodesEdit extends Edit {
	private final int from;
	private final int to;

	public SwapNodesEdit(Session session, int from, int to) {
		super(session);
		this.from = from;
		this.to = to;
	}

	@Override
	public void perform() {
		Node tmp = session.getNodeModel().getNodeVector().remove(from);
		session.getNodeModel().getNodeVector().add(to, tmp);
		session.notifyChangeListerners(new Session.Change(Session.MODEL_CHANGE));
	}
}
