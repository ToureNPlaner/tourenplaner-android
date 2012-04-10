package de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;

import java.io.Serializable;

public class Constraint implements Serializable {
	private Serializable value;
	private final ConstraintType type;

	public Constraint(ConstraintType type) {
		this.type = type;
	}

	public ConstraintType getType() {
		return type;
	}

	public Serializable getValue() {
		return value;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return type.getName();
	}

	public void generate(ObjectNode node) {
		type.generate(node, value);
	}

	public ConstraintFragment createFragment(int index) {
		return type.createFragment(this, index);
	}
}
