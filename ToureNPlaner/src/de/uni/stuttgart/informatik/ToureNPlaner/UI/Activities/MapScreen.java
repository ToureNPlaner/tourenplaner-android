package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session.RequestHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.ItemOverlayDrawable;
import org.mapsforge.android.maps.*;

public class MapScreen extends MapActivity implements Observer {
    public MapView mapView;
    private ArrayWayOverlay wayOverlay;
    private Session session;
    public final static  int RequestCodeMapScreen = 0;
    private ItemOverlayDrawable itemizedoverlay;
    private Session.RequestHandler handler = null;
    private GeoPoint GPSGeoPoint = null;

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

        //overlay for nodeItems
        itemizedoverlay = new ItemOverlayDrawable(this,session.getNodeModel(), mapView);
       
        setupGPS(isFirstStart);
        itemizedoverlay.addGPSMarkerToMap(GPSGeoPoint);
        itemizedoverlay.updateIcons();
        mapView.getOverlays().add(itemizedoverlay);
       
        setupWayOverlay();
    }

    private void setupGPS(boolean isFirstStart) {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(loc != null) {
        	GPSGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
        }

        // setting up LocationManager and set MapFocus on lastknown GPS-Location
        if(isFirstStart) {
            mapView.getController().setCenter(GPSGeoPoint);
        }
        // 5 minutes, 50 meters
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5 * 60 * 1000, 50, itemizedoverlay);
    }

    private void setupWayOverlay() {
        // ----------------WayOverlay Properties-----------------
        // create the default paint objects for overlay ways
        Paint wayDefaultPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayDefaultPaintFill.setStyle(Paint.Style.STROKE);
        wayDefaultPaintFill.setColor(Color.BLUE);
        wayDefaultPaintFill.setAlpha(160);
        wayDefaultPaintFill.setStrokeWidth(7);
        wayDefaultPaintFill.setStrokeJoin(Paint.Join.ROUND);
        wayDefaultPaintFill.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
        Paint wayDefaultPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayDefaultPaintOutline.setStyle(Paint.Style.STROKE);
        wayDefaultPaintOutline.setColor(Color.BLUE);
        wayDefaultPaintOutline.setAlpha(128);
        wayDefaultPaintOutline.setStrokeWidth(7);
        wayDefaultPaintOutline.setStrokeJoin(Paint.Join.ROUND);

        // create an individual paint object for an overlay way
        Paint wayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayPaint.setStyle(Paint.Style.FILL);
        wayPaint.setColor(Color.YELLOW);
        wayPaint.setAlpha(192);
        // create the WayOverlay and add the ways
        wayOverlay = new ArrayWayOverlay(wayDefaultPaintFill,
                wayDefaultPaintOutline);
        mapView.getOverlays().add(wayOverlay);
        Result result = session.getResult();
        if(result != null) {
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
			startActivityForResult(myIntent, RequestCodeMapScreen);
			return true;
		case R.id.reset:
			// clear nodes
			itemizedoverlay.clear();
			// clear path
			wayOverlay.clear();
			session.setResult(null);
			mapView.invalidate();
			return true;
		case R.id.calculate:
			if (session.getNodeModel().size() > 1) {
				handler = (RequestHandler) new Session.RequestHandler(session, this).execute();
				setProgressBarIndeterminateVisibility(true);
			}
			return true;
		case R.id.resultlist:
			Intent myIntentResult = new Intent(this, NodeResultlistScreen.class);
			myIntentResult.putExtra(Session.IDENTIFIER, session);
			startActivity(myIntentResult);
			return true;
			
//		case R.id.gotofirst:
//		if (itemizedoverlay.getNodeModel().size() > 0){
//			mapView.getController().setCenter(itemizedoverlay.getNodeModel().get(0).getGeoPoint());
//			}
//		return true;
//		case R.id.gotogps:
//		if(GPSGeoPoint!=null){
//			 mapView.getController().setCenter(GPSGeoPoint);
//		}
//		return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
			
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case RequestCodeMapScreen:

			session.setNodeModel((NodeModel) data.getExtras().getSerializable(
					NodeModel.IDENTIFIER));
			itemizedoverlay.setNodeModel(session.getNodeModel());
			itemizedoverlay.updateIcons();
			break;
		case ItemOverlayDrawable.RequestCodeItemOverlay:
			Intent sender = itemizedoverlay.ItemOverlayIntent;
			int DataIndex = sender.getExtras().getInt("index");
			switch (resultCode) {
			case RESULT_OK:
				session.getNodeModel().getNodeVector().set(DataIndex,(Node) data.getSerializableExtra("node"));

				break;
			case NodePreferences.RESULT_DELETE:
				session.getNodeModel().getNodeVector().remove(DataIndex);
				itemizedoverlay.setNodeModel(session.getNodeModel());
				itemizedoverlay.updateIcons();
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
        handler = (Session.RequestHandler) getLastNonConfigurationInstance();

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
