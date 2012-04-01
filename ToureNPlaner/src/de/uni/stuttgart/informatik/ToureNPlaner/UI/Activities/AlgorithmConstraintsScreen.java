package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import com.actionbarsherlock.app.SherlockListActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.ConstraintListAdapter;

import java.io.Serializable;

public class AlgorithmConstraintsScreen extends SherlockListActivity {
	private Session session;
	private boolean dirty = false;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		outState.putBoolean("dirty", dirty);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);

		if (savedInstanceState != null) {
			dirty = savedInstanceState.getBoolean("dirty");
		}

		setupListView();
	}

	private void setupListView() {
		ConstraintListAdapter adapter = new ConstraintListAdapter(session.getConstraints(), this);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent myIntent = new Intent(AlgorithmConstraintsScreen.this,
						EditConstraintScreen.class);
				myIntent.putExtra("constraint", (Serializable) adapterView.getItemAtPosition(i));
				myIntent.putExtra("index", i);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivityForResult(myIntent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_OK:
				session.getConstraints().get(
						data.getExtras().getInt("index")).setValue(data.getSerializableExtra("value"));
				((ConstraintListAdapter) getListAdapter()).notifyDataSetChanged();
				dirty = true;
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent data = new Intent();
			data.putExtra(NodeModel.IDENTIFIER, session.getNodeModel());
			setResult(dirty ? RESULT_OK : RESULT_CANCELED, data);
		}
		return super.onKeyDown(keyCode, event);
	}
}

