package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapView;

public class DrawableMarker extends Drawable {
    private MapView mapView;
    private GeoPoint gp;
    private int color = Color.BLACK;
    private int index = 1;

    private double bound;

    private final Paint circle;
    private final Paint circleLine;
    private final Paint textPaint;
    private final DrawFilter drawFilter;
    private final Point point;
    private boolean isChangeable = true;
    private Boolean isDrawText = true;
    

	

	public DrawableMarker(MapView mapview, GeoPoint gp, Boolean isDrawText) {
        this.mapView = mapview;
        this.gp = gp;
        this.isDrawText = isDrawText;

        circle = new Paint();
        circleLine = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(16);
        textPaint.setTextAlign(Paint.Align.CENTER);
        circleLine.setColor(Color.BLACK);
        // draw line with antialiasing
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG);
        point = new Point();
    }

 
    @Override
    public void draw(Canvas canvas) {
        int radiusFactor = 1;
        // Transfrom geoposition to Point on canvas
        mapView.getProjection().toPixels(gp, point);
        canvas.setDrawFilter(drawFilter);

        circle.setColor(color);

        //add a line for the circle
        canvas.drawCircle(point.x, point.y, (float) ((mapView.getZoomLevel()) * radiusFactor) + 1, circleLine);
        bound = mapView.getZoomLevel() * radiusFactor + 1;
        // add a factor to customize the standard radius
        canvas.drawCircle(point.x, point.y, (float) (mapView.getZoomLevel()) * radiusFactor, circle);
        // draw Text on the circle
        // x position depending on amount of numbers

        if (isDrawText) {
            canvas.drawText(String.valueOf(index), point.x, point.y + 6f, textPaint);
        }
    }

    
    public void setGp(GeoPoint gp) {
        this.gp = gp;
    }
    public boolean isChangeable() {
		return isChangeable;
	}

	public void setChangable(boolean isChangable) {
		this.isChangeable = isChangable;
	}
	public Boolean getIsDrawText() {
		return isDrawText;
	}

	public void setDrawText(Boolean isDrawText) {
		this.isDrawText = isDrawText;
	}
    public double getBound() {
        return bound;
    }

    public void SetIndex(Integer index) {
        this.index = index;
    }

    public void setColor(int color) {
        this.color = color;

    }

    @Override
    public int getOpacity() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setAlpha(int alpha) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub

    }
}
