package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.FloatConstraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class NumberConstraintFragment extends ConstraintFragment {
	// shouldn't be about the pixel width
	protected static final int SEEKBAR_MAX = 1000;
	private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;

	protected float min;
	protected float max;

	public static NumberConstraintFragment newInstance(Constraint constraint, int index) {
		NumberConstraintFragment fragment = new NumberConstraintFragment();
		Bundle args = new Bundle();
		args.putSerializable("constraint", constraint);
		args.putInt("index", index);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FloatConstraint type = (FloatConstraint) constraint.getType();
		min = type.getMinimum();
		max = type.getMaximum();

		setup();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.float_constraint_layout, null);
	}

	/**
	 * Range 0.0 - 1.0
	 */
	protected float barToValueNormalized(float bar) {
		return bar;
	}

	/**
	 * Range 0.0 - 1.0
	 *
	 * @return
	 */
	protected float valueToBarNormalized(float value) {
		return value;
	}

	protected float barToValue(int bar) {
		return barToValueNormalized((float) bar / SEEKBAR_MAX) * (max - min) + min;
	}

	protected int valueToBar(float value) {
		return (int) (valueToBarNormalized((value - min) / (max - min)) * SEEKBAR_MAX);
	}

	protected void setup() {
		TextView lblMin = (TextView) getView().findViewById(R.id.lblconstMin);
		TextView lblMax = (TextView) getView().findViewById(R.id.lblconstMax);
		final EditText etValue = (EditText) getView().findViewById(R.id.txtconstValue);

		lblMin.setText(String.valueOf(min));
		lblMax.setText(String.valueOf(max));


		etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		//------------------get seekBar -------------------
		final SeekBar seekbar = (SeekBar) getView().findViewById(R.id.editconstraintseekBar);
		seekbar.setMax(SEEKBAR_MAX);

		if (constraint.getValue() != null) {
			float val = (Float) constraint.getValue();
			seekbar.setProgress(valueToBar(val));
			etValue.setText(Float.toString(val));
		} else {
			etValue.setHint(getResources().getString(R.string.select_a_value));
		}
		etValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String val = etValue.getEditableText().toString();
				if (val != null && !val.equals("")) {
					try {
						float valfloat = Float.valueOf(val);
						if (valfloat > max) {
							valfloat = max;
						}
						if (valfloat < min) {
							valfloat = min;
						}
						dirty = true;
						constraint.setValue(valfloat);
						// prevent infinite loop
						seekbar.setOnSeekBarChangeListener(null);
						seekbar.setProgress(valueToBar(valfloat));
						seekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
					} catch (NumberFormatException e) {
						// ignore
					}
				}
			}
		});
		onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1,
			                              boolean arg2) {
				etValue.setText(Float.toString(barToValue(arg1)));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

			}
		};
		seekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
	}
}
