package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BillingScreen extends Activity {
	private ListView ListviewBilling;
	private String listviewArray[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.billingscreen);
		// TODO slistviewArray should be generated dynamic
		listviewArray = new String[3];
		listviewArray[0] = "Tour1";
		listviewArray[1] = "Tour2";
		listviewArray[2] = "Tour3";

		ListviewBilling = (ListView) findViewById(R.id.listviewBilling);
		// By using setAdpater method in listview we an add string array in
		// list.
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, listviewArray);
		ListviewBilling.setAdapter(adapter);

		ListviewBilling.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long arg3) {
				// TODO logic of billinglist click

			}
		});
	}

}
