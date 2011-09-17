package com.ToureNPlaner;


import java.util.List;
import java.util.Observable;
import java.util.Observer;


import org.mapsforge.android.maps.ArrayWayOverlay;
import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewMode;
import org.mapsforge.android.maps.Overlay;
import org.mapsforge.android.maps.OverlayItem;
import org.mapsforge.android.maps.OverlayWay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.Listener.MyGestureListener;
import com.Overlayers.LineOverlay;
import com.Overlayers.MapGestureDetectorOverlay;
import com.Overlayers.MapItemOverlay;

public class mapView extends MapActivity implements Observer{
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
		
		
		//adding an observer
	
		// setting properties of the mapview
		mapView = new MapView(this);
		mapView.setClickable(true);
		mapView.setLongClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setMapViewMode(MapViewMode.MAPNIK_TILE_DOWNLOAD);
		mapView.setFpsCounter(true);
		//mapView.setMapFile("/sdcard/berlin.map");
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
		
		// create some points to be used in the different overlays
		GeoPoint geoPoint1 = new GeoPoint(52.514446, 13.350150); // Berlin Victory Column
		GeoPoint geoPoint2 = new GeoPoint(52.516272, 13.377722); // Brandenburg Gate
		GeoPoint geoPoint3 = new GeoPoint(52.525, 13.369444); // Berlin Central Station
		GeoPoint geoPoint4 = new GeoPoint(52.52, 13.369444); // German Chancellery
		// create the default paint objects for overlay ways
		Paint wayDefaultPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		wayDefaultPaintFill.setStyle(Paint.Style.STROKE);
		wayDefaultPaintFill.setColor(Color.BLUE);
		wayDefaultPaintFill.setAlpha(160);
		wayDefaultPaintFill.setStrokeWidth(7);
		wayDefaultPaintFill.setStrokeJoin(Paint.Join.ROUND);
		wayDefaultPaintFill.setPathEffect(new DashPathEffect(new float[] { 20, 20 }, 0));

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
		ArrayWayOverlay wayOverlay = new ArrayWayOverlay(wayDefaultPaintFill,
				wayDefaultPaintOutline);
		OverlayWay way1 = new OverlayWay(new GeoPoint[][] { { geoPoint1, geoPoint2, geoPoint3, geoPoint4 } });
		//OverlayWay way2 = new OverlayWay(new GeoPoint[][] { { geoPoint1, geoPoint3, geoPoint4,
		//		geoPoint1 } }, wayPaint, null);
		wayOverlay.addWay(way1);
		//wayOverlay.addWay(way2);
		
		mapView.getOverlays().add(wayOverlay);
		
		mapView.getController().setCenter(geoPoint1);

		
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
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
