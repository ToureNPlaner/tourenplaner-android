/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.io.Serializable;

public class ConstraintFragment extends Fragment {
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
