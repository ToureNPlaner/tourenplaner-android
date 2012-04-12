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
