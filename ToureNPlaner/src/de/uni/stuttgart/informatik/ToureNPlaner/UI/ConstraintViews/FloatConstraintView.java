package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class FloatConstraintView extends ConstraintView {
	public FloatConstraintView(Context context, Constraint constraint) {
		super(context, constraint);
	}

	@Override
	public int getLayout() {
		return R.layout.float_constraint_layout;
	}
}
