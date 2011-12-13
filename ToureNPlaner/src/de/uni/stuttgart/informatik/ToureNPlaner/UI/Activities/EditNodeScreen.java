package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.io.Serializable;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.ConstraintListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.NodeListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class EditNodeScreen extends Activity {

    private Node node;
    private Session session;
    public static final int RESULT_DELETE = RESULT_FIRST_USER;
	private Bundle data;
	private ConstraintListAdapter adapter;
	
	 
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Session.IDENTIFIER, session);
        super.onSaveInstanceState(outState);
    }

    void finishActivity() {
        Intent data = new Intent();
        data.putExtra("node", node);
	    data.putExtra("index", this.data.getInt("index", 0));
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nodepreferences);
		
		 // If we get created for the first time we get our data from the intent
        if(savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }
       
       
      
        
		// generates the NodePreferences layout and fill content in the Textviews
		try {
            if (savedInstanceState != null) {
	            data = savedInstanceState;
                node = (Node) data.getSerializable("node");
            } else {
	            data = getIntent().getExtras();
                node = (Node) data.getSerializable("node");
            }
            
            //  TODO: uncomment if when there are algorithms with constraints
            //  setup ConstraintListview
            
            //  if(session.getSelectedAlgorithm().getPointConstraints().size() > 0){
            setupListView();
            //  }
            
			// -------------- get EditTexts --------------
			final EditText etName = (EditText) findViewById(R.id.etName);
			// -------------- get Buttons --------------
			Button btnDelete = (Button) findViewById(R.id.btnDelete);
			Button btnSave = (Button) findViewById(R.id.btnSave);
			Button btnReturn = (Button) findViewById(R.id.btnReturn);
			etName.setText(node.getName());
			// -----------------btnSave-----------------------
			btnSave.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					node.setName(etName.getText().toString());
                    finishActivity();
				}
			});

			btnReturn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
                    setResult(RESULT_CANCELED, null);
                    finish();
				}
			});
			// -----------------btnDelete-----------------------
			btnDelete.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
                    setResult(RESULT_DELETE, new Intent().putExtra("index",data.getInt("index", 0)));
                    finish();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                node.getConstraintList().set(data.getExtras().getInt("index"), (Constraint) data.getSerializableExtra("constraint"));
                adapter.notifyDataSetChanged();
                break;
        }
    }
    private void setupListView() {
 
        ListView listView = (ListView) findViewById(R.id.listViewNodePreferences);
        TextView constraintLabel = (TextView) this.findViewById(R.id.lblNodePreferencesConstraintText);
        // set title visible
        constraintLabel.setVisibility(0);
        //  TODO replace "node.getConstraintList()" with
        // "session.getSelectedAlgorithm().getPointConstraints()" if an algorithm with constraints is 
        //  available
        adapter = new ConstraintListAdapter(node.getConstraintList(), this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(EditNodeScreen.this,
                            EditConstraintScreen.class);
                myIntent.putExtra("constraint", (Serializable) adapterView.getItemAtPosition(i));
                myIntent.putExtra("index",i);
                myIntent.putExtra(Session.IDENTIFIER,session);
                startActivityForResult(myIntent,0);
            }
        });
    }
}
