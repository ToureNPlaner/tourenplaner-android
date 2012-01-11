
package de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.*;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class BillingListAdapter extends BaseExpandableListAdapter {
	Billing bill = new Billing(1);
    private String[] textCaptions = {"Tour1","Tour2"};
    private String[][] textItem = new String[textCaptions.length][8];
    private Context context;
    
    public BillingListAdapter(Context context){
    	SetupList();
    	this.context = context;
    }
    private void SetupList(){
    	for (int i = 0;i<textCaptions.length;i++){
    		textItem[i][0] = "reqID: " + String.valueOf(bill.getId());
    		textItem[i][1] = "userID";
    		textItem[i][2] = "alg";
    		textItem[i][3] = "resp hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
    		textItem[i][4] = "finish";
    		textItem[i][5] = "cost";
    		textItem[i][6] = "reqdate";
    		textItem[i][7] = "finishdate";
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
                ViewGroup.LayoutParams.FILL_PARENT, 64);

        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        textView.setTextSize(18);
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

