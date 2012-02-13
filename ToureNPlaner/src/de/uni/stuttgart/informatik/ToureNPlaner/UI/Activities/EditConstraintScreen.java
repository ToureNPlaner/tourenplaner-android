package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;

public class EditConstraintScreen extends Activity {
	private Constraint constraint;
	private Bundle data;
	private ConstraintView constraintView;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putAll(data);
		super.onSaveInstanceState(outState);
	}

	void finishActivity() {
		Intent data = new Intent();
		data.putExtra("value", constraint.getValue());
		data.putExtra("index", this.data.getInt("index", 0));
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If we get created for the first time we get our data from the intent
		data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();

		constraint = (Constraint) data.getSerializable("constraint");

		constraintView = constraint.createView(this);

		setContentView(R.layout.editconstraints);

		final TextView etName = (TextView) findViewById(R.id.txtconstName);
		final TextView etType = (TextView) findViewById(R.id.txtconstType);

		etName.setText(constraint.getType().getName());
		etType.setText(constraint.getType().getTypename());

		Button btnReturn = (Button) findViewById(R.id.btnconstReturn);
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finishActivity();
			}
		});

		final ViewGroup placeholder = (ViewGroup) findViewById(R.id.EditConstraintPlaceHolder);

		getLayoutInflater().inflate(constraintView.getLayout(), placeholder);


		/*//------------------get TextView ------------------
		   TextView lblMin = (TextView) findViewById(R.id.lblconstMin);
		   TextView lblMax = (TextView) findViewById(R.id.lblconstMax);
		   //lblMin.setText(String.valueOf(constraint.getMinimumValue()));
		   //lblMax.setText(String.valueOf(constraint.getMaximumValue()));

		   //------------------get seekBar -------------------
		   final SeekBar seekbar = (SeekBar) findViewById(R.id.editconstraintseekBar);

		   if (constraintType.equals("float") || constraintType.equals("price")) {
			   etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			   divisionFactor = 100;
		   } else {
			   etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
			   divisionFactor = 1;
		   }

		   //seekbar.setMax(constraint.getMaximumValue().intValue() * divisionFactor);
		   if (constraint.getValue() != null) {

			   if (constraintType.equals("integer") || constraintType.equals("meter")) {
				   divisionFactor = 1;
				   // set seekbar progresslevel
				   seekbar.setProgress(Integer.valueOf(constraint.getValue().toString()));
			   }
			   if (constraintType.equals("boolean")) {
				   divisionFactor = 1;
				   // set seekbar progresslevel
				   int booleanValue = 0;
				   if ((Boolean) constraint.getValue() == true) {
					   booleanValue = 1;
				   }
				   seekbar.setProgress(booleanValue);
			   }
			   if (constraintType.equals("float") || constraintType.equals("price")) {
				   divisionFactor = 100;
				   // set seekbar progresslevel
				   float tempFloat = Float.valueOf(constraint.getValue().toString());
				   int tempInteger = (int) tempFloat;
				   seekbar.setProgress(tempInteger * divisionFactor);
			   }
		   }
		   seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			   @Override
			   public void onProgressChanged(SeekBar arg0, int arg1,
											 boolean arg2) {
				   if (constraintType.equals("float") || constraintType.equals("price")) {
					   etValue.setText(String.valueOf((float) arg1 / divisionFactor));
				   }

				   if (constraintType.equals("integer") || constraintType.equals("meter")) {
					   etValue.setText(String.valueOf(arg1 / divisionFactor));
				   }

				   if (constraintType.equals("boolean")) {
					   if (arg1 == 0) {
						   etValue.setText("false");
					   } else {
						   etValue.setText("true");
					   }
				   }

			   }

			   @Override
			   public void onStartTrackingTouch(SeekBar arg0) {

			   }

			   @Override
			   public void onStopTrackingTouch(SeekBar arg0) {

			   }
		   });*/
	}
}
