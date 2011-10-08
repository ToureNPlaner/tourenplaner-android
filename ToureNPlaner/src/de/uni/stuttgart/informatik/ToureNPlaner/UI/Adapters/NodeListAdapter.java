package de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.util.ArrayList;

public class NodeListAdapter extends ArrayAdapter<Node> {
    public NodeListAdapter(ArrayList<Node> nodeVector, Context context) {
        super(context, android.R.layout.simple_list_item_1, nodeVector);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout itemLayout;

        Node node = getItem(position);

        itemLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.nodelistadapteritem, parent, false);

        TextView tvUser = (TextView) itemLayout.findViewById(R.id.toptext);
        tvUser.setText(node.getName());

        TextView tvText = (TextView) itemLayout.findViewById(R.id.bottomtext);
        tvText.setText(node.getGeoPoint().toString());

        ImageView ImageView = (ImageView) itemLayout.findViewById(R.id.icon);
        Drawable icon;

        // sets the icon depending on the index
        if (position == 0) {
            icon = getContext().getResources().getDrawable(R.drawable.startmarker);
        } else if (position == getCount() - 1) {
            icon = getContext().getResources().getDrawable(R.drawable.endmarker);
        } else {
            icon = getContext().getResources().getDrawable(R.drawable.marker);
        }
        ImageView.setImageDrawable(icon);
        return itemLayout;
    }
}
