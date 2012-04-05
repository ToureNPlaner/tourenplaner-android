package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;

import java.io.Serializable;

public abstract class ConstraintType implements Serializable {
	private final String name;
	private final String id;
	private final String description;

	protected ConstraintType(String name, String description, String id) {
		this.name = name;
		this.description = description;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
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
		} else if (PriceConstraint.typename.equals(typename)) {
			return PriceConstraint.parse(constraint);
		} else if (IntegerConstraint.typename.equals(typename)) {
			return IntegerConstraint.parse(constraint);
		} else if (StringConstraint.typename.equals(typename)) {
			return StringConstraint.parse(constraint);
		}
		throw new IllegalArgumentException("No constraint with that type!");
	}

	public abstract void generate(ObjectNode node, Object value);

	public abstract ConstraintFragment createFragment(Constraint constraint, int index);
}
