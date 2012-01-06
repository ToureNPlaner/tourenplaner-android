package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditConstraintScreen extends Activity{
	private Session session;
    private Constraint constraint;
   	private Bundle data;
   	private String constraintType;
   	private int divisionFactor = 1;
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
			final EditText etValue = (EditText) findViewById(R.id.txtconstValue);
		
			
			constraintType = String.valueOf(constraint.getType());
			// -------------- get Buttons --------------
			
			Button btnReturn = (Button) findViewById(R.id.btnconstReturn);

			etName.setText(String.valueOf(constraint.getName()));
			etType.setText(String.valueOf(constraint.getType()));
			if(constraint.getValue() != null){
			etValue.setText(String.valueOf(constraint.getValue()));
			}
			
			//------------------get TextView ------------------
			TextView lblMin = (TextView) findViewById(R.id.lblconstMin);
			TextView lblMax = (TextView) findViewById(R.id.lblconstMax);
			lblMin.setText(String.valueOf(constraint.getMinimumValue()));
			lblMax.setText(String.valueOf(constraint.getMaximumValue()));
			
			//------------------get seekBar -------------------
			final SeekBar seekbar =  (SeekBar) findViewById(R.id.editconstraintseekBar);
			
    		if(constraintType.equals("float")||constraintType.equals("price")){
    			etValue.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
				divisionFactor = 100;
			}else{
				etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
				divisionFactor=1;
			}

			seekbar.setMax(constraint.getMaximumValue().intValue()*divisionFactor);
			if(constraint.getValue() != null){
	    	
	    		if(constraintType.equals("integer")||constraintType.equals("meter")){
					divisionFactor=1;
					// set seekbar progresslevel
					seekbar.setProgress(Integer.valueOf(constraint.getValue().toString()));
				}
	    		if(constraintType.equals("boolean")){
					divisionFactor=1;
					// set seekbar progresslevel
					int booleanValue = 0;
					if((Boolean)constraint.getValue() == true){
						booleanValue = 1;				
					}
					seekbar.setProgress(booleanValue);
				}
	    		if(constraintType.equals("float")||constraintType.equals("price")){
					divisionFactor = 100;
					// set seekbar progresslevel
					float tempFloat= Float.valueOf(constraint.getValue().toString());
					int tempInteger = (int) tempFloat;
					seekbar.setProgress(tempInteger*divisionFactor);
				}
			}
			seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar arg0, int arg1,
						boolean arg2) {
					if(constraintType.equals("float")||constraintType.equals("price")){
						etValue.setText(String.valueOf((float)arg1/divisionFactor));	
						}
				
				if(constraintType.equals("integer")||constraintType.equals("meter")){
					etValue.setText(String.valueOf(arg1/divisionFactor));	
					}
				
				if(constraintType.equals("boolean")){
				if(arg1 == 0){
					etValue.setText("false");
				}else{
					etValue.setText("true");
				}
			}
					
		}

				@Override
				public void onStartTrackingTouch(SeekBar arg0) {
				
				}

				@Override
				public void onStopTrackingTouch(SeekBar arg0) {
					
				}});
			
						
			// -----------------btnSave-----------------------
			btnReturn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
			if(constraintType.equals("float")||constraintType.equals("price")){
					constraint.setValue(Float.valueOf(etValue.getText().toString()));		
					}
			
			if(constraintType.equals("integer")||constraintType.equals("meter")){
				constraint.setValue(Integer.valueOf(etValue.getText().toString()));		
				}
			
			if(constraintType.equals("boolean")){
				if(seekbar.getProgress() == 1){
					constraint.setValue(true);	
				}else{
					constraint.setValue(false);
				}
						
				}
				
					finishActivity();
				}
			});
			} catch (Exception e) {
			e.printStackTrace();
		}

	}
  }
