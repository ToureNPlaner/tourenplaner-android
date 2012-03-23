package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.NodeListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.DragDrop.DragNDropListView;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Listener.DragListener;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Listener.DropListener;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Listener.RemoveListener;

import java.io.Serializable;

public class NodelistScreen extends SherlockListActivity implements Session.Listener {
	private NodeListAdapter adapter;
	private Session session;
	private boolean dirty;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		outState.putBoolean("dirty", dirty);
		super.onSaveInstanceState(outState);
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);

		if (savedInstanceState != null) {
			dirty = savedInstanceState.getBoolean("dirty");
		}

		session.registerListener(NodelistScreen.class, this);

		setContentView(R.layout.dragndroplistview);

		adapter = new NodeListAdapter(session.getNodeModel().getNodeVector(), this, session.getSelectedAlgorithm().sourceIsTarget());
		setListAdapter(adapter);
		ListView listView = getListView();
		registerForContextMenu(listView);


		if (listView instanceof DragNDropListView) {
			((DragNDropListView) listView).setDropListener(mDropListener);
			((DragNDropListView) listView).setRemoveListener(mRemoveListener);
			((DragNDropListView) listView).setDragListener(mDragListener);

			//---------ContextMenu-----------------
			listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
				@Override
				public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
					AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
					contextMenu.setHeaderTitle(adapter.getItem(info.position).getName());
					String[] menuItems = {getResources().getString(R.string.edit), getResources().getString(R.string.delete)};
					for (int i = 0; i < menuItems.length; i++) {
						contextMenu.add(Menu.NONE, i, i, menuItems[i]);
					}
				}
			}
			);

			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
				                        final int pos, long arg3) {

					Intent myIntent = new Intent(NodelistScreen.this,
							EditNodeScreen.class);
					myIntent.putExtra(Session.IDENTIFIER, session);
					myIntent.putExtra("node", (Serializable) adapter.getItemAtPosition(pos));
					startActivityForResult(myIntent, pos);

				}
			});
		}
	}

	private DropListener mDropListener =
			new DropListener() {
				public void onDrop(int from, int to) {
					ListAdapter adapter = getListAdapter();
					if (adapter instanceof NodeListAdapter) {
						((NodeListAdapter) adapter).onDrop(from, to);
						dirty = true;
						getListView().invalidateViews();
					}
				}
			};

	private RemoveListener mRemoveListener =
			new RemoveListener() {
				public void onRemove(int which) {
					ListAdapter adapter = getListAdapter();
					if (adapter instanceof NodeListAdapter) {
						((NodeListAdapter) adapter).onRemove(which);
						getListView().invalidateViews();
					}
				}
			};

	private DragListener mDragListener =
			new DragListener() {

				int backgroundColor = 0xe0103010;
				int defaultBackgroundColor;

				public void onDrag(int x, int y, ListView listView) {
				}

				public void onStartDrag(View itemView) {
					itemView.setVisibility(View.INVISIBLE);
					defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
					itemView.setBackgroundColor(backgroundColor);
					ImageView iv = (ImageView) itemView.findViewById(R.id.nodelisticon);
					if (iv != null) iv.setVisibility(View.INVISIBLE);
				}

				public void onStopDrag(View itemView) {
					itemView.setVisibility(View.VISIBLE);
					itemView.setBackgroundColor(defaultBackgroundColor);
					ImageView iv = (ImageView) itemView.findViewById(R.id.nodelisticon);
					if (iv != null) iv.setVisibility(View.VISIBLE);
				}

			};

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case 0: // edit
				Intent myIntent = new Intent(NodelistScreen.this,
						EditNodeScreen.class);
				myIntent.putExtra("node", adapter.getItem(info.position));
				myIntent.putExtra(Session.IDENTIFIER, session);
				myIntent.putExtra("index", info.position);
				startActivityForResult(myIntent, 0);
				break;
			case 1: // delete
				Edit edit = new RemoveNodeEdit(session, info.position);
				edit.perform();
				break;
		}
		return true;
	}


	// ----------------Menu-----------------
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.details).setEnabled(session.getResult() != null);
		menu.findItem(R.id.revertNodes).setEnabled(session.getNodeModel().size() >= 2);
		return true;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.nodelistmenu, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.revertNodes:
				Edit edit = new ReverseNodesEdit(session);
				edit.perform();
				break;
			case R.id.details:
				showDialog(0);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
			case 0:
				String distanceUnit = getResources().getString(R.string.meter);
				int distance = session.getResult().getMisc().distance;
				if (distance > 1000) {
					distance = distance / 1000;
					distanceUnit = getResources().getString(R.string.kilometer);
				}
				Context mContext = this;
				dialog = new Dialog(mContext);
				dialog.setContentView(R.layout.route_details);
				dialog.setTitle(getResources().getString(R.string.details));

				TextView txtmarkercount = (TextView) dialog.findViewById(R.id.details_markercount);
				TextView txtdistance = (TextView) dialog.findViewById(R.id.details_distance);
				TextView txtmsg = (TextView) dialog.findViewById(R.id.details_message);

				txtmarkercount.setText(getResources().getString(R.string.amount_of_points) + ": " + session.getResult().getPoints().size());
				txtdistance.setText(getResources().getString(R.string.distance) + ": " + distance + " " + distanceUnit);
				String msg = session.getResult().getMisc().message;
				if (msg != null)
					txtmsg.setText(getResources().getString(R.string.message) + ": " + msg);
				break;
			default:
				dialog = null;
		}
		return dialog;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Edit edit;
		switch (resultCode) {
			case RESULT_OK:
				edit = new UpdateNodeEdit(session, data.getExtras().getInt("index"), (Node) data.getSerializableExtra("node"));
				edit.perform();
				dirty = true;
				break;
			case EditNodeScreen.RESULT_DELETE:
				edit = new RemoveNodeEdit(session, data.getExtras().getInt("index"));
				edit.perform();
				dirty = true;
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent data = new Intent();
			data.putExtra(NodeModel.IDENTIFIER, session.getNodeModel());
			setResult(dirty ? RESULT_OK : RESULT_CANCELED, data);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		session.removeListener(NodelistScreen.class);
		super.onDestroy();
	}

	@Override
	public void onChange(Session.Change change) {
		adapter.notifyDataSetChanged();
	}
}
