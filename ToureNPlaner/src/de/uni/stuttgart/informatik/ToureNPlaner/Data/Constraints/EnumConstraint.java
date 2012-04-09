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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.EnumConstraintFragment;

import java.util.ArrayList;

import static de.uni.stuttgart.informatik.ToureNPlaner.Util.MakeIterable.in;

public class EnumConstraint extends ConstraintType {
	public static final String typename = "enum";
	private ArrayList<String> values;

	public EnumConstraint(String name, String description, String id, ArrayList<String> values) {
		super(name, description, id);
		this.values = values;
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
		return EnumConstraintFragment.newInstance(constraint, index);
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public static ConstraintType parse(JsonNode constraint) {
		ArrayList<String> values = new ArrayList<String>();

		for (JsonNode in : in(constraint.path("values").elements())) {
			values.add(in.asText());
		}

		return new EnumConstraint(constraint.path("name").asText(),
				constraint.path("description").asText(),
				constraint.path("id").asText(),
				values);
	}
}
