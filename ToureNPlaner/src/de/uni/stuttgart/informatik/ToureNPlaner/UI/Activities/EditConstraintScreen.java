package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;

public class EditConstraintScreen extends SherlockFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();

		Constraint constraint = (Constraint) data.getSerializable("constraint");
		int index = data.getInt("index", 0);

		if (savedInstanceState == null) {
			// First-time init; create fragment to embed in activity.
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ConstraintFragment newFragment = constraint.createFragment(index);
			ft.add(android.R.id.content, newFragment, "frag");
			ft.commit();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finishActivity();
		}
		return super.onKeyDown(keyCode, event);
	}

	void finishActivity() {
		Intent data = new Intent();

		ConstraintFragment fragment = (ConstraintFragment) getSupportFragmentManager().findFragmentByTag("frag");

		data.putExtra("value", fragment.getValue());
		data.putExtra("index", fragment.getIndex());
		setResult(RESULT_OK, data);
	}
}
