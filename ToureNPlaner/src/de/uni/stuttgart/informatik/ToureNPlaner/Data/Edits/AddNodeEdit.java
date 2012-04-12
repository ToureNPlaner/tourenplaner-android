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

public class AddNodeEdit extends Edit {
	private final Node node;
	private final int index;
	private final Position position;

	public static enum Position {
		BEGINNING, END, CUSTOM
	}

	public AddNodeEdit(Session session, Node node, Position position) {
		super(session);
		this.node = node;
		this.position = position;
		this.index = 0;
	}

	public AddNodeEdit(Session session, Node node, int index) {
		super(session);
		this.node = node;
		this.index = index;
		this.position = Position.CUSTOM;
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
			case CUSTOM:
				session.getNodeModel().getNodeVector().add(Math.min(session.getNodeModel().size(), Math.max(0, index)), node);
		}
		Session.Change change = new Session.Change(Session.MODEL_CHANGE | Session.ADD_CHANGE);
		change.setNode(node);

		session.notifyChangeListerners(change);
	}
}
