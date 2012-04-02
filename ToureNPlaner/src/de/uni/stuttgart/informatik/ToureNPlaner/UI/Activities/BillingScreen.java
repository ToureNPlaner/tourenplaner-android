package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockExpandableListActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.ConstraintType;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.BillingListHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.BillingRequestHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.RawHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen.MapScreen;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.BillingListAdapter;

import java.util.ArrayList;

public class BillingScreen extends SherlockExpandableListActivity implements Observer, OnScrollListener {
	private BillingListAdapter adapter;
	private BillingListHandler billingListhandler;
	private Session session;
	private String algSuffix;
	BillingRequestHandler billingRequestHandler;

	private ArrayList<BillingItem> billinglist = new ArrayList<BillingItem>();

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}

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
				int type = ExpandableListView.getPackedPositionType(info.packedPosition);
				if (type == 0) {
					contextMenu.setHeaderTitle(adapter.getGroup((int) info.id).toString());
					String[] menuItems = {getResources().getString(R.string.load_request)};
					for (int i = 0; i < menuItems.length; i++) {
						contextMenu.add(Menu.NONE, i, i, menuItems[i]);
					}
				}
			}
		}
		);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final ExpandableListView.ExpandableListContextMenuInfo info =
				(ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case 0: // showBilling
				int requestid = adapter.getRequestID((int) info.id);
				algSuffix = adapter.getAlgSuffix((int) info.id);
				String status = adapter.getStatus((int) info.id);
				if (status.equals("ok")) {
					setSupportProgressBarIndeterminateVisibility(true);
					billingRequestHandler = new BillingRequestHandler(billingRequestListener, session, requestid);
					billingRequestHandler.execute();
				} else {
					Toast.makeText(getApplicationContext(), "failed to load request", Toast.LENGTH_SHORT).show();
				}
				break;

		}
		return true;
	}

	// ----------- BillingRequestHandler ----------------------
	private final Observer billingRequestListener = new Observer() {
		@Override
		public void onCompleted(RawHandler caller, Object object) {
			setSupportProgressBarIndeterminateVisibility(false);

			Result result = (Result) object;
			ArrayList<ResultNode> resultArray;
			ArrayList<AlgorithmInfo> algorithmList;
			resultArray = result.getPoints();
			ArrayList<Node> nodeArray = new ArrayList<Node>();
			String name = "";
			int id;


			// search for the algorithmn suffix that was used by this request
			int PositionOfAlg = 0;
			algorithmList = session.getServerInfo().getAlgorithms();
			for (int i = 0; i < algorithmList.size(); i++) {
				if (algorithmList.get(i).getUrlsuffix().equals(algSuffix)) {
					PositionOfAlg = i;
				}
			}
			session.setSelectedAlgorithm(algorithmList.get(PositionOfAlg));

			// put all resultNodes in Node ArrayList
			for (int i = 0; i < resultArray.size(); i++) {
				id = resultArray.get(i).getId();
				name = String.valueOf(i);
				ArrayList<ConstraintType> cl = new ArrayList<ConstraintType>();
				Node node = new Node(id, name, resultArray.get(i).getGeoPoint(), cl);
				nodeArray.add(node);
			}
			session.getNodeModel().setNodeVector(nodeArray);
			session.setResult(result);

			//  start Mapscreen
			Intent myIntent = new Intent(getApplicationContext(), MapScreen.class);
			myIntent.putExtra(Session.IDENTIFIER, session);
			startActivity(myIntent);

			billingRequestHandler = null;
		}

		@Override
		public void onError(RawHandler caller, Object object) {
			billingRequestHandler = null;
			setSupportProgressBarIndeterminateVisibility(false);
			Toast.makeText(getApplicationContext(), ((Exception) object).getLocalizedMessage(), Toast.LENGTH_LONG);
		}
	};

	//------ BillingListHandler ------
	@SuppressWarnings("unchecked")
	@Override
	public void onCompleted(RawHandler caller, Object object) {
		setSupportProgressBarIndeterminateVisibility(false);
		adapter.addAll((ArrayList<BillingItem>) object);
		adapter.notifyDataSetChanged();
		billingListhandler = null;
	}

	@Override
	public void onError(RawHandler caller, Object object) {
		billingListhandler = null;
		setSupportProgressBarIndeterminateVisibility(false);
		Toast.makeText(this, ((Exception) object).getLocalizedMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onScroll(AbsListView arg0, int firstVisible, int visibleCount, int totalCount) {
		boolean loadMore =
				firstVisible + visibleCount >= totalCount - 10;

		if (loadMore && billingListhandler == null) {
			setSupportProgressBarIndeterminateVisibility(true);
			billingListhandler = new BillingListHandler(this, session, 15, adapter.getGroupCount());
			billingListhandler.execute();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
}
