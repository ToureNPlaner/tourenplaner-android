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

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public abstract class TextDialog extends DialogFragment {

	private EditText input;

	public abstract void doPositiveClick();

	public abstract void doNegativeClick();

	public static TextDialog newInstance(TextDialog dialog, String title, String content) {
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putCharSequence("content", content);
		dialog.setArguments(b);
		return dialog;
	}

	public EditText getInputField() {
		return input;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putCharSequence("content", input.getText());
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		input = new EditText(getActivity());
		String message = getArguments().getString("title");
		CharSequence content;
		if (savedInstanceState == null) {
			content = getArguments().getCharSequence("content");
		} else {
			content = savedInstanceState.getCharSequence("content");
		}
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
		input.setText(content, TextView.BufferType.SPANNABLE);
		return new AlertDialog.Builder(getActivity())
				.setTitle(message)
				.setView(input)
				.setPositiveButton(getResources().getText(android.R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						doPositiveClick();
					}
				}).setNegativeButton(getResources().getText(android.R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						doNegativeClick();
					}
				}).setOnKeyListener(new DialogInterface.OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
						if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
								(i == KeyEvent.KEYCODE_ENTER)) {
							dialogInterface.dismiss();
							doPositiveClick();
							return true;
						}
						return false;
					}
				}).create();
	}
}
