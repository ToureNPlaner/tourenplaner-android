package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.UserInput;



public class AlgorithmScreen extends Activity {

	// generates all variables which are set in this activity
	private String spinnerArray[];
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.algorithmscreen);
	
	//TODO get amount of available algorithms  
	

	// TODO should be dynamic
	//  fills the SpinnerArray
	spinnerArray=new String[3];
	spinnerArray[0]="SP";
	spinnerArray[1]="TSP";
	spinnerArray[2]="CSP";
	
	// loads the spinnerArray into the spinnerdropdown
	Spinner spinner = (Spinner) findViewById(R.id.algorithm_spinner);
	@SuppressWarnings({ "rawtypes", "unchecked" })
	ArrayAdapter adapter = new ArrayAdapter(this,
	android.R.layout.simple_spinner_dropdown_item, spinnerArray);
	spinner.setAdapter(adapter);
	
	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

		
		@Override
		public void onItemSelected(AdapterView<?> adapter, View view, int pos,
				long id) {
			
			UserInput.setChoosenAlgorithm(adapter.getItemAtPosition(pos).toString());
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	});
	
	Button btnconfirm = (Button) findViewById(R.id.btnconfirm);
	btnconfirm.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View view) {
			// generates an intent from the class MapScreen
			Intent myIntent = new Intent(view.getContext(),
					MapScreen.class);
			startActivity(myIntent);
			
		}
		
	});
	
	
	}
	

}
