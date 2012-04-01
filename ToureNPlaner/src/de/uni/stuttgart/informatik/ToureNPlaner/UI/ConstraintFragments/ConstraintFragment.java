package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import com.actionbarsherlock.app.SherlockFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

import java.io.Serializable;

public class ConstraintFragment extends SherlockFragment {
	protected Constraint constraint;
	protected int index;

	public Serializable getValue() {
		return constraint.getValue();
	}

	public int getIndex() {
		return index;
	}
}
