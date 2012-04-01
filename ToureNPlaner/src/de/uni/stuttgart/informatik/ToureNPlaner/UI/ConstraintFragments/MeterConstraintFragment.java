package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import android.os.Bundle;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public class MeterConstraintFragment extends NumberConstraintFragment {
	private static final float factor = 3.f;

	public static MeterConstraintFragment newInstance(Constraint constraint, int index) {
		MeterConstraintFragment fragment = new MeterConstraintFragment();
		Bundle args = new Bundle();
		args.putSerializable("constraint", constraint);
		args.putInt("index", index);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected float barToValueNormalized(float bar) {
		return (float) Math.pow(bar, factor);
	}

	@Override
	protected float valueToBarNormalized(float value) {
		return (float) Math.pow(value, 1 / factor);
	}
}
