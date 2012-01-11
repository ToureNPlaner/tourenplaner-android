
package de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters;

import java.util.ArrayList;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.*;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class BillingListAdapter extends BaseExpandableListAdapter {
	private ArrayList<Billing> billingList = new ArrayList<Billing>();
	Billing billingItem = new Billing(1, 0, "tsp", "no_request","no_response", 1337, "01.01.0001", "21.12.2012", 5021, "ok");
	Billing billingItem2 = new Billing(2, 0, "csp", "no_request","no_response", 1337, "01.01.0002", "21.12.2013", 5021, "ok");
    private String[] textCaptions;
    private String[][] textItem;
    private Context context;
    
    public BillingListAdapter(Context context){
    	SetupList();
    	this.context = context;
    }
    
    private void SetupList(){
    	// TODO:: create BillingList which contains all Billings
    	billingList.add(billingItem);
    	billingList.add(billingItem2);
    	textCaptions = new String[billingList.size()];
    	for (int i = 0;i<billingList.size();i++){
    		textCaptions[i] = "Tour " + String.valueOf(billingList.get(i).getRequestid());
    	}
    	textItem = new String[textCaptions.length][10];
    	for (int i = 0;i<textCaptions.length;i++){
    		textItem[i][0] = "reqID: " +String.valueOf(billingList.get(i).getRequestid());
    		textItem[i][1] = "userID: " + String.valueOf(billingList.get(i).getUserid());
    		textItem[i][2] = "algorithmus: "+ String.valueOf(billingList.get(i).getAlgorithm());
    		textItem[i][3] = "request: "+ String.valueOf(billingList.get(i).getRequest());
    		textItem[i][4] = "response: "+ String.valueOf(billingList.get(i).getResponse());
    		textItem[i][5] = "cost: "+ String.valueOf(billingList.get(i).getCost());
    		textItem[i][6] = "requestDate: "+ String.valueOf(billingList.get(i).getRequestDate());
    		textItem[i][7] = "finishedDate: "+ String.valueOf(billingList.get(i).getFinishedDate());
    		textItem[i][8] = "duration: "+ String.valueOf(billingList.get(i).getDuration());
    		textItem[i][9] = "status: "+ String.valueOf(billingList.get(i).getStatus());
    	}
    	
    }
    
    public Object getChild(int groupPosition, int childPosition) {
        return textItem[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return textItem[groupPosition].length;
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
        return textCaptions[groupPosition];
    }

    public int getGroupCount() {
        return textCaptions.length;
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

