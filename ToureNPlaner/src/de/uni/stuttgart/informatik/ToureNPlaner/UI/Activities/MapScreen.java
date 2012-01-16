package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.*;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.*;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.NodeOverlay;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewMode;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;
import org.mapsforge.android.maps.overlay.OverlayWay;
import org.mapsforge.core.GeoPoint;

import java.util.ArrayList;

public class MapScreen extends MapActivity implements Session.Listener {
	private MapView mapView;
	private ArrayWayOverlay wayOverlay;
	private Session session;
	public static final int REQUEST_NODEMODEL = 0;
	public static final int REQUEST_NODE = 1;
	private NodeOverlay nodeOverlay;
	private RequestHandler handler = null;

	private final ArrayList<RequestNN> requestList = new ArrayList<RequestNN>();

	private final Observer requestListener = new Observer() {
		@Override
		public void onCompleted(ConnectionHandler caller, Object object) {
			handler = null;
			Edit edit = new SetResultEdit(session, (Result) object);
			edit.perform();
			setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onError(ConnectionHandler caller, Object object) {
			handler = null;
			setProgressBarIndeterminateVisibility(false);
			Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
		}
	};

	private final Observer nnsListener = new Observer() {
		@Override
		public void onCompleted(ConnectionHandler caller, Object object) {
			Edit edit = new UpdateNNSEdit(session, ((RequestNN) caller).getNode(), ((Node) object).getGeoPoint());
			edit.perform();
			requestList.remove((RequestNN) caller);
		}

		@Override
		public void onError(ConnectionHandler caller, Object object) {
			Toast.makeText(getApplicationContext(), object.toString(),
					Toast.LENGTH_LONG).show();
			requestList.remove((RequestNN) caller);
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		boolean isFirstStart = savedInstanceState == null;
		// If we get created for the first time we get our data from the intent
		if (savedInstanceState != null) {
			session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
		} else {
			session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
		}

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		// setting properties of the mapview
		setContentView(R.layout.activity_mapscreen);
		this.mapView = (MapView) findViewById(R.id.mapView);
		mapView.setClickable(true);
		mapView.setLongClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setMapViewMode(MapViewMode.MAPNIK_TILE_DOWNLOAD);
		//mapView.setRenderTheme(MapView.DEFAULT_RENDER_THEME);
		//mapView.setMapTileDownloadServer("gerbera.informatik.uni-stuttgart.de/osm/tiles");
		// mapView.setMapFile("/sdcard/berlin.map");
		//mapView.setFpsCounter(true);
		//mapView.setMemoryCardCachePersistence(true);
		//mapView.setMemoryCardCacheSize(100);//overlay for nodeItems

		initializeHandler();

		setupWayOverlay();

		setupGPS(isFirstStart);

		mapView.getOverlays().add(nodeOverlay);

		session.registerListener(nodeOverlay);
		session.registerListener(this);
	}

	private void setupGPS(boolean isFirstStart) {
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		GeoPoint gpsGeoPoint = null;

		if (loc != null) {
			gpsGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
		}

		// setting up LocationManager and set MapFocus on lastknown GPS-Location
		if (isFirstStart) {
			mapView.getController().setCenter(gpsGeoPoint);
		}

		Drawable drawable = getResources().getDrawable(R.drawable.markericon);
		nodeOverlay = new NodeOverlay(this, session, gpsGeoPoint, drawable);
	}

	private void setupWayOverlay() {
		Paint wayDefaultPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
		wayDefaultPaintOutline.setStyle(Paint.Style.STROKE);
		wayDefaultPaintOutline.setColor(Color.BLUE);
		wayDefaultPaintOutline.setAlpha(160);
		wayDefaultPaintOutline.setStrokeWidth(5);
		wayDefaultPaintOutline.setStrokeJoin(Paint.Join.ROUND);

		// create the WayOverlay and add the ways
		wayOverlay = new ArrayWayOverlay(wayDefaultPaintOutline, null);
		mapView.getOverlays().add(wayOverlay);
		Result result = session.getResult();
		if (result != null) {
			addPathToMap(result.getWay());
		}
	}

	// ----------------Menu-----------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mapscreenmenu, menu);
		MenuItemCompat.setShowAsAction(menu.findItem(R.id.calculate), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.nodelist:
				Intent myIntent = new Intent(this, NodelistScreen.class);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivityForResult(myIntent, REQUEST_NODEMODEL);
				return true;
			case R.id.reset:
				if (handler != null) {
					handler.cancel(true);
					setProgressBarIndeterminateVisibility(false);
					handler = null;
				}
				Edit edit = new ClearEdit(session);
				edit.perform();
				return true;
			case R.id.calculate:
				performRequest();
				return true;
			case R.id.resultlist:
//				Intent myIntentResult = new Intent(this, NodeResultlistScreen.class);
//				myIntentResult.putExtra(Session.IDENTIFIER, session);
//				startActivity(myIntentResult);
				return true;
			case R.id.gps:
				mapView.getController().setCenter(nodeOverlay.getGpsPosition());
				return true;
			case R.id.back:
				finish();
				return true;
			case R.id.settings:
				startActivity(new Intent(this, Preferences.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}


	}

	private void performRequest() {
		if (session.getNodeModel().size() >= session.getSelectedAlgorithm().getMinPoints()) {
			if (handler != null)
				handler.cancel(true);
			handler = (RequestHandler) new RequestHandler(session, requestListener).execute();
			setProgressBarIndeterminateVisibility(true);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int NodeModelsize = session.getNodeModel().size();

			if (NodeModelsize > 0) {
				Edit edit = new RemoveNodeEdit(session, NodeModelsize - 1);
				edit.perform();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Edit edit;
		switch (requestCode) {
			case REQUEST_NODEMODEL:
				switch (resultCode) {
					case RESULT_OK:
						edit = new ChangeNodeModelEdit(session, (NodeModel) data.getExtras().getSerializable(NodeModel.IDENTIFIER));
						edit.perform();
						break;
				}
				break;
			case REQUEST_NODE:
				switch (resultCode) {
					case RESULT_OK:
						edit = new UpdateNodeEdit(session, data.getExtras().getInt("index"), (Node) data.getSerializableExtra("node"));
						edit.perform();
						break;
					case EditNodeScreen.RESULT_DELETE:
						edit = new RemoveNodeEdit(session, data.getExtras().getInt("index"));
						edit.perform();
						break;
				}
		}
	}

	public void addPathToMap(GeoPoint[][] points) {
		wayOverlay.clear();
		wayOverlay.addWay(new OverlayWay(points));
	}

	public void performNNSearch(Node node) {
		requestList.add((RequestNN) new RequestNN(nnsListener, session, node).execute());
	}

	private void initializeHandler() {
		handler = (RequestHandler) getLastNonConfigurationInstance();

		if (handler != null) {
			handler.setListener(requestListener);
			setProgressBarIndeterminateVisibility(true);
		} else {
			setProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return handler;
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 5 minutes, 50 meters
		locManager.removeUpdates(nodeOverlay);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 5 minutes, 50 meters
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5 * 60 * 1000, 50, nodeOverlay);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		session.removeListener(nodeOverlay);
		session.removeListener(this);

		if (handler != null)
			handler.setListener(null);

		for (RequestNN request : requestList) {
			request.setListener(null);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Only disable the button, if we don't have an action bar. Version < HONEYCOMB
		if (Build.VERSION.SDK_INT < 11) {
			menu.findItem(R.id.calculate).setEnabled(session.getNodeModel().size() >= session.getSelectedAlgorithm().getMinPoints());
		}
		return true;
	}

	@Override
	public void onChange(final int change) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (0 < (change & Session.RESULT_CHANGE)) {
					if (session.getResult() == null) {
						wayOverlay.clear();
					} else {
						addPathToMap(session.getResult().getWay());
					}
				}
				if (0 < (change & Session.MODEL_CHANGE)) {
					if (0 == (change & Session.ADD_CHANGE)) {
						Edit edit = new SetResultEdit(session, null);
						edit.perform();
					}
					performRequest();
				}
			}
		});
	}
}
