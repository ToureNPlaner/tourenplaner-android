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
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.NodeDrawable;

import java.util.ArrayList;

public class NodeListAdapter extends ArrayAdapter<Node> {
	private final boolean sourceIsTarget;

	public NodeListAdapter(Context context, ArrayList<Node> nodeList, boolean sourceIsTarget) {
		super(context, R.layout.list_item, nodeList);
		this.sourceIsTarget = sourceIsTarget;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Node node = getItem(position);

		View itemLayout = convertView == null ? (LinearLayout) View.inflate(getContext(), R.layout.nodelistadapteritem, null) : convertView;

		TextView tvUser = (TextView) itemLayout.findViewById(R.id.toptext);
		tvUser.setText(node.getName());

		TextView tvText = (TextView) itemLayout.findViewById(R.id.bottomtext);
		tvText.setText("lat:" + node.getGeoPoint().getLatitude() + " lon:" + node.getGeoPoint().getLongitude());

		ImageView ImageView = (ImageView) itemLayout.findViewById(R.id.nodelisticon);
		Drawable icon;

		// sets the icon depending on the index
		if (!sourceIsTarget && position == 0) {
			icon = new NodeDrawable(NodeDrawable.MarkerType.START);
		} else if (!sourceIsTarget && position == getCount() - 1) {
			icon = new NodeDrawable(NodeDrawable.MarkerType.END);
		} else if (sourceIsTarget) {
			icon = new NodeDrawable(NodeDrawable.MarkerType.START);
		} else {
			icon = new NodeDrawable(NodeDrawable.MarkerType.MIDDLE);
		}

		((NodeDrawable) icon).setLabel(node.getShortName());

		ImageView.setImageDrawable(icon);
		return itemLayout;
	}
}
