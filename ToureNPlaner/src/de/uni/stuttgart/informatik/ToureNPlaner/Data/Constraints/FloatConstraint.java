package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.FloatConstraintView;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

public class FloatConstraint extends ConstraintType {
	public static String typename = "float";
	private final float minimum;
	private final float maximum;

	public FloatConstraint(String name, String description, String id, float minimum, float maximum) {
		super(name, description, id);
		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	public String getTypename() {
		return typename;
	}

	@Override
	public void generate(ObjectNode node, Object value) {
		node.put(getId(), (Float) value);
	}

	public static ConstraintType parse(JsonNode constraint) {
		float min = (float) constraint.path("min").asDouble(0.0);
		float max;
		JsonNode n = constraint.get("max");
		if (n == null)
			max = 100000.f;
		else
			max = (float) n.getDoubleValue();

		return new FloatConstraint(constraint.path("name").asText(),
				constraint.path("description").asText(),
				constraint.path("id").asText(),
				min, max);
	}

	@Override
	public ConstraintView createView(Context context, Constraint constraint) {
		return new FloatConstraintView(context, constraint, 100);
	}

	public float getMinimum() {
		return minimum;
	}

	public float getMaximum() {
		return maximum;
	}
}
