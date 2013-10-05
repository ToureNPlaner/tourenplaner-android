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

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.ConstraintFragments.ConstraintFragment;

import java.util.ArrayList;

public class ConstraintListFragment extends Fragment {
	public static String IDENTIFIER = "constraints";

	private ArrayList<Constraint> constraints;

	public boolean isDirty() {
		for (int i = 0; i < constraints.size(); i++) {
			if (((ConstraintFragment)  getActivity().getSupportFragmentManager().findFragmentById(i + 1)).isDirty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(IDENTIFIER, constraints);
		super.onSaveInstanceState(outState);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getArguments();
		constraints = (ArrayList<Constraint>) data.getSerializable(IDENTIFIER);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setupList();

		if (savedInstanceState == null)
			setupFragments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.constraint_list, null);
	}

	private void setupFragments() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		for (int i = 0; i < constraints.size(); i++) {
			ft.add(i + 1, constraints.get(i).createFragment(i));
		}
		ft.commit();
	}

	private void setupList() {
		LinearLayout list = (LinearLayout) getView().findViewById(android.R.id.list);
		for (int i = 0; i < constraints.size(); i++) {
			FrameLayout frameLayout = new FrameLayout(getActivity());
			frameLayout.setId(i + 1);
			list.addView(frameLayout);
		}
	}

	public static ConstraintListFragment newInstance(ArrayList<Constraint> constraints) {
		ConstraintListFragment fragment = new ConstraintListFragment();
		Bundle args = new Bundle();
		args.putSerializable(IDENTIFIER, constraints);
		fragment.setArguments(args);
		return fragment;
	}

	public ArrayList<Constraint> getConstraints() {
		int size = constraints.size();
		constraints.clear();
		// Old references could not be valid anymore (e.g. Activity restart) so we update them
		for (int i = 0; i < size; i++) {
			ConstraintFragment f = ((ConstraintFragment) getFragmentManager().findFragmentById(i + 1));
			constraints.add(f.getConstraint());
		}
		return constraints;
	}
}
