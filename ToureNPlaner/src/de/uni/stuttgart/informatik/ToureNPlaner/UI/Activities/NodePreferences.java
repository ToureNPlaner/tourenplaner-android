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
import de.uni.stuttgart.informatik.ToureNPlaner.Data.SessionData;

public class NodePreferences extends Activity {

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        SessionData.Instance.save(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SessionData.Instance.load(savedInstanceState);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nodepreferences);
		// generates the NodePreferences layout and fill content in the
		// Textviews
		try {
			Node node = NodeModel.getInstance()
					.get(SessionData.Instance.getSelectedNode());
			// -------------- get EditTexts --------------
			final EditText etName = (EditText) findViewById(R.id.etName);
			// final EditText etLongitude = (EditText)
			// findViewById(R.id.etLongitude);
			// final EditText etLatitude = (EditText)
			// findViewById(R.id.etLatitude);
			// -------------- get Buttons --------------
			Button btnDelete = (Button) findViewById(R.id.btnDelete);
			Button btnSave = (Button) findViewById(R.id.btnSave);
			Button btnReturn = (Button) findViewById(R.id.btnReturn);

			etName.setText(node.getName());
			// etLongitude.setText(String.valueOf(node.getLongitude()));
			// etLatitude.setText(String.valueOf(node.getLatitude()));

			// -----------------btnSave-----------------------
			btnSave.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					// -------------set Name------------------
					NodeModel.getInstance().get(SessionData.Instance.getSelectedNode())
							.setName(etName.getText().toString());
					// notifies the Nodelistscreen for all changes
					NodelistScreen.getAdapter().notifyDataSetChanged();
					finish();
				}
			});

			btnReturn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
			// -----------------btnDelete-----------------------
			btnDelete.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					NodeModel.getInstance().remove(SessionData.Instance.getSelectedNode());
					// notifies the Nodelistscreen for all changes
					NodelistScreen.getAdapter().notifyDataSetChanged();
					finish();
				};
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
