/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockExpandableListActivity;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.*;
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
	private final OnClickListener buttonClicklistener = new OnClickListener() {
		@Override
		public void onClick(View view) {

			int id = (Integer) view.getTag();
			loadRequest(id);
		}
	};
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
		adapter = new BillingListAdapter(this, billinglist, buttonClicklistener);
		setListAdapter(adapter);
		getExpandableListView().setOnScrollListener(this);
		getExpandableListView().setGroupIndicator(getResources().getDrawable(R.drawable.expandable_icon));
	}

	private void loadRequest(int id) {
		int requestid = adapter.getRequestID(id);
		algSuffix = adapter.getAlgSuffix(id);
		String status = adapter.getStatus(id);
		if (status.equals("ok")) {
			setSupportProgressBarIndeterminateVisibility(true);
			billingRequestHandler = new BillingRequestHandler(billingRequestListener, session, requestid);
			billingRequestHandler.execute();
		} else {
			Toast.makeText(getApplicationContext(), "failed to load request", Toast.LENGTH_SHORT).show();
		}

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
				ResultNode n = resultArray.get(i);
				Node node = new Node(n.getId(), n.getName(), n.getShortName(), resultArray.get(i).getGeoPoint(), session.getSelectedAlgorithm().getPointConstraintTypes());
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

	@SuppressWarnings("unchecked")
	@Override
	public void onScroll(AbsListView arg0, int firstVisible, int visibleCount, int totalCount) {
		boolean loadMore =
				firstVisible + visibleCount >= totalCount - 20;

		if (loadMore && billingListhandler == null) {
			setSupportProgressBarIndeterminateVisibility(true);
			billingListhandler = new BillingListHandler(this, session, 20, adapter.getGroupCount());
			billingListhandler.execute();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
}
