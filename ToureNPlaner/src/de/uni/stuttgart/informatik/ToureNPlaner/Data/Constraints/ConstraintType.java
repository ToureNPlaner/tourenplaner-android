package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;
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

	public abstract ConstraintView createView(Context context, Constraint constraint);
}
