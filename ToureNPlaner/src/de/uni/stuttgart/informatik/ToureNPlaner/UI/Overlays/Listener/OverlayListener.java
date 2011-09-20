package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.Listener;
import org.mapsforge.android.maps.GeoPoint;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen;

/**
 * This Class provides functions depending on user gestures
 * 
 */
public class OverlayListener implements OnGestureListener{
	String markerName;
	String markerMessage;
	
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
				final GeoPoint gp = MapScreen.mapView.getProjection().fromPixels((int) e.getX(),(int) e.getY());
				markerName = "Marker Nr. " + String.valueOf(NodeModel.getInstance().size() +1 );
				Node node = new Node(markerName,gp);
				NodeModel.getInstance().addNodeToVector(node);
		MapScreen.addMarkerToMap(node);
	
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
