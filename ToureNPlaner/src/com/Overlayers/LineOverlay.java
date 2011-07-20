package com.Overlayers;

// doesn't work

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Overlay;
import org.mapsforge.android.maps.Projection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.widget.Toast;

public class LineOverlay extends Overlay{
	
	 public LineOverlay(Context context,Canvas canvas, MapView mapView, boolean shadow){
draw(context,canvas,mapView,shadow);

	    }   

	    public void draw(Context context,Canvas canvas, MapView mapView, boolean shadow){
	       
	    	//super method can't be called from import org.mapsforge.android.maps.Overlay
	    	
	    	//super.draw(canvas, mapView, shadow);

	    Paint   mPaint = new Paint();
	        mPaint.setDither(true);
	        mPaint.setColor(Color.RED);
	        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	        mPaint.setStrokeJoin(Paint.Join.ROUND);
	        mPaint.setStrokeCap(Paint.Cap.ROUND);
	        mPaint.setStrokeWidth(5);

	        // berlin
	        GeoPoint gP1 = new GeoPoint(52517039, 13388854);
			GeoPoint gP2 = new GeoPoint(52517300, 13388870);
		

	        Point p1 = new Point();
	        Point p2 = new Point();

	    Path    path = new Path();

	    mapView.getProjection().toPixels(gP1, p1);
	        mapView.getProjection().toPixels(gP2, p2);
	    	Toast.makeText(
					context,
					String.valueOf(p1), 300).show();
	    	Toast.makeText(
					context,
					String.valueOf(p2), 600).show();

	        path.moveTo(p2.x, p2.y);
	        path.lineTo(p1.x,p1.y);
	        canvas.drawPath(path, mPaint);
	    }

		

		@Override
		protected void drawOverlayBitmap(Canvas canvas, Point drawPosition,
				Projection projection, byte drawZoomLevel) {
			
			
		}


}
