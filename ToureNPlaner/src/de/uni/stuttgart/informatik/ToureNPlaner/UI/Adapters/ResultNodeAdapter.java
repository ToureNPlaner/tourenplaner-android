package de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ResultNode;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.util.List;
import java.util.Map;

public class ResultNodeAdapter extends ArrayAdapter<ResultNode> {
	public ResultNodeAdapter(Context context, List<ResultNode> objects) {
		super(context, R.layout.list_item, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ResultNode node = getItem(position);

		TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);

		for (Map.Entry<String, String> e : node.getMisc().entrySet()) {
			item.append(e.getKey() + ": " + e.getValue() + "\n");
		}

		return item;
	}
}
