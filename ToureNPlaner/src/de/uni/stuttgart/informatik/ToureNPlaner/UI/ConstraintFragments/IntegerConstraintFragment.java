package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import android.os.Bundle;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public class IntegerConstraintFragment extends NumberConstraintFragment {
	public static IntegerConstraintFragment newInstance(Constraint constraint, int index) {
		IntegerConstraintFragment fragment = new IntegerConstraintFragment();
		Bundle args = new Bundle();
		args.putSerializable("constraint", constraint);
		args.putInt("index", index);
		fragment.setArguments(args);
		return fragment;
	}
}
