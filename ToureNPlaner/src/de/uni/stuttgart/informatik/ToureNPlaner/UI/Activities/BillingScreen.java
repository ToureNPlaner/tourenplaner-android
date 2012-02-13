package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.BillingItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.BillingListHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.RawHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.BillingListAdapter;

import java.util.ArrayList;

public class BillingScreen extends ExpandableListActivity implements Observer, OnScrollListener {
	private BillingListAdapter adapter;
	private BillingListHandler handler;
	private Session session;
	private SharedPreferences billingscreen_preferences;

	private ArrayList<BillingItem> billinglist = new ArrayList<BillingItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
		} else {
			session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
		}
		billingscreen_preferences = PreferenceManager.getDefaultSharedPreferences(this);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		adapter = new BillingListAdapter(this, billinglist);
		setListAdapter(adapter);
		getExpandableListView().setOnScrollListener(this);
		registerForContextMenu(getExpandableListView());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCompleted(RawHandler caller, Object object) {
		handler = null;
		setProgressBarIndeterminateVisibility(false);
		ArrayList<BillingItem> list = (ArrayList<BillingItem>) object;
		billinglist.addAll(list);
		adapter.SetupList();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		//TODO::refresh new values from preferencescreen

	}

	@Override
	public void onError(RawHandler caller, Object object) {
		handler = null;
		setProgressBarIndeterminateVisibility(false);
		Toast.makeText(this, ((Exception) object).getLocalizedMessage(), Toast.LENGTH_LONG);
		//TODO::better error message
	}

	//------------menu---------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.billingscreenmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.billinglistpreferences:
				startActivity(new Intent(this, BillingScreenPreferences.class));
				return true;

		}
		return false;
	}

	@Override
	public void onScroll(AbsListView arg0, int firstVisible, int visibleCount, int totalCount) {
		boolean loadMore =
				firstVisible + visibleCount >= totalCount - 10;

		if (loadMore && handler == null) {
			setProgressBarIndeterminateVisibility(true);
			handler = new BillingListHandler(this, session, 15, billinglist.size());
			handler.execute();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}