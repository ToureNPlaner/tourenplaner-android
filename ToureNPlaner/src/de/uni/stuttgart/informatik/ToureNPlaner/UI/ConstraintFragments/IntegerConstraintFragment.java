package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public class IntegerConstraintFragment extends NumberConstraintFragment {
	public static IntegerConstraintFragment newInstance(Constraint constraint, int index) {
		IntegerConstraintFragment fragment = new IntegerConstraintFragment();
		fragment.constraint = constraint;
		return fragment;
	}
}
