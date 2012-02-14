package de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.BillingItem;

import java.util.ArrayList;

public class BillingListAdapter extends BaseExpandableListAdapter {
	private ArrayList<String> billingCaptions = new ArrayList<String>();
	private ArrayList<String[]> billingItems = new ArrayList<String[]>();
	private Context context;

	public BillingListAdapter(Context context, ArrayList<BillingItem> billinglist) {
		this.context = context;
		addAll(billinglist);
	}

	public void addAll(ArrayList<BillingItem> items) {
		setupList(items);
	}

	private void setupList(ArrayList<BillingItem> items) {
		billingCaptions.ensureCapacity(billingCaptions.size() + items.size());
		for (int i = 0; i < items.size(); i++) {
			billingCaptions.add("Tour " + String.valueOf(items.get(i).getRequestid()));
		}

		billingItems.ensureCapacity(billingItems.size() + items.size());

		// TODO localize
		for (int i = 0; i < items.size(); i++) {
			String[] arr = new String[10];
			arr[0] = "reqID: " + items.get(i).getRequestid();
			arr[1] = "userID: " + items.get(i).getUserid();
			arr[2] = "algorithmus: " + items.get(i).getAlgorithm();
			arr[3] = "request: " + items.get(i).getRequest();
			arr[4] = "response: " + items.get(i).getResponse();
			arr[5] = "cost: " + items.get(i).getCost();
			arr[6] = "requestDate: " + items.get(i).getRequestdate();
			arr[7] = "finishedDate: " + items.get(i).getFinishdate();
			arr[8] = "duration: " + items.get(i).getDuration();
			arr[9] = "status: " + items.get(i).getStatus();
			billingItems.add(arr);
		}
	}

	public Object getChild(int groupPosition, int childPosition) {
		return billingItems.get(groupPosition)[childPosition];
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return billingItems.get(groupPosition).length;
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
		return billingCaptions.get(groupPosition);
	}

	public int getGroupCount() {
		return billingCaptions.size();
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

