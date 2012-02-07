package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;
import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.BillingItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.BillingListAdapter;
public class BillingScreen extends ExpandableListActivity {
    private ExpandableListAdapter adapter;
    private Session session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
			session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
		} else {
			session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
		}
        ArrayList<BillingItem> bl = session.getBillingList();
        adapter = new BillingListAdapter(this,bl);
        setListAdapter(adapter);
        registerForContextMenu(getExpandableListView());
    }

}