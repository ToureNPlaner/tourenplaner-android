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
