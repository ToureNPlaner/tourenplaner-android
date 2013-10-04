/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Fragments.ConstraintListFragment;

public class EditNodeScreen extends Activity {
	public static final int RESULT_DELETE = RESULT_FIRST_USER;

	private Session session;
	private Node node;
	private int index;

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

	private void setupButtons() {
		// -------------- get EditTexts --------------
		final EditText etName = (EditText) findViewById(R.id.etName);
		// -------------- get Buttons --------------
		Button btnDelete = (Button) findViewById(R.id.btnDelete);
		Button btnSave = (Button) findViewById(R.id.btnSave);
		etName.setText(node.getName());
		// -----------------btnSave-----------------------
		btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ConstraintListFragment fragment = (ConstraintListFragment) getFragmentManager().findFragmentByTag("list");
				if (fragment != null && fragment.isDirty())
					node.setConstraintList(fragment.getConstraints());
				node.setName(etName.getText().toString());
				finishActivity();
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
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(android.R.id.list, ConstraintListFragment.newInstance(node.getConstraintList()), "list");
		ft.commit();
	}
}
