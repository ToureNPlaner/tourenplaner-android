package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.SessionData;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static de.uni.stuttgart.informatik.ToureNPlaner.UI.Util.showTextDialog;

public class ServerScreen extends Activity {
    ArrayList<String> servers;

    final String SERVERLIST_FILENAME = "serverlist";
    private ArrayAdapter adapter;
    private Spinner spinner;
    private Button btnconfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serverscreen);

        spinner = (Spinner) findViewById(R.id.spinner_server);
        btnconfirm = (Button) findViewById(R.id.btnconfirm);

        loadServerList();

        setupSpinner();
        setupButtons();
    }

    private void saveServerList() {
        try {
            FileOutputStream outputStream = openFileOutput(SERVERLIST_FILENAME, MODE_PRIVATE);
            try {
                ObjectOutputStream out = new ObjectOutputStream(outputStream);
                out.writeObject(servers);
            } finally {
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
	private void loadServerList() {
        try {
            FileInputStream inputStream = openFileInput(SERVERLIST_FILENAME);
            try {
                ObjectInputStream in = new ObjectInputStream(inputStream);
                servers = (ArrayList<String>) in.readObject();
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            servers = new ArrayList<String>();
        }
    }

    private void setupButtons() {
        setupConfirmButton();
        setupSetUrlButton();
        setupAddButton();
    }

    private void setupAddButton() {
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTextDialog(ServerScreen.this,"Choose", new de.uni.stuttgart.informatik.ToureNPlaner.UI.Util.Callback() {
                    @Override
                    public void result(String input) {
                        servers.add(input);
                        adapter.notifyDataSetChanged();
                        saveServerList();
                    }
                });
            }
        });
    }

    private void setupConfirmButton() {
        btnconfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://" + spinner.getSelectedItem();
                //TODO make cancelable
                final ProgressDialog dialog = ProgressDialog.show(ServerScreen.this, "Connecting", url, true);
                Session.connect(url, new Observer() {
                    @Override
                    public void onCompleted(Object object) {
                        dialog.dismiss();
                        ServerInfo info = (ServerInfo) object;
                        Intent myIntent = new Intent(getBaseContext(), LoginScreen.class);
                        startActivity(myIntent);
                    }

                    @Override
                    public void onError(Object object) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }

    private void setupSetUrlButton() {
        Button btnSetURL = (Button) findViewById(R.id.btnSetUrl);
        //  User can change the server URL
        btnSetURL.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ServerScreen.this);

                alert.setTitle("type your URL");
                alert.setMessage("URL");

                // Set an EditText view to get user input
                final EditText input = new EditText(ServerScreen.this);
                input.setText(SessionData.Instance.getServerURL());
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SessionData.Instance.setServerURL(input.getText().toString());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }

        });
    }

    private void setupSpinner() {
        // loads the spinnerArray into the spinnerdropdown
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, servers);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                SessionData.Instance.setChoosenAlgorithm(adapter.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }
}
