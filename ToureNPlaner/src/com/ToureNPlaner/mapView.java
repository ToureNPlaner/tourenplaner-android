package com.ToureNPlaner;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Overlay;
import org.mapsforge.android.maps.OverlayItem;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.Listener.MyGestureListener;
import com.Overlayers.LineOverlay;
import com.Overlayers.MapGestureDetectorOverlay;
import com.Overlayers.MapItemOverlay;

public class mapView extends MapActivity {
	/** Called when the activity is first created. */
	
	
	public static float xCoordinate;
	public static float yCoordinate;
	private MapGestureDetectorOverlay mapGestureOverlay;
	public static MapView mapView;
	private static List<Overlay> mapOverlays;
	private static MapItemOverlay itemizedoverlay;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// setting properties of the mapview
		mapView = new MapView(this);
		mapView.setClickable(true);
		mapView.setLongClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setMapFile("/sdcard/berlin.map");
		setContentView(mapView);
		// ----- initialize components of overlays 
		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
		
		// initialize components of ItemOverlay
		itemizedoverlay = new MapItemOverlay(drawable, this);
		
		// initialize components of MapGestureOverlay
		mapGestureOverlay = new MapGestureDetectorOverlay(
				this, new MyGestureListener(this));
		mapGestureOverlay.setIsLongpressEnabled(true);

		
		//  On TouchListener for mapView handles events and call the MapGestureOverlay
		mapView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				mapGestureOverlay.onTouchEvent(event, mapView);
				return false;
			}

		});

		//TESTAREA
		GeoPoint gp = new GeoPoint(52517039, 13388854);
		GeoPoint gp2 = new GeoPoint(52517300, 13388870);
		addMarkerToMap(gp, "", "");
		addMarkerToMap(gp2, "", "");
			
		mapOverlays.add(new LineOverlay(this, new Canvas(), mapView, false));        

		
		//ENDTESTAREA
		
		
		//adds the layers to the mapview

		
		mapOverlays.add(mapGestureOverlay);
		
	}
	/**
	 * this methode adds a marker to the mapview ( on the itemmapoverlay)
	 * 
	 * @param i X coordinate of a GeoPoint 
	 * @param j	Y coordinate of a GeoPoint
	 * @param title of an item
	 * @param message of an item
	 */
	public static void addMarkerToMap(int i, int j, String title, String message) {
		GeoPoint point = new GeoPoint(i, j);
		OverlayItem overlayitem = new OverlayItem(point, title, message);
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
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
		mapOverlays.add(itemizedoverlay);
	}

}
