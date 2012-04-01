package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.MeterConstraintFragment;
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
			max = 10000.f;
		else
			max = (float) n.getDoubleValue();

		return new MeterConstraint(constraint.path("name").asText(),
				constraint.path("description").asText(),
				constraint.path("id").asText(),
				min, max);
	}

	@Override
	public ConstraintFragment createFragment(Constraint constraint, int index) {
		return MeterConstraintFragment.newInstance(constraint, index);
	}
}
