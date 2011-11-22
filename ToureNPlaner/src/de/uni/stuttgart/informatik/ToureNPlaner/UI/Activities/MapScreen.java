package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.RequestHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.NodeOverlay;
import org.mapsforge.android.maps.*;

public class MapScreen extends MapActivity implements Observer {
	public MapView mapView;
	private ArrayWayOverlay wayOverlay;
	private Session session;
	public final static int REQUEST_CODE_MAP_SCREEN = 0;
	private NodeOverlay nodeOverlay;
	private RequestHandler handler = null;
	GeoPoint gpsGeoPoint = null;

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
		mapView.setMapViewMode(MapViewMode.CUSTOM_TILE_DOWNLOAD);
		mapView.setMapTileDownloadServer("gerbera.informatik.uni-stuttgart.de/osm/tiles");
		// mapView.setMapFile("/sdcard/berlin.map");
		//mapView.setFpsCounter(true);
		mapView.setMemoryCardCachePersistence(true);
		mapView.setMemoryCardCacheSize(100);//overlay for nodeItems

		initializeHandler();

		setupWayOverlay();

		setupGPS(isFirstStart);
		nodeOverlay.updateIcons();
		mapView.getOverlays().add(nodeOverlay);
	}

	private void setupGPS(boolean isFirstStart) {
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		

		if (loc != null) {
			gpsGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
		}

		// setting up LocationManager and set MapFocus on lastknown GPS-Location
		if (isFirstStart) {
			mapView.getController().setCenter(gpsGeoPoint);
		}

		nodeOverlay = new NodeOverlay(this, session.getSelectedAlgorithm(), session.getNodeModel(), gpsGeoPoint);

		// 5 minutes, 50 meters
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5 * 60 * 1000, 50, nodeOverlay);
	}

	private void setupWayOverlay() {
		Paint wayDefaultPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
		wayDefaultPaintOutline.setStyle(Paint.Style.STROKE);
		wayDefaultPaintOutline.setColor(Color.BLUE);
		wayDefaultPaintOutline.setAlpha(160);
		wayDefaultPaintOutline.setStrokeWidth(5);
		wayDefaultPaintOutline.setStrokeJoin(Paint.Join.ROUND);

		// create the WayOverlay and add the ways
		wayOverlay = new ArrayWayOverlay(wayDefaultPaintOutline,
				null);
		mapView.getOverlays().add(wayOverlay);
		Result result = session.getResult();
		if (result != null) {
			addPathToMap(result.getPoints());
		}
	}

	// ----------------Menu-----------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mapscreenmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.nodelist:
				// generates an intent from the class NodeListScreen
				Intent myIntent = new Intent(this, NodelistScreen.class);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivityForResult(myIntent, REQUEST_CODE_MAP_SCREEN);
				return true;
			case R.id.reset:
				// clear nodes
				nodeOverlay.clear();
				// clear path
				wayOverlay.clear();
				session.setResult(null);
				mapView.invalidate();
				return true;
			case R.id.calculate:
				if (session.getNodeModel().size() > 1) {
					handler = (RequestHandler) new RequestHandler(session, this).execute();
					setProgressBarIndeterminateVisibility(true);
				}
				return true;
			
			case R.id.resultlist:
				Intent myIntentResult = new Intent(this, NodeResultlistScreen.class);
				myIntentResult.putExtra(Session.IDENTIFIER, session);
				startActivity(myIntentResult);
				return true;
			case R.id.gps:
				if(gpsGeoPoint!=null){
					 mapView.getController().setCenter(gpsGeoPoint);
				}
				return true;
				
			case R.id.back:
			finish();
				return true;
//		case R.id.gotofirst:
//		if (nodeOverlay.getNodeModel().size() > 0){
//			mapView.getController().setCenter(nodeOverlay.getNodeModel().get(0).getGeoPoint());
//			}
//		return true;
	
			default:
				return super.onOptionsItemSelected(item);
		}


	}
	
	
	//---------------Key-Events--------------------
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	int NodeModelsize =  nodeOverlay.getNodeModel().size();
	    	NodeModel tempNodeModel = new NodeModel();
	    	if (NodeModelsize > 0){
	    	// create a tempNodeModel to force a redraw of the NodeOverlay 
	    	nodeOverlay.getNodeModel().remove(NodeModelsize-1);
	    	tempNodeModel = nodeOverlay.getNodeModel();
	     	nodeOverlay.setNodeModel(tempNodeModel);
	    	nodeOverlay.updateIcons();
	    	wayOverlay.clear();
			mapView.invalidate();
		    return true;
	    	}else{
	    		// go back to algorithmscreen when no marker is left
	    		 return super.onKeyDown(keyCode, event);
	    		
	    	}
	    
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
			case REQUEST_CODE_MAP_SCREEN:

				session.setNodeModel((NodeModel) data.getExtras().getSerializable(
						NodeModel.IDENTIFIER));
				nodeOverlay.setNodeModel(session.getNodeModel());
				nodeOverlay.updateIcons();
				break;
			case NodeOverlay.REQUEST_CODE_ITEM_OVERLAY:
				switch (resultCode) {
					case RESULT_OK:
						session.getNodeModel().getNodeVector().set(data.getExtras().getInt("index"), (Node) data.getSerializableExtra("node"));
						nodeOverlay.updateIcons();
						break;
					case EditNodeScreen.RESULT_DELETE:
						session.getNodeModel().getNodeVector().remove(data.getExtras().getInt("index"));
						nodeOverlay.setNodeModel(session.getNodeModel());
						nodeOverlay.updateIcons();
						wayOverlay.clear();
						mapView.invalidate();
				}
		}
	}

	public void addPathToMap(GeoPoint[][] points) {
		wayOverlay.clear();
		wayOverlay.addWay(new OverlayWay(points));
	}

	private void initializeHandler() {
		handler = (RequestHandler) getLastNonConfigurationInstance();

		if (handler != null) {
			handler.setListener(this);
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
	public void onCompleted(Object object) {
		handler = null;
		Result result = (Result) object;
		session.setResult(result);
		addPathToMap(result.getPoints());
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onError(Object object) {
		handler = null;
		Toast.makeText(getApplicationContext(), object.toString(),
				Toast.LENGTH_LONG).show();
		setProgressBarIndeterminateVisibility(false);
	}
}
