package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public class PriceConstraintFragment extends NumberConstraintFragment {
	public static PriceConstraintFragment newInstance(Constraint constraint, int index) {
		PriceConstraintFragment fragment = new PriceConstraintFragment();
		fragment.constraint = constraint;
		return fragment;
	}
}
