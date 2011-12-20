package de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraint;

public class ConstraintListAdapter extends ArrayAdapter<Constraint> {
	private ArrayList<Constraint> constraintList;
    public ConstraintListAdapter(ArrayList<Constraint> constraintList, Context context) {
        super(context, android.R.layout.simple_list_item_1, constraintList);
        this.constraintList = constraintList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout itemLayout;

        Constraint constraint = getItem(position);

        itemLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.constraintlistadapteritem, parent, false);

        TextView tvname = (TextView) itemLayout.findViewById(R.id.constName);
        tvname.setText(constraint.getName());
        
        

        TextView tvValue = (TextView) itemLayout.findViewById(R.id.constValue);
        if(constraint.getValue() != null){
        tvValue.setText( constraint.getValue().toString());
        }else{
        	 tvValue.setText(constraint.getMinimumValue().toString());
        }
        
        TextView tvtype = (TextView) itemLayout.findViewById(R.id.constType);
        tvtype.setText(constraint.getType());
        
        
        return itemLayout;
    }
    
   
    public int getCount() {
        return constraintList.size();
    }

 
    public Constraint getItem(int position) {
        return constraintList.get(position);
    }

    
    public long getItemId(int position) {
        return position;
    }
    
  

	
}
