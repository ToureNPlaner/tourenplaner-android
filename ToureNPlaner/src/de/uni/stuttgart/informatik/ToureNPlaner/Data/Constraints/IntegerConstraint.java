package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.IntegerConstraintView;

public class IntegerConstraint extends ConstraintType{

	public static String typename = "integer";
	private final int minimum;
	private final int maximum;

	public IntegerConstraint(String name, String description, String id, int minimum, int maximum) {
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
		node.put(getId(), (Integer) value);
	}

	public static ConstraintType parse(JsonNode constraint) {
		int min = (int) constraint.path("min").asInt(0);
		int max;
		JsonNode n = constraint.get("max");
		if (n == null)
			max = 10000;
		else
			max = (int) n.getIntValue();

		return new IntegerConstraint(constraint.path("name").asText(),
				constraint.path("description").asText(),
				constraint.path("id").asText(),
				min, max);
	}

	@Override
	public ConstraintView createView(Context context, Constraint constraint) {
		return new IntegerConstraintView(context, constraint);
	}

	public int getMinimum() {
		return minimum;
	}

	public int getMaximum() {
		return maximum;
	}

}
