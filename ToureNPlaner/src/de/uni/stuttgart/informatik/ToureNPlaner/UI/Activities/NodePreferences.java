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
			final EditText etLongitude = (EditText) findViewById(R.id.etLongitude);

			// -------------- get Buttons --------------
			Button btnLongitudeEdit = (Button) findViewById(R.id.btnLongitude);
			Button btnNameEdit = (Button) findViewById(R.id.btnName);
			Button btnReturn = (Button) findViewById(R.id.btnReturn);

			etName.setText(node.getName());
			etLongitude.setText(String.valueOf(node.getLongitude()));

			btnLongitudeEdit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					NodeModel
							.getInstance()
							.get(UserInput.getSelectedNode())
							.setLongitude(
									Double.valueOf(etLongitude.getText()
											.toString()));
				}
			});
			btnNameEdit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					NodeModel.getInstance().get(UserInput.getSelectedNode())
							.setName(etName.getText().toString());
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
