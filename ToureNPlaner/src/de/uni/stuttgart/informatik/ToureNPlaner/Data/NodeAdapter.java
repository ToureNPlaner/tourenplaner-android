package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class NodeAdapter extends ArrayAdapter<Node>{
	  private  Vector<Node> items;
	  private Context context;
	public NodeAdapter(Context context, int textViewResourceId, Vector<Node> items) {
		super(context, textViewResourceId);
		 this.items = items;
		 this.context = context;
	}
	
	 @Override
     public View getView(int position, View convertView, ViewGroup parent) {
             View v = convertView;
             if (v == null) {
                
            	 LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 v = vi.inflate(R.layout.listitem, null);
             }
             Node node = items.get(position);
             if (node != null) {
                     TextView tt = (TextView) v.findViewById(R.id.toptext);
                     TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                     if (tt != null) {
                           tt.setText("Name: "+node.getName());                            }
                     if(bt != null){
                           bt.setText("Status: "+ node.getLatitude());
                     }
             }
             return v;
     }

	

}
