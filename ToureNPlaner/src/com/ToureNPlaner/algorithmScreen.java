package com.ToureNPlaner;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.ToureNPlaner.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class algorithmScreen extends Activity {
	/** Called when the activity is first created. */

	;

	public void onCreate(Bundle savedInstanceState) {

		final SharedPreferences applicationPreferences = getSharedPreferences(
				loginScreen.prefName, MODE_PRIVATE);
		final SharedPreferences.Editor prefEditor = applicationPreferences
				.edit();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.algorithm);
		final Button btnTSP = (Button) findViewById(R.id.btnTSP);
		final Button btnCSP = (Button) findViewById(R.id.btnCSP);
		final Button btnBilling = (Button) findViewById(R.id.btnBilling);
		
		btnBilling.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try
				{
					HttpClient hc = new DefaultHttpClient();
					HttpGet get = new HttpGet("http://www.google.de");
			
					HttpResponse rp = hc.execute(get);

					if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
					{
						String str = EntityUtils.toString(rp.getEntity());
						Toast.makeText(
								getBaseContext(),
								str.substring(0, 250),
								Toast.LENGTH_SHORT).show();
					}
				
				}catch(IOException e){

				}catch(Exception e) {
					Toast.makeText(
							getBaseContext(),
							e.toString(),
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		

		btnCSP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loginScreen.choosenAlgorithm = btnCSP.getText().toString();
				prefEditor.putString("choosenAlgorithm",
						loginScreen.choosenAlgorithm);
				prefEditor.commit();

				Toast.makeText(
						getBaseContext(),
						"you have choosen the "
								+ applicationPreferences.getString(
										"choosenAlgorithm", "a non existing")
								+ " algorithm", Toast.LENGTH_SHORT).show();

			}
		});
		btnTSP.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				File f = new File("testfile.txt");
				
				Intent myIntent = new Intent(view.getContext(), mapView.class);
				startActivity(myIntent);
				
				loginScreen.choosenAlgorithm = btnTSP.getText().toString();
				prefEditor.putString("choosenAlgorithm",
						loginScreen.choosenAlgorithm);
				prefEditor.commit();
				Toast.makeText(
						getBaseContext(),
						"you have choosen the "
								+ applicationPreferences.getString(
										"choosenAlgorithm",
										"a non existing") + " algorithm"+ f.getAbsolutePath(),
						Toast.LENGTH_SHORT).show();
				onPause();
			}

		});
		
	}
}
