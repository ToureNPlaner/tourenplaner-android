package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints;

import org.codehaus.jackson.JsonNode;

public class MeterConstraint extends FloatConstraint {
	public static String typename = "meter";

	@Override
	public String getTypename() {
		return typename;
	}

	public MeterConstraint(String name) {
		super(name);
	}

	public static ConstraintType parse(JsonNode constraint) {
		return new FloatConstraint(constraint.path("name").asText());
	}
}
