package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintViews.ConstraintView;

public class EditConstraintScreen extends Activity {
	private Constraint constraint;
	private int index;

	private ConstraintView constraintView;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("index", index);
		outState.putSerializable("constraint", constraint);
		super.onSaveInstanceState(outState);
	}

	void finishActivity() {
		Intent data = new Intent();
		data.putExtra("value", constraint.getValue());
		data.putExtra("index", index);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();

		constraint = (Constraint) data.getSerializable("constraint");
		index = data.getInt("index", 0);

		setupView();
	}

	private void setupView() {
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

		constraintView = constraint.createView(this);
		constraintView.attach(getLayoutInflater(), placeholder);
	}
}
