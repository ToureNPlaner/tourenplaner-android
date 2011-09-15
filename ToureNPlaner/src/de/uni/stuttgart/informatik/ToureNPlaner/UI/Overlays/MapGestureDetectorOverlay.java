package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Overlay;
import org.mapsforge.android.maps.Projection;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class MapGestureDetectorOverlay extends Overlay implements OnGestureListener {
 private GestureDetector gestureDetector;
 private OnGestureListener onGestureListener;
 public MapGestureDetectorOverlay() {
  gestureDetector = new GestureDetector(this);
 }

 public MapGestureDetectorOverlay(OnGestureListener onGestureListener) {
  this();
  setOnGestureListener(onGestureListener);
 }

/**
 * is called from the mapview touchlistener
 * @param event
 * @param mapView
 * @return
 */
 public boolean onTouchEvent(MotionEvent event, MapView mapView) {
	 
	 if (gestureDetector.onTouchEvent(event)) {
   return true;
  }
  return false;
 }

 
 // following methods are referenced to the methods in com.Listener.MyGestureListener
 @Override
 public boolean onDown(MotionEvent e) {
  if (onGestureListener != null) {
   return onGestureListener.onDown(e);
  }
  return false;
 }

 @Override
 public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
   float velocityY) {
  if (onGestureListener != null) {
   return onGestureListener.onFling(e1, e2, velocityX, velocityY);
  }
  return false;
 }

 @Override
 public void onLongPress(MotionEvent e) {
  if (onGestureListener != null) {
   onGestureListener.onLongPress(e);
  }
 }

 @Override
 public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
   float distanceY) {
  if (onGestureListener != null) {
   onGestureListener.onScroll(e1, e2, distanceX, distanceY);
  }
  return false;
 }

 @Override
 public void onShowPress(MotionEvent e) {
  if (onGestureListener != null) {
   onGestureListener.onShowPress(e);
  }
 }

 @Override
 public boolean onSingleTapUp(MotionEvent e) {
  if (onGestureListener != null) {
   onGestureListener.onSingleTapUp(e);
  }
  return false;
 }

 public boolean isLongpressEnabled() {
  return gestureDetector.isLongpressEnabled();
 }

 public void setIsLongpressEnabled(boolean isLongpressEnabled) {
  gestureDetector.setIsLongpressEnabled(isLongpressEnabled);
 }

 public OnGestureListener getOnGestureListener() {
  return onGestureListener;
 }

 public void setOnGestureListener(OnGestureListener onGestureListener) {
  this.onGestureListener = onGestureListener;
 }

@Override
protected void drawOverlayBitmap(Canvas canvas, Point drawPosition,
		Projection projection, byte drawZoomLevel) {
	// TODO Auto-generated method stub
	
}


}
