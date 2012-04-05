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

package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.StringConstraintFragment;

public class StringConstraint extends ConstraintType {
	public static String typename = "string";

	public StringConstraint(String name, String description, String id) {
		super(name, description, id);
	}

	@Override
	public String getTypename() {
		return typename;
	}

	@Override
	public void generate(ObjectNode node, Object value) {
		node.put(getId(), (String) value);
	}

	@Override
	public ConstraintFragment createFragment(Constraint constraint, int index) {
		return StringConstraintFragment.newInstance(constraint, index);
	}
}
