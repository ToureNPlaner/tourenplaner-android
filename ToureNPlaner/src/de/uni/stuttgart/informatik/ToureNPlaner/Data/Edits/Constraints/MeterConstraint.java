package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.MeterConstraintView;
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
		return new MeterConstraint(constraint.path("name").asText());
	}

	@Override
	public ConstraintView createView(Context context, Constraint constraint) {
		return new MeterConstraintView(context, constraint);
	}
}
