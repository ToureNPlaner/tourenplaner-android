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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ResultNode;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.util.List;
import java.util.Map;

import static de.uni.stuttgart.informatik.ToureNPlaner.UI.Formatter.formatDistance;
import static de.uni.stuttgart.informatik.ToureNPlaner.UI.Formatter.formatTime;

public class ResultNodeAdapter extends ArrayAdapter<ResultNode> {
	public ResultNodeAdapter(Context context, List<ResultNode> objects) {
		super(context, R.layout.list_item, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView item = convertView == null ? (TextView) View.inflate(getContext(), R.layout.list_item, null) : (TextView) convertView;
		ResultNode node = getItem(position);

		String txt = "";

		if (node.getDistToPrev() != 0.0) {
			txt += formatDistance(getContext(), node.getDistToPrev()) + "\n";
		}
		if (node.getTimeToPrev() != 0.0) {
			txt += formatTime(getContext(), node.getTimeToPrev()) + "\n";
		}

		for (Map.Entry<String, String> e : node.getMisc().entrySet()) {
			txt += e.getKey() + ": " + e.getValue() + "\n";
		}

		// cut of last \n
		if (txt.length() > 0)
			txt = txt.substring(0, txt.length() - 1);

		item.setText(txt);

		return item;
	}
}
