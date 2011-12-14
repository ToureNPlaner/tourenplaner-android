package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.*;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Dialogs.MyProgressDialog;

public class LoginScreen extends FragmentActivity implements Observer {
	private AuthRequestHandler handler;
    private Session session;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Session.IDENTIFIER, session);
        super.onSaveInstanceState(outState);
    }

	public static class ConnectionProgressDialog extends MyProgressDialog {
        public static ConnectionProgressDialog newInstance(String title, String message) {
            return (ConnectionProgressDialog) MyProgressDialog.newInstance(new ConnectionProgressDialog(), title, message);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            ((LoginScreen) getActivity()).cancelConnection();
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        EditText emailTextfield = (EditText) findViewById(R.id.emailTextfield);
        emailTextfield.setText("root@toureNPlaner.de");
        EditText passwordTextfield = (EditText) findViewById(R.id.passwordTextfield);
        passwordTextfield.setText("toureNPlaner");

        // If we get created for the first time we get our data from the intent
        if (savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }

        setupLoginButton();

	    initializeHandler();
    }

    private void setupLoginButton() {
        Button btnlogin = (Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // gets Values of TextEditors
                EditText emailTextfield = (EditText) findViewById(R.id.emailTextfield);
                EditText passwordTextfield = (EditText) findViewById(R.id.passwordTextfield);

                // set userdata
                session.setUsername(emailTextfield.getText().toString());
                session.setPassword(passwordTextfield.getText().toString());

	            ConnectionProgressDialog.newInstance("Login", session.getUsername()).show(getSupportFragmentManager(), "login");
                handler = new AuthRequestHandler(LoginScreen.this, session);
	            handler.execute();
            }
        });
    }

	private void initializeHandler() {
        handler = (AuthRequestHandler) getLastCustomNonConfigurationInstance();

        if (handler != null)
            handler.setListener(this);
        else {
            MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("login");
            if (dialog != null)
                dialog.dismiss();
        }
    }

	private void cancelConnection() {
        handler.cancel(true);
        handler = null;
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(handler != null)
			handler.setListener(null);
	}

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return handler;
    }

	@Override
	public void onCompleted(ConnectionHandler caller, Object object) {
		handler = null;
        MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("login");
        dialog.dismiss();
		Intent myIntent = new Intent(getBaseContext(), AlgorithmScreen.class);
        myIntent.putExtra(Session.IDENTIFIER, session);
        startActivity(myIntent);
	}

	@Override
	public void onError(ConnectionHandler caller, Object object) {
		handler = null;
	    MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("login");
	    dialog.dismiss();
	    Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
	}
}
