package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.mapsforge.android.maps.ArrayWayOverlay;
import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewMode;
import org.mapsforge.android.maps.Overlay;
import org.mapsforge.android.maps.OverlayItem;
import org.mapsforge.android.maps.OverlayWay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.MapGestureDetectorOverlay;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.NodeOverlay;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.Listener.OverlayListener;

public class MapScreen extends MapActivity implements Observer {
	public static MapScreen mapScreenContext;
	public static MapView mapView;
	private static NodeOverlay itemizedoverlay;
	private static List<Overlay> mapOverlays;
	private MapGestureDetectorOverlay mapGestureOverlay;
private static ArrayWayOverlay wayOverlay;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapscreen);
		//adding observer
		NodeModel.getInstance().addObserver(this);
		// setting properties of the mapview
		mapView = new MapView(this);
		mapView.setClickable(true);
		mapView.setLongClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setMapViewMode(MapViewMode.MAPNIK_TILE_DOWNLOAD);
		mapView.setFpsCounter(true);
		setContentView(mapView);

		mapOverlays = mapView.getOverlays();

		// initialize the Icons and ItemOverlays
		Drawable markerIcon = this.getResources().getDrawable(R.drawable.icon);
		itemizedoverlay = new NodeOverlay(markerIcon, this);

		// initialize components of MapGestureOverlay
		mapGestureOverlay = new MapGestureDetectorOverlay(new OverlayListener());
		mapGestureOverlay.setIsLongpressEnabled(true);
		mapOverlays.add(mapGestureOverlay);

		// On TouchListener for mapView handles events and call the
		// MapGestureOverlay
		mapView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mapGestureOverlay.onTouchEvent(event, mapView);
				return false;
			}
		});

		// TEMP
		GeoPoint geoPoint1 = new GeoPoint(52.514446, 13.350150); // Berlin Victory Column
		GeoPoint geoPoint2 = new GeoPoint(52.516272, 13.377722); // Brandenburg Gate
		GeoPoint geoPoint3 = new GeoPoint(52.525, 13.369444); // Berlin Central Station
		GeoPoint geoPoint4 = new GeoPoint(52.52, 13.369444); // German Chancellery
		GeoPoint geoPoint5 = new GeoPoint(52516999, 13388900);

		// initialize components of ItemOverlay
		addMarkerToMap(geoPoint1, "hah", "HH");
		addMarkerToMap(geoPoint2, "hah", "HH");
		addMarkerToMap(geoPoint3, "hah", "HH");
		addMarkerToMap(geoPoint4, "hah", "HH");
		addMarkerToMap(geoPoint5, "hah", "HH");
		// END TEMP

		// ----------------WayOverlay Properties-----------------
		// create the default paint objects for overlay ways
		Paint wayDefaultPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		wayDefaultPaintFill.setStyle(Paint.Style.STROKE);
		wayDefaultPaintFill.setColor(Color.BLUE);
		wayDefaultPaintFill.setAlpha(160);
		wayDefaultPaintFill.setStrokeWidth(7);
		wayDefaultPaintFill.setStrokeJoin(Paint.Join.ROUND);
		wayDefaultPaintFill.setPathEffect(new DashPathEffect(new float[] { 20,
				20 }, 0));

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
		// --------------------------------------------------------
		// set focus of MapScreen
		mapView.getController().setCenter(geoPoint1);
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
		case R.id.calculate:
			// generates an intent from the class NodeListScreen
		//	Intent myIntent = new Intent(this, NodelistScreen.class);
		//	startActivity(myIntent);
			addPathToMap();
			return true;
		case R.id.reset:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * this methode adds a marker to the mapview ( on the itemmapoverlay)
	 * 
	 * @param gp
	 *            is a GeoPoint
	 * @param title
	 *            of an item
	 * @param message
	 *            of an item
	 */
	public static void addMarkerToMap(GeoPoint gp, String title, String message) {
		OverlayItem overlayitem = new OverlayItem(gp, title, message);
		itemizedoverlay.addOverlay(overlayitem);
		NodeModel.getInstance().addNodeToVector(new Node(overlayitem.getTitle(), gp.getLatitude(), gp
						.getLongitude()));
		mapOverlays.add(itemizedoverlay);
	}


	public static void addPathToMap() {
		wayOverlay.clear();
		
//		GeoPoint geoPoint1 = new GeoPoint(52.514446, 13.350150); // Berlin Victory Column
//		GeoPoint geoPoint2 = new GeoPoint(52.516272, 13.377722); // Brandenburg Gate
//		GeoPoint geoPoint3 = new GeoPoint(52.525, 13.369444); // Berlin Central Station
//		GeoPoint geoPoint4 = new GeoPoint(52.52, 13.369444); // German Chancellery
//		GeoPoint geoPoint5 = new GeoPoint(52516999, 13388900);
//		OverlayWay way = new OverlayWay(new GeoPoint[][] { { geoPoint1,
//				geoPoint2, geoPoint3, geoPoint4,geoPoint5 } });
		
		OverlayWay way ;
		
		GeoPoint[][] geoArray = new GeoPoint[1][NodeModel.getInstance().size()];
		for (int i = 0; i < NodeModel.getInstance().size();i++){
			geoArray[0][i] = NodeModel.getInstance().get(i).getGeoPoint();
	
		}
		way = new OverlayWay (geoArray);
//		 OverlayWay way2 = new OverlayWay(new GeoPoint[][] { { geoPoint1,
//		 geoPoint3, geoPoint4,
//		 geoPoint1 } }, wayPaint, null);
		wayOverlay.addWay(way);
	
		// wayOverlay.addWay(way2);

		
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		addPathToMap();
		
	}
}
