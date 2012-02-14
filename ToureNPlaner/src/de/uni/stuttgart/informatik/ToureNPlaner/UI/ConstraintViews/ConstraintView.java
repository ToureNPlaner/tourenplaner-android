package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;

public abstract class ConstraintView {
	protected final Constraint constraint;
	protected View view;

	public ConstraintView(Context context, Constraint constraint) {
		this.constraint = constraint;
	}

	public abstract int getLayout();

	protected abstract void setup();

	public void attach(LayoutInflater layoutInflater, ViewGroup placeholder) {
		view = layoutInflater.inflate(getLayout(), placeholder);
		setup();
	}
}
