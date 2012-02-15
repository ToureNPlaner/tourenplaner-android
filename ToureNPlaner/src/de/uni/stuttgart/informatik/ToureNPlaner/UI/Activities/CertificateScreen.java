package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
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

public class CertificateScreen extends FragmentActivity {
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
		setContentView(R.layout.certificatescreen);

		loadKeystore();

		setupListView();
		setupAddButton();
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
		try {
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			return;
		}
		try {
			keyStore.load(openFileInput("keystore"), null);
			return;
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}

		try {
			keyStore.load(new KeyStore.LoadStoreParameter() {
				@Override
				public KeyStore.ProtectionParameter getProtectionParameter() {
					return new KeyStore.PasswordProtection(null);
				}
			});
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void setupListView() {
		ListView listView = (ListView) findViewById(R.id.certificateListView);
		adapter = new KeystoreAdapter(getApplicationContext(), keyStore);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			addCert(data.getData());
		}
	}

	private void setupAddButton() {
		Button btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent("org.openintents.action.PICK_FILE");
				intent.setData(Uri.parse("file://" + Environment.getExternalStorageDirectory()));
				intent.putExtra("org.openintents.extra.TITLE", "Please select a file");
				try {
					startActivityForResult(intent, 1);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}

