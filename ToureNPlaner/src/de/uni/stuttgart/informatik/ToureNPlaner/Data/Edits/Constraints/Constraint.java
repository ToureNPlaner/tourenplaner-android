package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;
import org.codehaus.jackson.node.ObjectNode;

import java.io.Serializable;

public class Constraint implements Serializable {
	private Serializable value;
	private final ConstraintType type;

	public Constraint(ConstraintType type) {
		this.type = type;
	}

	public Constraint(Constraint constraint) {
		this.type = constraint.type;
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

	public ConstraintView createView(Context context) {
		return type.createView(context, this);
	}
}
