package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import android.os.Bundle;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public class PriceConstraintFragment extends NumberConstraintFragment {
	public static PriceConstraintFragment newInstance(Constraint constraint, int index) {
		PriceConstraintFragment fragment = new PriceConstraintFragment();
		Bundle args = new Bundle();
		args.putSerializable("constraint", constraint);
		args.putInt("index", index);
		fragment.setArguments(args);
		return fragment;
	}
}
