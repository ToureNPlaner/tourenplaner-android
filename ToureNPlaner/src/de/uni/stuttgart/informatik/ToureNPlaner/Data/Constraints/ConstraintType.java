package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

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
		} else if (PriceConstraint.typename.equals(typename)){
			return PriceConstraint.parse(constraint);
		} else if(IntegerConstraint.typename.equals(typename)){
			return IntegerConstraint.parse(constraint);
		}
		throw new IllegalArgumentException("No constraing with that type!");
	}

	public abstract void generate(ObjectNode node, Object value);

	public abstract ConstraintView createView(Context context, Constraint constraint);
}
