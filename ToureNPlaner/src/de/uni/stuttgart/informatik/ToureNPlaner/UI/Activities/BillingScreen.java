package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.BillingItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ResultNode;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.ConstraintType;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Edit;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.SetResultEdit;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.BillingListHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.BillingRequestHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.RawHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.RequestNN;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.BillingListAdapter;

public class BillingScreen extends ExpandableListActivity implements Observer, OnScrollListener {
	private BillingListAdapter adapter;
	private BillingListHandler handler;
	private Session session;
	public static Result resultstatic;
	BillingRequestHandler billingRequestHandler;

	private ArrayList<BillingItem> billinglist = new ArrayList<BillingItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		adapter = new BillingListAdapter(this, billinglist);
		setListAdapter(adapter);
		getExpandableListView().setOnScrollListener(this);
		registerForContextMenu(getExpandableListView());
		getExpandableListView().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
				ExpandableListView.ExpandableListContextMenuInfo info =
						(ExpandableListView.ExpandableListContextMenuInfo) contextMenuInfo;
				// 0 - Group
				// 1 - Child
				int type =	ExpandableListView.getPackedPositionType(info.packedPosition);
				if (type == 0){
				contextMenu.setHeaderTitle(String.valueOf(info.id));
				String[] menuItems = {getResources().getString(R.string.load_request)};
				for (int i = 0; i < menuItems.length; i++) {
					contextMenu.add(Menu.NONE, i, i, menuItems[i]);
				}
			}}
		}
		);
	}
	
@Override
public boolean onContextItemSelected(MenuItem item) {
	final ExpandableListView.ExpandableListContextMenuInfo info =
	(ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
	switch (item.getItemId()) {
		case 0: // showBilling
			Integer requestid = adapter.getRequestID((int) info.id);
			String algSuffix = adapter.getAlgSuffix((int) info.id);
			setProgressBarIndeterminateVisibility(true);
		//	handler = new BillingListHandler(this, session, requestid,algSuffix ,1);
			//handler.execute();
			billingRequestHandler =new BillingRequestHandler(billingRequestListener, session,1000,"ag",1 );
			billingRequestHandler.execute();
			break;

	}
	return true;
}

private final Observer billingRequestListener = new Observer() {
	@Override
	public void onCompleted(RawHandler caller, Object object) {
		setProgressBarIndeterminateVisibility(false);
		
		Result result = (Result )object;
		ArrayList<ResultNode> resultArray = new ArrayList<ResultNode>();
		resultArray = result.getPoints();
		ArrayList<Node> nodeArray = new ArrayList<Node>();
		String name ="";
		Integer id;
		// put all resultNodes in Node ArrayList
		for (int i = 0; i<resultArray.size();i++){
			id = resultArray.get(i).getId();
			name = String.valueOf(i);
			ArrayList<ConstraintType> cl = new ArrayList<ConstraintType>();
			Node node = new Node(id,name,resultArray.get(i).getGeoPoint(),cl);
			nodeArray.add(node);
		}
		NodeModel nm = new NodeModel();
		nm.setNodeVector(nodeArray);
		session.setNodeModel(nm);
		session.setResult(result);
		// search for the algorithmn suffix that was used by this request
		Integer PositionOfAlg=0;
		for(int i=0; i< session.getServerInfo().getAlgorithms().size();i++){
			if(session.getServerInfo().getAlgorithms().get(i).getUrlsuffix().equals(billingRequestHandler.getAlgSuffix())){
				PositionOfAlg = i;
			}
		}
		Edit edit = new SetResultEdit(session, result);
		edit.perform();
		session.setSelectedAlgorithm(session.getServerInfo().getAlgorithms().get(PositionOfAlg));
		Intent myIntent = new Intent(getApplicationContext(), MapScreen.class);
		myIntent.putExtra(Session.IDENTIFIER, session);
		startActivity(myIntent);
		Toast.makeText(getApplicationContext(),"request successful loaded", Toast.LENGTH_LONG).show();
		billingRequestHandler = null;
	}

	@Override
	public void onError(RawHandler caller, Object object) {
		billingRequestHandler = null;
		setProgressBarIndeterminateVisibility(false);
		Toast.makeText(getApplicationContext(), ((Exception) object).getLocalizedMessage(), Toast.LENGTH_LONG);
	}};
	@SuppressWarnings("unchecked")
	@Override
	public void onCompleted(RawHandler caller, Object object) {
		setProgressBarIndeterminateVisibility(false);
		adapter.addAll((ArrayList<BillingItem>) object);
		adapter.notifyDataSetChanged();
		handler = null;
	}
	@Override
	public void onError(RawHandler caller, Object object) {
		handler = null;
		setProgressBarIndeterminateVisibility(false);
		Toast.makeText(this, ((Exception) object).getLocalizedMessage(), Toast.LENGTH_LONG);
	}
	@Override
	public void onScroll(AbsListView arg0, int firstVisible, int visibleCount, int totalCount) {
		boolean loadMore =
				firstVisible + visibleCount >= totalCount - 10;

		if (loadMore && handler == null) {
			setProgressBarIndeterminateVisibility(true);
			handler = new BillingListHandler(this, session, 15, adapter.getGroupCount());
			handler.execute();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
}