package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.MeterConstraintView;
import org.codehaus.jackson.JsonNode;

public class MeterConstraint extends FloatConstraint {
	public static String typename = "meter";

	public MeterConstraint(String name, String description, String id, float minimum, float maximum) {
		super(name, description, id, minimum, maximum);
	}

	@Override
	public String getTypename() {
		return typename;
	}

	public static ConstraintType parse(JsonNode constraint) {
		float min = (float) constraint.path("min").asDouble(0.0);
		float max;
		JsonNode n = constraint.get("max");
		if (n == null)
			max = 18000.f;
		else
			max = (float) n.getDoubleValue();

		return new MeterConstraint(constraint.path("name").asText(),
				constraint.path("description").asText(),
				constraint.path("id").asText(),
				min, max);
	}

	@Override
	public ConstraintView createView(Context context, Constraint constraint) {
		return new MeterConstraintView(context, constraint);
	}
}
