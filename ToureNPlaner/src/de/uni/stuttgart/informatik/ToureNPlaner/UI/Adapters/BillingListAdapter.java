
package de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters;

import java.util.ArrayList;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class BillingListAdapter extends BaseExpandableListAdapter {
	private ArrayList<BillingItem> billingListItem = new ArrayList<BillingItem>();
    private String[] billingCaptions;
    private String[][] billingItem;
    private Context context;
    private ArrayList<BillingItem> billingItemlist;
    
    public BillingListAdapter(Context context,ArrayList<BillingItem> billingItemlist){
    	this.context = context;
    	this.billingItemlist = billingItemlist;
    	SetupList();
    	
    }
    
    private void SetupList(){
       	billingCaptions = new String[billingItemlist.size()];
    	for (int i = 0;i<billingItemlist.size();i++){
    		billingCaptions[i] = "Tour " + String.valueOf(billingItemlist.get(i).getRequestid());
    	}
    	billingItem = new String[billingCaptions.length][10];
    	for (int i = 0;i<billingCaptions.length;i++){	
    		billingItem[i][0] = "reqID: " +billingItemlist.get(i).getRequestid();
    		billingItem[i][1] = "userID: "+billingItemlist.get(i).getUserid();
    		billingItem[i][2] = "algorithmus: "+billingItemlist.get(i).getAlgorithm();
    		billingItem[i][3] = "request: "+billingItemlist.get(i).getRequest();
    		billingItem[i][4] = "response: "+billingItemlist.get(i).getResponse();
    		billingItem[i][5] = "cost: "+billingItemlist.get(i).getCost();
    		billingItem[i][6] = "requestDate: "+billingItemlist.get(i).getRequestdate();
    		billingItem[i][7] = "finishedDate: "+billingItemlist.get(i).getFinishdate();
    		billingItem[i][8] = "duration: "+billingItemlist.get(i).getDuration();
    		billingItem[i][9] = "status: "+billingItemlist.get(i).getStatus();
    	}   	
    }
    
    public Object getChild(int groupPosition, int childPosition) {
        return billingItem[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return billingItem[groupPosition].length;
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        textView.setTextSize(20);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
        View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
    }

    public Object getGroup(int groupPosition) {
        return billingCaptions[groupPosition];
    }

    public int getGroupCount() {
        return billingCaptions.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}

