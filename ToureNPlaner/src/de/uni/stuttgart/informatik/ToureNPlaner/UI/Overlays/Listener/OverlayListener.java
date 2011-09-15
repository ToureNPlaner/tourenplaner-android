package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.Listener;
import org.mapsforge.android.maps.GeoPoint;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * This Class provides functions depending on user gestures
 * 
 */
public class OverlayListener implements OnGestureListener{

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		GeoPoint p = MapScreen.mapView.getProjection().fromPixels((int) e.getX(),(int) e.getY());
		MapScreen.addMarkerToMap(p,"test","longpressed");
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
