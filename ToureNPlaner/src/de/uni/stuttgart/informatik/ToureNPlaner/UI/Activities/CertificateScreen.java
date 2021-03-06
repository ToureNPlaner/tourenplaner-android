/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.KeystoreAdapter;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.List;

public class CertificateScreen extends ListActivity {
	private KeyStore keyStore;
	private KeystoreAdapter adapter;

	private void addCert(Uri uri) {
		try {
			InputStream fileInputStream = getApplicationContext().getContentResolver().openInputStream(uri);
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			Collection<? extends Certificate> certs = factory.generateCertificates(fileInputStream);
			List<String> segments = uri.getPathSegments();
			int num = 0;
			for (Certificate cert : certs) {
				keyStore.setCertificateEntry(segments.get(segments.size() - 1) + num, cert);
				num++;
			}
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
		adapter.notifyDataSetChanged();
		storeKeystore();
		ToureNPlanerApplication.setupSsl();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadKeystore();

		setupListView();
	}

	private void storeKeystore() {
		try {
			FileOutputStream outputStream = openFileOutput("keystore", MODE_PRIVATE);
			try {
				keyStore.store(outputStream, null);
			} finally {
				outputStream.close();
			}
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void loadKeystore() {
		keyStore = ToureNPlanerApplication.getKeyStore();
	}

	private void setupListView() {
		adapter = new KeystoreAdapter(getApplicationContext(), keyStore);
		setListAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			addCert(data.getData());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.certificatescreenmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add_certificate:
				Intent intent = new Intent("org.openintents.action.PICK_FILE");
				intent.setData(Uri.parse("file://" + Environment.getExternalStorageDirectory()));
				intent.putExtra("org.openintents.extra.TITLE", "Please select a file");
				try {
					startActivityForResult(intent, 1);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

