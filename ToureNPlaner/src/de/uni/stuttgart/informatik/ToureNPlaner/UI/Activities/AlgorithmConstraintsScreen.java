package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.ConstraintListAdapter;

import java.io.Serializable;

public class AlgorithmConstraintsScreen extends ListActivity {

    private Session session;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Session.IDENTIFIER, session);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If we get created for the first time we get our data from the intent
        if (savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }

        setupListView();
    }

    private void setupListView() {
        ConstraintListAdapter adapter = new ConstraintListAdapter(session.getSelectedAlgorithm().getConstraints(), this);
            setListAdapter(adapter);
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent myIntent = new Intent(AlgorithmConstraintsScreen.this,
                                EditConstraintScreen.class);
                    myIntent.putExtra("constraint", (Serializable) adapterView.getItemAtPosition(i));
                    myIntent.putExtra(Session.IDENTIFIER,session);
                    startActivityForResult(myIntent,0);
                }
            });
        }
}

