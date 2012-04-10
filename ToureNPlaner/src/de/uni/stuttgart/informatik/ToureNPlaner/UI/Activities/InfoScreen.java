package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Fragments.InfoFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Fragments.NodeListFragment;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.TabListener;

public class InfoScreen extends SherlockFragmentActivity {
	private Session session;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		outState.putInt("Tab", getSupportActionBar().getSelectedNavigationIndex());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab = getSupportActionBar().newTab()
				.setText(R.string.info)
				.setTabListener(new TabListener<NodeListFragment>(
						this, "NodeList", NodeListFragment.class));
		actionBar.addTab(tab);

		if (session.getResult() != null) {
			tab = getSupportActionBar().newTab()
					.setText(R.string.Result)
					.setTabListener(new TabListener<InfoFragment>(
							this, "Info", InfoFragment.class));
			actionBar.addTab(tab);
		}

		if (savedInstanceState != null)
			getSupportActionBar().selectTab(getSupportActionBar().getTabAt(savedInstanceState.getInt("Tab", 0)));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent data = new Intent();
			data.putExtra(NodeModel.IDENTIFIER, session.getNodeModel().getNodeVector());
			boolean dirty = ((NodeListFragment) getSupportFragmentManager().findFragmentByTag("NodeList")).isDirty();
			setResult(dirty ? RESULT_OK : RESULT_CANCELED, data);
		}
		return super.onKeyDown(keyCode, event);
	}
}
