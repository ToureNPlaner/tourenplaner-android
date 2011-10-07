package de.uni.stuttgart.informatik.ToureNPlaner.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class Util {
    public interface Callback {
        void result(String input);
    }

    public static void showTextDialog(final Context context, String message, final Callback callback, String content) {
        final EditText input = new EditText(context);
        input.setText(content, TextView.BufferType.SPANNABLE);
        new AlertDialog.Builder(context)
                .setTitle(message)
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callback.result(input.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    dialogInterface.dismiss();
                    callback.result(input.getText().toString());
                    return true;
                }
                return false;
            }
        }).show();
    }
}
