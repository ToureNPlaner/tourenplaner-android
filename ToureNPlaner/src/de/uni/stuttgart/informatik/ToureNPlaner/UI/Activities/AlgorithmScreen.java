package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.ServerInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class AlgorithmScreen extends Activity {

    private Session session;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Session.IDENTIFIER, session);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithmscreen);

        // If we get created for the first time we get our data from the intent
        if(savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }

        setupListView();
        setupBillingButton();
    }

    private void setupListView() {
        ListView listView = (ListView) findViewById(R.id.listViewAlgorithm);
        ArrayAdapter<AlgorithmInfo> adapter = new ArrayAdapter<AlgorithmInfo>(this,
                android.R.layout.simple_list_item_1, session.getServerInfo().getAlgorithms());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                session.setSelectedAlgorithm((AlgorithmInfo) adapterView.getItemAtPosition(i));
                Intent myIntent = new Intent(view.getContext(),
                            MapScreen.class);
                myIntent.putExtra(Session.IDENTIFIER,session);
                startActivity(myIntent);
            }
        });
    }

    private void setupBillingButton() {
        Button btnBilling = (Button) findViewById(R.id.btnBilling);
        if(session.getServerInfo().getServerType() == ServerInfo.ServerType.PUBLIC) {
            btnBilling.setVisibility(View.GONE);
        } else {
            btnBilling.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // generates an intent from the class BillingScreen
                    Intent myIntent = new Intent(view.getContext(),
                            BillingScreen.class);
                    startActivity(myIntent);
                }
            });
        }
    }

}
