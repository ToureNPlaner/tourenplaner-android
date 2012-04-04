package de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Enumeration;

public class KeystoreAdapter extends BaseAdapter {
	private final Context context;
	private final KeyStore keyStore;

	public KeystoreAdapter(Context context, KeyStore keyStore) {
		this.context = context;
		this.keyStore = keyStore;
	}

	@Override
	public int getCount() {
		try {
			return keyStore.size();
		} catch (KeyStoreException e) {
			return 0;
		}
	}

	@Override
	public Object getItem(int i) {
		try {
			Enumeration<String> alias = keyStore.aliases();
			while (i-- > 0) {
				alias.nextElement();
			}
			return alias.nextElement();
		} catch (KeyStoreException e) {
			return null;
		}
	}

	@Override
	public long getItemId(int i) {
		return (long) i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		TextView textView = view == null ? (TextView) View.inflate(context, R.layout.list_item, null) : (TextView) view;
		textView.setText(getItem(i).toString());
		return textView;
	}
}
