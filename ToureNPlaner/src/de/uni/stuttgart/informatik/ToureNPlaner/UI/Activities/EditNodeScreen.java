package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.ConstraintListAdapter;

import java.io.Serializable;

public class EditNodeScreen extends Activity {
	public static final int RESULT_DELETE = RESULT_FIRST_USER;

	private Session session;
	private Node node;
	private int index;

	private ConstraintListAdapter adapter;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		outState.putSerializable("node", node);
		outState.putSerializable("index", index);

		super.onSaveInstanceState(outState);
	}

	void finishActivity() {
		Intent data = new Intent();
		data.putExtra("node", node);
		data.putExtra("index", index);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nodepreferences);

		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);
		node = (Node) data.getSerializable("node");
		index = data.getInt("index");

		if (!session.getSelectedAlgorithm().getPointConstraintTypes().isEmpty()) {
			setupListView();
		}
		setupButtons();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_OK:
				node.getConstraintList().get(data.getExtras().getInt("index")).setValue(data.getSerializableExtra("value"));
				adapter.notifyDataSetChanged();
				break;
		}
	}

	private void setupButtons() {
		// -------------- get EditTexts --------------
		final EditText etName = (EditText) findViewById(R.id.etName);
		// -------------- get Buttons --------------
		Button btnDelete = (Button) findViewById(R.id.btnDelete);
		Button btnSave = (Button) findViewById(R.id.btnSave);
		Button btnReturn = (Button) findViewById(R.id.btnReturn);
		etName.setText(node.getName());
		// -----------------btnSave-----------------------
		btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				node.setName(etName.getText().toString());
				finishActivity();
			}
		});
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED, null);
				finish();
			}
		});
		// -----------------btnDelete-----------------------
		btnDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_DELETE, new Intent().putExtra("index", index));
				finish();
			}
		});


	}

	private void setupListView() {

		ListView listView = (ListView) findViewById(R.id.listViewNodePreferences);
		TextView constraintLabel = (TextView) this.findViewById(R.id.lblNodePreferencesConstraintText);
		// set title visible
		constraintLabel.setVisibility(0);
		adapter = new ConstraintListAdapter(node.getConstraintList(), this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent myIntent = new Intent(EditNodeScreen.this,
						EditConstraintScreen.class);
				myIntent.putExtra("constraint", (Serializable) adapterView.getItemAtPosition(i));
				myIntent.putExtra("index", i);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivityForResult(myIntent, 0);
			}
		});
	}
}
