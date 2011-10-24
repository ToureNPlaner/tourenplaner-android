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

