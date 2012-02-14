package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.MeterConstraintView;
import org.codehaus.jackson.JsonNode;

public class MeterConstraint extends FloatConstraint {
	public static String typename = "meter";

	public MeterConstraint(String name, float minimum, float maximum) {
		super(name, minimum, maximum);
	}

	@Override
	public String getTypename() {
		return typename;
	}

	public static ConstraintType parse(JsonNode constraint) {
		return new MeterConstraint(constraint.path("name").asText(),
				(float) constraint.path("min").asDouble(0.0),
				(float) constraint.path("max").asDouble(0.0));
	}

	@Override
	public ConstraintView createView(Context context, Constraint constraint) {
		return new MeterConstraintView(context, constraint);
	}
}
