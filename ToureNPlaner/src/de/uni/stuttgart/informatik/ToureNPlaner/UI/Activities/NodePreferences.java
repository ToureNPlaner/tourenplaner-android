package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.UserInput;

public class NodePreferences extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nodepreferences);
		// generates the NodePreferences layout and fill content in the
		// Textviews
		try {
			Node node = NodeModel.getInstance()
					.get(UserInput.getSelectedNode());
			// -------------- get EditTexts --------------
			final EditText etName = (EditText) findViewById(R.id.etName);
//			final EditText etLongitude = (EditText) findViewById(R.id.etLongitude);
//			final EditText etLatitude = (EditText) findViewById(R.id.etLatitude);
			// -------------- get Buttons --------------
			Button btnDelete = (Button) findViewById(R.id.btnDelete);
			Button btnSave = (Button) findViewById(R.id.btnSave);
			Button btnReturn = (Button) findViewById(R.id.btnReturn);

			etName.setText(node.getName());
//			etLongitude.setText(String.valueOf(node.getLongitude()));
//			etLatitude.setText(String.valueOf(node.getLatitude()));
			btnSave.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					// -------------set Name------------------
					NodeModel.getInstance().get(UserInput.getSelectedNode())
							.setName(etName.getText().toString());

//					// ------------set Longitude--------------
//					NodeModel
//							.getInstance()
//							.get(UserInput.getSelectedNode())
//							.setLongitude(
//									Double.valueOf(etLongitude.getText()
//											.toString()));
//					// ------------set Latitude--------------
//					NodeModel
//							.getInstance()
//							.get(UserInput.getSelectedNode())
//							.setLatitude(
//									Double.valueOf(etLatitude.getText()
//											.toString()));
				
					finish();
				}
			});

			btnReturn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					finish();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
