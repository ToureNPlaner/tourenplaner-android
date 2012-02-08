package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;
import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.BillingItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.BillingListHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.RawHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.BillingListAdapter;
public class BillingScreen extends ExpandableListActivity implements Observer{
    private ExpandableListAdapter adapter;
    private BillingListHandler handler;
    private Session session;
    public static transient ArrayList<BillingItem> billinglist = new ArrayList<BillingItem>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }
    	handler = new BillingListHandler(BillingScreen.this, session);
		handler.execute();
		
    }
	@Override
	public void onCompleted(RawHandler caller, Object object) {
		handler = null;
        adapter = new BillingListAdapter(this,billinglist);
        setListAdapter(adapter);
        registerForContextMenu(getExpandableListView());
        billinglist.clear();
	
	}

	@Override
	public void onError(RawHandler caller, Object object) {
		handler = null;
}

}