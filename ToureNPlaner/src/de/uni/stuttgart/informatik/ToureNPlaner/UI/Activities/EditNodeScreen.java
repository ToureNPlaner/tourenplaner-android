package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class EditNodeScreen extends Activity {

    private Node node;

    public static final int RESULT_DELETE = RESULT_FIRST_USER;

	private Bundle data;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("node",node);
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
		// generates the NodePreferences layout and fill content in the Textviews
		try {
            if (savedInstanceState != null) {
	            data = savedInstanceState;
                node = (Node) data.getSerializable("node");
            } else {
	            data = getIntent().getExtras();
                node = (Node) data.getSerializable("node");
            }

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
			// etLongitude.setText(String.valueOf(node.getLoE7()));
			// etLatitude.setText(String.valueOf(node.getLaE7()));

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
}
