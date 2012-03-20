package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public class MeterConstraintView extends FloatConstraintView {
	public MeterConstraintView(Context context, Constraint constraint) {
		super(context, constraint);
	}

	private static final float factor = 3.f;

	@Override
	protected float barToValueNormalized(float bar) {
		return (float) Math.pow(bar, factor);
	}

	@Override
	protected float valueToBarNormalized(float value) {
		return (float) Math.pow(value, 1 / factor);
	}
}
