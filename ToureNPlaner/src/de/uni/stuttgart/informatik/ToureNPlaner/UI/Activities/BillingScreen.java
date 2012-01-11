package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.BillingListAdapter;
public class BillingScreen extends ExpandableListActivity {
    ExpandableListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BillingListAdapter(this);
        setListAdapter(adapter);
        registerForContextMenu(getExpandableListView());
    }

}