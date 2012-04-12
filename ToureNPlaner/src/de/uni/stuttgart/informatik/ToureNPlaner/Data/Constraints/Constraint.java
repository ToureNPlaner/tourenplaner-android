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

import java.io.Serializable;

public class Constraint implements Serializable {
	private Serializable value;
	private final ConstraintType type;

	public Constraint(ConstraintType type) {
		this.type = type;
	}

	public ConstraintType getType() {
		return type;
	}

	public Serializable getValue() {
		return value;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return type.getName();
	}

	public void generate(ObjectNode node) {
		type.generate(node, value);
	}

	public ConstraintFragment createFragment(int index) {
		return type.createFragment(this, index);
	}
}
