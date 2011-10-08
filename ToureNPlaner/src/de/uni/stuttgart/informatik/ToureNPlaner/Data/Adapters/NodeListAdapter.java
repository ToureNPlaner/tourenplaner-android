package de.uni.stuttgart.informatik.ToureNPlaner.Data.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.util.ArrayList;

public class NodeListAdapter extends BaseAdapter{
	  private  ArrayList<Node> nodeVector;
	  private Context context;

    public NodeListAdapter(ArrayList<Node> nodeVector, Context context) {

		 this.nodeVector = nodeVector;
		 this.context = context;
    }
	
	 @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
		  LinearLayout itemLayout;
	        Node node = nodeVector.get(position);
	 
	        itemLayout= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.nodelistadapteritem, parent, false);
	 
	        TextView tvUser = (TextView) itemLayout.findViewById(R.id.toptext);
	        tvUser.setText(node.getName());
	 
	        TextView tvText = (TextView) itemLayout.findViewById(R.id.bottomtext);
	        tvText.setText(node.getGeoPoint().toString());
	        
	        ImageView ImageView = (ImageView) itemLayout.findViewById(R.id.icon);
	        Drawable icon = context.getResources().getDrawable(R.drawable.icon);
	     
	        
	        // sets the icon depending on the index
	        if (position == 0){
	        	icon =  context.getResources().getDrawable(R.drawable.startmarker);
	        }else if(position ==nodeVector.size()-1 ){
	        	icon =  context.getResources().getDrawable(R.drawable.endmarker);
	        }else{
	        	icon =  context.getResources().getDrawable(R.drawable.marker);
	        	        }
	        ImageView.setImageDrawable(icon);
	        return itemLayout;
   }

	@Override
	public int getCount() {
			return nodeVector.size();
	}

	@Override
	public Object getItem(int position) {
			return nodeVector.get(position);
	}

	@Override
	public long getItemId(int position) {
			return 0;
	}

	

}
