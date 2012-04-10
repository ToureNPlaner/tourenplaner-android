package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import android.os.Bundle;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.io.Serializable;

public class ConstraintFragment extends SherlockFragment {
	protected Constraint constraint;
	protected int index;
	protected boolean dirty;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("constraint", constraint);
		outState.putInt("index", index);
		outState.putBoolean("dirty", dirty);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getArguments();
		constraint = (Constraint) data.get("constraint");
		index = data.getInt("index");
		dirty = data.getBoolean("dirty", false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		((TextView) getView().findViewById(R.id.name)).setText(constraint.getType().getName());
		((TextView) getView().findViewById(R.id.description)).setText(constraint.getType().getDescription());
	}

	public boolean isDirty() {
		return dirty;
	}

	public Serializable getValue() {
		return constraint.getValue();
	}

	public int getIndex() {
		return index;
	}

	public Constraint getConstraint() {
		return constraint;
	}
}
