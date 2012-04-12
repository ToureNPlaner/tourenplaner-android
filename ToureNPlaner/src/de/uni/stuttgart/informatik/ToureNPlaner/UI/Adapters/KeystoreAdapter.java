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
