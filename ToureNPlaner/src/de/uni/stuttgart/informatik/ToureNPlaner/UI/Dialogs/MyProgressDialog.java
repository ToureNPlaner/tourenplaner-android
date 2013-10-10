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

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public abstract class MyProgressDialog extends DialogFragment {
	public static MyProgressDialog newInstance(MyProgressDialog dialog, String title, String message) {
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("message", message);
		dialog.setArguments(b);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final android.app.ProgressDialog dialog = new android.app.ProgressDialog(getActivity());
		dialog.setTitle(getArguments().getString("title"));
		dialog.setMessage(getArguments().getString("message"));
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}
}

