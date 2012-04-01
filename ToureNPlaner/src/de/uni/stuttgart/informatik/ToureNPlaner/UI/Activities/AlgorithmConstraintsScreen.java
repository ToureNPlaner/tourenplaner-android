package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;

import java.util.ArrayList;

public class AlgorithmConstraintsScreen extends SherlockFragmentActivity {
	public static String IDENTIFIER = "constraints";

	private ArrayList<Constraint> constraints;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(IDENTIFIER, constraints);
		super.onSaveInstanceState(outState);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		constraints = (ArrayList<Constraint>) data.getSerializable(IDENTIFIER);

		setContentView(R.layout.algorithmconstrainsscreen);

		setupList();

		if (savedInstanceState == null)
			setupFragments();
	}

	private void setupFragments() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		for (int i = 0; i < constraints.size(); i++) {
			ft.add(i + 1, constraints.get(i).createFragment(i));
		}
		ft.commit();
	}

	private void setupList() {
		LinearLayout list = (LinearLayout) findViewById(android.R.id.list);
		for (int i = 0; i < constraints.size(); i++) {
			FrameLayout frameLayout = new FrameLayout(this);
			frameLayout.setId(i + 1);
			list.addView(frameLayout);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent data = new Intent();
			boolean dirty = false;
			for (int i = 0; i < constraints.size(); i++) {
				if (((ConstraintFragment) getSupportFragmentManager().findFragmentById(i + 1)).isDirty()) {
					dirty = true;
				}
			}
			if (dirty)
				data.putExtra(IDENTIFIER, constraints);
			setResult(dirty ? RESULT_OK : RESULT_CANCELED, data);
		}
		return super.onKeyDown(keyCode, event);
	}
}

