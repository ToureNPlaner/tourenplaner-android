package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.core.GeoPoint;

import java.util.ArrayList;

public class FastWayOverlay extends Overlay {
	public void initWay(GeoPoint[][] points) {
		for (GeoPoint[] p : points) {
			ways.add(new Way(p));
		}
		requestRedraw();
	}

	private MapView mapView;
	private final Paint paint;
	private final Path path = new Path();

	private final ArrayList<Way> ways = new ArrayList<Way>();

	public FastWayOverlay(MapView mapView, Paint paint) {
		this.mapView = mapView;
		this.paint = paint;
		this.path.setFillType(Path.FillType.EVEN_ODD);
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

	public synchronized void clear() {
		ways.clear();

		path.reset();
		requestRedraw();
	}

	static int numPoints;
	static long clipping;
	static long caching;
	static long pathing;
	static int pointsDrawn;

	@Override
	protected String getThreadName() {
		return "FastWayOverlay";
	}

	@Override
	protected synchronized void drawOverlayBitmap(Canvas canvas, Point drawPosition, Projection projection, byte drawZoomLevel) {
		if (isInterrupted() || ways.isEmpty()) {
			// stop working
			return;
		}
		long totalStartTime;
		long startTime, endTime;
		GeoPoint topLeft = projection.fromPixels(0, 0);
		GeoPoint bottomRight = projection.fromPixels(this.mapView.getWidth(), this.mapView.getHeight());
		for (Way way : ways) {
			totalStartTime = System.nanoTime();
			way.setupPath(path, drawPosition, drawZoomLevel, topLeft.longitudeE6, topLeft.latitudeE6, bottomRight.longitudeE6, bottomRight.latitudeE6);
			startTime = System.nanoTime();
			canvas.drawPath(path, paint);
			endTime = System.nanoTime();
			long rendering = (endTime - startTime) / 1000;
			long total = (endTime - totalStartTime) / 1000;
			Log.v("STATS", drawZoomLevel + ", " + Way.steps[drawZoomLevel] + ", " + numPoints + ", " + pointsDrawn + ", " + clipping + ", " + caching + ", " + pathing + ", " + rendering + ", " + total);
		}
	}
}
