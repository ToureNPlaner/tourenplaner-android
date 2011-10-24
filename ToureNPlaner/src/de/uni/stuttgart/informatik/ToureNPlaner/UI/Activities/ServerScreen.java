package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Dialogs.MyProgressDialog;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static de.uni.stuttgart.informatik.ToureNPlaner.UI.Util.showTextDialog;

public class ServerScreen extends FragmentActivity implements Observer {
    static final String SERVERLIST_FILENAME = "serverlist";
    private ArrayAdapter adapter;
    private Session.ConnectionHandler handler;
    private ArrayList<String> servers;

    private static class ConnectionProgressDialog extends MyProgressDialog {
        public static ConnectionProgressDialog newInstance(String title, String message) {
            return (ConnectionProgressDialog) MyProgressDialog.newInstance(new ConnectionProgressDialog(), title, message);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            ((ServerScreen)getActivity()).cancelConnection();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serverscreen);

        loadServerList();

        setupListView();
        setupButtons();

        initializeHandler();
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
        setupAddButton();
    }

    private void initializeHandler() {
            handler = (Session.ConnectionHandler) getLastCustomNonConfigurationInstance();

            if(handler != null)
                handler.setListener(this);
            else {
                MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("connecting");
                if(dialog != null)
                    dialog.dismiss();
            }
        }

    private void setupAddButton() {
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTextDialog(ServerScreen.this, "Choose", new de.uni.stuttgart.informatik.ToureNPlaner.UI.Util.Callback() {
                    @Override
                    public void result(String input) {
                        servers.add(input);
                        adapter.notifyDataSetChanged();
                        saveServerList();
                    }
                }, "");
            }
        });
    }

    private void setupListView() {
        ListView listView = (ListView) findViewById(R.id.serverListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, servers);
        listView.setAdapter(adapter);

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                if (view.getId() == R.id.serverListView) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                    contextMenu.setHeaderTitle(servers.get(info.position));
                    String[] menuItems = {"edit", "delete"};
                    for (int i = 0; i < menuItems.length; i++) {
                        contextMenu.add(Menu.NONE, i, i, menuItems[i]);
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = "http://" + adapterView.getItemAtPosition(i);
                serverSelected(url);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0: // edit
                showTextDialog(ServerScreen.this, "Choose", new de.uni.stuttgart.informatik.ToureNPlaner.UI.Util.Callback() {
                    @Override
                    public void result(String input) {
                        servers.set(info.position, input);
                        adapter.notifyDataSetChanged();
                        saveServerList();
                    }
                }, servers.get(info.position));
                break;
            case 1: // delete
                servers.remove(info.position);
                adapter.notifyDataSetChanged();
                saveServerList();
                break;
        }
        return true;
    }

    private void cancelConnection() {
            handler.cancel(true);
            handler = null;
        }

    @Override
    public void onCompleted(Object object) {
        handler = null;
        MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("connecting");
        dialog.dismiss();
        Session session = (Session) object;
        Intent myIntent;
        if (session.getServerInfo().getServerType() == ServerInfo.ServerType.PUBLIC) {
            myIntent = new Intent(getBaseContext(), AlgorithmScreen.class);
        } else {
            myIntent = new Intent(getBaseContext(), LoginScreen.class);
        }
        myIntent.putExtra(Session.IDENTIFIER, session);
        startActivity(myIntent);
    }

    @Override
    public void onError(Object object) {
        handler = null;
        MyProgressDialog dialog = (MyProgressDialog) getSupportFragmentManager().findFragmentByTag("connecting");
        dialog.dismiss();
        Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return handler;
    }

    private void serverSelected(String url) {
        ConnectionProgressDialog.newInstance("Connecting",url).show(getSupportFragmentManager(), "connecting");
        handler = Session.connect(url, this);
    }
}
