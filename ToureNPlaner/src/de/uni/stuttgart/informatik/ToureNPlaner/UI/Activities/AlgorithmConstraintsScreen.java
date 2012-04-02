package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Fragments.ConstraintListFragment;

import java.util.ArrayList;

public class AlgorithmConstraintsScreen extends SherlockFragmentActivity {
	public static String IDENTIFIER = "constraints";

	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, ConstraintListFragment.newInstance(
					(ArrayList<Constraint>) getIntent().getExtras().getSerializable(IDENTIFIER)), "list");
			ft.commit();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			ConstraintListFragment fragment = ((ConstraintListFragment) getSupportFragmentManager().findFragmentByTag("list"));

			Intent data = new Intent();
			boolean dirty = fragment.isDirty();
			if (dirty)
				data.putExtra(IDENTIFIER, fragment.getConstraints());
			setResult(dirty ? RESULT_OK : RESULT_CANCELED, data);
		}
		return super.onKeyDown(keyCode, event);
	}
}

