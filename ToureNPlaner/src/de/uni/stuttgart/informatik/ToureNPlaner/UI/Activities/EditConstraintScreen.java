package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class EditConstraintScreen extends Activity{
	private Session session;
    private Constraint constraint;
   	private Bundle data;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Session.IDENTIFIER,session);
        super.onSaveInstanceState(outState);
    }

    void finishActivity() {
        Intent data = new Intent();
        data.putExtra("constraint", constraint);
        data.putExtra("index", this.data.getInt("index", 0));
	    setResult(RESULT_OK, data);
        finish();
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editconstraints);
		 // If we get created for the first time we get our data from the intent
        if(savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }
       
	// generates the EditConstraints layout and fill content in the Textviews
		try {
            if (savedInstanceState != null) {
	            data = savedInstanceState;
                constraint = (Constraint) data.getSerializable("constraint");
            } else {
	            data = getIntent().getExtras();
	            constraint = (Constraint) data.getSerializable("constraint");
            }
           	// -------------- get EditTexts --------------
			final EditText etName = (EditText) findViewById(R.id.txtconstName);
			final EditText etType = (EditText) findViewById(R.id.txtconstType);
			final EditText etMin = (EditText) findViewById(R.id.txtconstMin);
			final EditText etMax = (EditText) findViewById(R.id.txtconstMax);
			
			// -------------- get Buttons --------------
			
			Button btnReturn = (Button) findViewById(R.id.btnconstReturn);

			etName.setText(String.valueOf(constraint.getName()));
			etType.setText(String.valueOf(constraint.getType()));
			etMin.setText(String.valueOf(constraint.getMinimumValue()));
			etMax.setText(String.valueOf(constraint.getMaximumValue()));
		
			// -----------------btnSave-----------------------
			btnReturn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					constraint.setMaximumValue(Double.valueOf(etMax.getEditableText().toString()));
					constraint.setMinimumValue(Double.valueOf(etMin.getEditableText().toString()));
					finishActivity();
				}
			});
			} catch (Exception e) {
			e.printStackTrace();
		}

	}
  }
