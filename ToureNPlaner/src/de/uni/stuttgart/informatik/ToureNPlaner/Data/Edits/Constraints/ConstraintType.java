package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.Serializable;

public abstract class ConstraintType implements Serializable {
	private final String name;

	protected ConstraintType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract String getTypename();

	public static ConstraintType parse(JsonNode constraint) {
		String typename = constraint.path("type").asText();
		if (FloatConstraint.typename.equals(typename)) {
			return FloatConstraint.parse(constraint);
		} else if (MeterConstraint.typename.equals(typename)) {
			return MeterConstraint.parse(constraint);
		}
		throw new IllegalArgumentException("No constraing with that type!");
	}

	public abstract void generate(ObjectNode node, Object value);
}
