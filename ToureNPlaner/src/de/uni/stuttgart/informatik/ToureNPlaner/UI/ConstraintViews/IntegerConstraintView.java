package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.IntegerConstraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class IntegerConstraintView extends ConstraintView {
	public IntegerConstraintView(Context context, Constraint constraint) {
		super(context, constraint);
	}

	@Override
	public int getLayout() {
		// can be changed if needed
		return R.layout.float_constraint_layout;
	}

	@Override
	protected void setup() {
		TextView lblMin = (TextView) view.findViewById(R.id.lblconstMin);
		TextView lblMax = (TextView) view.findViewById(R.id.lblconstMax);
		final EditText etValue = (EditText) view.findViewById(R.id.txtconstValue);

		final IntegerConstraint type = (IntegerConstraint) constraint.getType();

		lblMin.setText(String.valueOf(type.getMinimum()));
		lblMax.setText(String.valueOf(type.getMaximum()));


		etValue.setInputType(InputType.TYPE_CLASS_NUMBER);

		//------------------get seekBar -------------------
		final SeekBar seekbar = (SeekBar) view.findViewById(R.id.editconstraintseekBar);
		seekbar.setMax((int) type.getMaximum());
		if (constraint.getValue() != null) {
			int val = (Integer) constraint.getValue();
			seekbar.setProgress((int) (val));
			etValue.setText(Integer.toString(val));
		} else {
			etValue.setHint(context.getResources().getString(R.string.select_a_value));
		}
		etValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
			                          int count) {
				String val = etValue.getEditableText().toString();
				if (val != null && !val.equals("")) {
					Integer valint = Integer.valueOf(val);
					if (valint > type.getMaximum()) {
						valint = type.getMaximum();
					}
					if (valint < type.getMinimum()) {
						valint = type.getMinimum();
					}
					constraint.setValue(valint);
				}
			}
		});
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1,
			                              boolean arg2) {
				int val = type.getMinimum() + (int) arg1;
				etValue.setText(Integer.toString(val));
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
