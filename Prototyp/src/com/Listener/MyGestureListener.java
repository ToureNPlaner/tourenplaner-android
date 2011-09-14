package com.Listener;

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapView;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

import com.ToureNPlaner.mapView;

public class MyGestureListener implements OnGestureListener{
private Context mContext;
private GestureDetector gestureScanner = new GestureDetector(this);
	public MyGestureListener (Context context){
		this.mContext = context;
	}
	
	// following methods overrides the common gesturelistener methods
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
/**
 * generates a marker at the press-position
 */
	@Override
	public void onLongPress(MotionEvent e) {
		GeoPoint p = mapView.mapView.getProjection().fromPixels((int) e.getX(),(int) e.getY());
		mapView.addMarkerToMap(p,"test","longpressed");
	         
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Toast.makeText(
				mContext,
				String.valueOf(e.getX()) + "," + String.valueOf(e.getY()), 600).show();
		return false;
	}

}
