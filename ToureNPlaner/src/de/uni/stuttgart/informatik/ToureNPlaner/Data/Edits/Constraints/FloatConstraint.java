package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

public class FloatConstraint extends ConstraintType {
	public static String typename = "float";

	public FloatConstraint(String name) {
		super(name);
	}

	@Override
	public String getTypename() {
		return typename;
	}

	@Override
	public void generate(ObjectNode node, Object value) {
		node.put(getName(), (Float) value);
	}

	public static ConstraintType parse(JsonNode constraint) {
		return new FloatConstraint(constraint.path("name").asText());
	}
}
