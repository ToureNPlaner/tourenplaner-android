package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;

public class IntegerConstraint extends ConstraintType {
	public static final String typename = "integer";
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
		int min = constraint.path("min").asInt(0);
		int max = constraint.path("max").asInt(10000);

		return new IntegerConstraint(constraint.path("name").asText(),
				constraint.path("description").asText(),
				constraint.path("id").asText(),
				min, max);
	}

	@Override
	public ConstraintFragment createFragment(Constraint constraint, int index) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}


	public int getMinimum() {
		return minimum;
	}

	public int getMaximum() {
		return maximum;
	}

}
