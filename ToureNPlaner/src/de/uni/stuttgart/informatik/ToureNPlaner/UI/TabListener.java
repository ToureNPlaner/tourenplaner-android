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


package de.uni.stuttgart.informatik.ToureNPlaner.UI;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {
	private Fragment mFragment;
	private final FragmentActivity mActivity;
	private final String mTag;
	private final Class<T> mClass;

	/**
	 * Constructor used each time a new tab is created.
	 *
	 * @param activity The host Activity, used to instantiate the fragment
	 * @param tag      The identifier tag for the fragment
	 * @param clz      The fragment's Class, used to instantiate the fragment
	 */
	public TabListener(FragmentActivity activity, String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		mFragment = null;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
		// We need a new transaction because ¯\_(ツ)_/¯
		// see also https://groups.google.com/forum/#!topic/actionbarsherlock/ierFjBP-GuA
		FragmentTransaction fft = mActivity.getSupportFragmentManager().beginTransaction();
		// Check if the fragment is already initialized
		if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			fft.add(android.R.id.content, mFragment, mTag).commit();
		} else {
			// If it exists, simply attach it in order to show it
			fft.attach(mFragment).commit();
		}
	}

	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		if (mFragment != null) {
			// We need a new transaction because ¯\_(ツ)_/¯
			// see also https://groups.google.com/forum/#!topic/actionbarsherlock/ierFjBP-GuA
			// Detach the fragment, because another one is being attached
			mActivity.getSupportFragmentManager().beginTransaction().detach(mFragment).commit();
		}
	}

	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}
}
