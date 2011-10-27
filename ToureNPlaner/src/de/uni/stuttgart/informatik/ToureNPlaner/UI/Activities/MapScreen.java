package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.util.ArrayList;

import org.mapsforge.android.maps.ArrayWayOverlay;
import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewMode;
import org.mapsforge.android.maps.OverlayWay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.ItemOverlayDrawable;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.ItemOverlayLocation;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.GPSLocationListener;

public class MapScreen extends MapActivity implements Observer {
	public MapView mapView;
	private ArrayWayOverlay wayOverlay;
	private Session session;
	// private ItemOverlay itemizedoverlay;
	private ItemOverlayDrawable itemizedoverlay;
	private ItemOverlayLocation itemizedoverlaylocation;
	private AsyncTask<Void, Void, Object> handler;
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// If we get created for the first time we get our data from the intent
		if (savedInstanceState != null) {
			session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
		} else {
			session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
		}
				// setting properties of the mapview
				mapView = new MapView(this);
				mapView.setClickable(true);
				mapView.setLongClickable(true);
				mapView.setBuiltInZoomControls(true);
				mapView.setMapViewMode(MapViewMode.MAPNIK_TILE_DOWNLOAD);
				// mapView.setMapFile("/sdcard/berlin.map");
				mapView.setFpsCounter(true);
				mapView.setMemoryCardCachePersistence(true);
				mapView.setMemoryCardCacheSize(1000);//overlay for nodeItems

				setContentView(mapView);
				Log.v("entering","Try GPS");
				try{
				Log.v("gps","pending");
				LocationManager locManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
				Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				Log.v("GPSProvider",LocationManager.GPS_PROVIDER.toString());
				// setting up LocationManager and set MapFocus on lastknown GPS-Location 
				GeoPoint gp = new GeoPoint(loc.getLatitude(),loc.getLongitude());
				session.setCurrentLocation(gp);
				mapView.getController().setCenter(gp);
				//overlay for locationItems
				itemizedoverlaylocation = new ItemOverlayLocation(this, mapView,loc);
				
				//overlay for nodeItems
				itemizedoverlaylocation.addLocationToMap(gp,"");
				mapView.getOverlays().add(itemizedoverlaylocation);
				LocationListener mlocListener = new GPSLocationListener(this,itemizedoverlaylocation);
				locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
				Log.v("gps","succed");
				}catch(Exception e){
					Log.v("gps","failed" + e.getMessage());//overlay for nodeItems

				}
				try{
					mapView.getController().setCenter(session.getCurrentLocation());		
				}catch(Exception e){
				}
				//overlay for nodeItems
				itemizedoverlay = new ItemOverlayDrawable(this, session.getNodeModel(),	mapView);
				
				mapView.getOverlays().add(itemizedoverlay);
				
				setupWayOverlay();
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
		wayDefaultPaintFill.setPathEffect(new DashPathEffect(new float[] {20,20}, 0));
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
			startActivityForResult(myIntent, 0);
			return true;
		case R.id.reset:
			// clear nodes
			itemizedoverlay.clear();
			// clear path
			wayOverlay.clear();
		
			return true;
		case R.id.calculate:
			handler = new Session.RequestHandler(session, this).execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		session.setNodeModel((NodeModel) data.getExtras().getSerializable(
				NodeModel.IDENTIFIER));
		itemizedoverlay.setNodeModel(session.getNodeModel());
	}
	public void addPathToMap(ArrayList<GeoPoint> points) {
		wayOverlay.clear();
		OverlayWay way;
		GeoPoint[][] geoArray = new GeoPoint[1][points.size()];
		for (int i = 0; i < points.size(); i++) {
			geoArray[0][i] = points.get(i);
		}
		way = new OverlayWay(geoArray);
		wayOverlay.addWay(way);
	}
	@Override
	public void onCompleted(Object object) {
		handler = null;
		Result result = (Result) object;
		addPathToMap(result.getPoints());
	}
	@Override
	public void onError(Object object) {
		handler = null;
		Toast.makeText(getApplicationContext(), object.toString(),
				Toast.LENGTH_LONG).show();
	}
}
