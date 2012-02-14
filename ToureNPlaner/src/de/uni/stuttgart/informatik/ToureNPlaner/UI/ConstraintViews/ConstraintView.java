package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public abstract class ConstraintView {
	protected final Constraint constraint;

	public ConstraintView(Context context, Constraint constraint) {
		this.constraint = constraint;
	}

	public abstract int getLayout();
}
