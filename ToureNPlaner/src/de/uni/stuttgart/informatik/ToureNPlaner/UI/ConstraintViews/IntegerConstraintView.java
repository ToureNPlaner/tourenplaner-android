package de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.IntegerConstraint;

public class IntegerConstraintView extends ConstraintView{
	private Context context;
	public IntegerConstraintView(Context context, Constraint constraint) {
		super(context, constraint);
		IntegerConstraint type = (IntegerConstraint) constraint.getType();
		this.context = context;
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
		

		etValue.setInputType(InputType.TYPE_CLASS_NUMBER );

		//------------------get seekBar -------------------
		final SeekBar seekbar = (SeekBar) view.findViewById(R.id.editconstraintseekBar);
		seekbar.setMax((int)type.getMaximum());
		if (constraint.getValue() != null) {
			int val = (Integer) constraint.getValue();
			seekbar.setProgress((int) (val));
		} else {
			etValue.setHint(context.getResources().getString(R.string.select_a_value));
		}
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1,
			                              boolean arg2) {
				int val = type.getMinimum() + (int) arg1;
				constraint.setValue(val);
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
