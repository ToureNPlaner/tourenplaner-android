package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.util.List;

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewMode;
import org.mapsforge.android.maps.Overlay;
import org.mapsforge.android.maps.OverlayItem;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.MapGestureDetectorOverlay;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.NodeOverlay;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.Listener.OverlayListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class MapScreen extends MapActivity{
	public static MapScreen mapScreenContext;
	public static MapView mapView;
	private static NodeOverlay itemizedoverlay;
	private static List<Overlay> mapOverlays;
	private MapGestureDetectorOverlay mapGestureOverlay;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
	        setContentView(R.layout.mapscreen);
	        
	        
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
			itemizedoverlay = new NodeOverlay(markerIcon,this);
			
			// initialize components of MapGestureOverlay
			mapGestureOverlay = new MapGestureDetectorOverlay( new OverlayListener());
			mapGestureOverlay.setIsLongpressEnabled(true);
			mapOverlays.add(mapGestureOverlay);
			

			//  On TouchListener for mapView handles events and call the MapGestureOverlay
			mapView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					mapGestureOverlay.onTouchEvent(event, mapView);
					return false;
				}
			});

			
			//TEMP
			GeoPoint gp = new GeoPoint(52517039, 13388854);
			GeoPoint gp1 = new GeoPoint(52517040, 13388854);
			GeoPoint gp2 = new GeoPoint(52517041, 13388854);
			GeoPoint gp3 = new GeoPoint(52517042, 13388854);
			GeoPoint gp4 = new GeoPoint(52517043, 13388854);
			
			// initialize components of ItemOverlay
			addMarkerToMap(gp,"hah","HH");
			addMarkerToMap(gp1,"hah","HH");
			addMarkerToMap(gp2,"hah","HH");
			addMarkerToMap(gp3,"hah","HH");
			addMarkerToMap(gp4,"hah","HH");
			//END TEMP
			
			// set focus of MapScreen
			mapView.getController().setCenter(gp);
	}
			//----------------Menu-----------------
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
					Intent myIntent = new Intent(this,
							NodeScreen.class);
					startActivity(myIntent);
			        return true;
			    case R.id.reset:
			      
			        return true;
			    default:
			        return super.onOptionsItemSelected(item);
			    }
			}
	
	
	
	
	/**
	 * this methode adds a marker to the mapview ( on the itemmapoverlay)
	 * @param gp is a GeoPoint
	 * @param title of an item
	 * @param message of an item
	 */
	public static void addMarkerToMap(GeoPoint gp, String title, String message) {
		OverlayItem overlayitem = new OverlayItem(gp, title, message);
		itemizedoverlay.addOverlay(overlayitem);
		NodeModel.getNodeVector().add(new Node(overlayitem.getTitle(),gp.getLatitude(),gp.getLongitude()));
		mapOverlays.add(itemizedoverlay);
	}
}
