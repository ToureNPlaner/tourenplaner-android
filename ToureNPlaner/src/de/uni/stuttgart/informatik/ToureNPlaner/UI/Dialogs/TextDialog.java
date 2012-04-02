package de.uni.stuttgart.informatik.ToureNPlaner.UI.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockDialogFragment;

public abstract class TextDialog extends SherlockDialogFragment {

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
