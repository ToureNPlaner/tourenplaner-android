package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class LoginScreen extends Activity {

    private Session session;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Session.IDENTIFIER, session);
        super.onSaveInstanceState(outState);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        EditText emailTextfield = (EditText) findViewById(R.id.emailTextfield);
        emailTextfield.setText("root@toureNPlaner");
        EditText passwordTextfield = (EditText) findViewById(R.id.passwordTextfield);
        passwordTextfield.setText("toureNPlaner");

        // If we get created for the first time we get our data from the intent
        if (savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }

        setupLoginButton();
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
                session.setUser(emailTextfield.getText().toString());
                session.setPassword(passwordTextfield.getText().toString());

                // TODO DB check if user exist and have permissions
                Intent myIntent = new Intent(view.getContext(),
                        AlgorithmScreen.class);
                myIntent.putExtra(Session.IDENTIFIER, session);
                startActivity(myIntent);
            }
        });
    }
}
