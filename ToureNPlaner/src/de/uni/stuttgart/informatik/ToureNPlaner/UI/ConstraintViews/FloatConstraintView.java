package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.FloatConstraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class FloatConstraintView extends ConstraintView {
	private static final int seekBarMax = 10000;
	private final float divisionFactor;

	public FloatConstraintView(Context context, Constraint constraint) {
		super(context, constraint);
		FloatConstraint type = (FloatConstraint) constraint.getType();
		divisionFactor = (type.getMaximum() - type.getMinimum()) / (float) seekBarMax;
	}

	@Override
	public int getLayout() {
		return R.layout.float_constraint_layout;
	}

	@Override
	protected void setup() {
		TextView lblMin = (TextView) view.findViewById(R.id.lblconstMin);
		TextView lblMax = (TextView) view.findViewById(R.id.lblconstMax);
		final EditText etValue = (EditText) view.findViewById(R.id.txtconstValue);

		final FloatConstraint type = (FloatConstraint) constraint.getType();

		lblMin.setText(String.valueOf(type.getMinimum()));
		lblMax.setText(String.valueOf(type.getMaximum()));

		etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		//------------------get seekBar -------------------
		final SeekBar seekbar = (SeekBar) view.findViewById(R.id.editconstraintseekBar);
		seekbar.setMax(seekBarMax);
		if (constraint.getValue() != null) {
			float val = (Float) constraint.getValue();
			seekbar.setProgress((int) (val * divisionFactor));
		} else {
			// TODO localize
			etValue.setHint("Choose a value.");
		}
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1,
			                              boolean arg2) {
				float val = type.getMinimum() + (float) arg1 / divisionFactor;
				constraint.setValue(val);
				etValue.setText(Float.toString(val));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

			}
		});
	}
}
