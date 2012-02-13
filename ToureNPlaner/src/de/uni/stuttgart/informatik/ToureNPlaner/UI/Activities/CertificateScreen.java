package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.ServerScreen.NewDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CertificateScreen extends FragmentActivity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.certificatescreen);

		//loadServerList();

		//setupListView();
		//setupAddButton();

	}
	
	private void setupAddButton() {
		Button btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				NewDialog.newInstance(getResources().getString(R.string.new_string), "")
						.show(getSupportFragmentManager(), NewDialog.IDENTIFIER);
			}
		});
	}
}
