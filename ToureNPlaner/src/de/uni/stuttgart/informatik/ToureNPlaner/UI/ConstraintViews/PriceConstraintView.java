package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public class PriceConstraintView extends FloatConstraintView {
	public PriceConstraintView(Context context, Constraint constraint) {
			super(context, constraint,100);
	}
}
