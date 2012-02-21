package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.widget.Toast;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.core.GeoPoint;

import java.util.ArrayList;

public class FastWayOverlay extends Overlay {
	public synchronized void initWay(int[][] points) {
		for (int[] p : points) {
			ways.add(new Way(p));
		}
		requestRedraw();
	}

	private MapView mapView;
	private final Paint paint;
	private final Path path = new Path();
	private final float thickness;

	private final ArrayList<Way> ways = new ArrayList<Way>();

	public FastWayOverlay(MapView mapView, Paint paint) {
		this.mapView = mapView;
		this.paint = paint;
		this.path.setFillType(Path.FillType.EVEN_ODD);
		// About 3.1 mm: 20/160 inch
		this.thickness = 20.f * mapView.getResources().getDisplayMetrics().density;
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

	private Toast toast = null;

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		if (toast == null)
			toast = Toast.makeText(mapView.getContext(), "", Toast.LENGTH_SHORT);
		float dist = getNearestWaySegment(geoPoint, mapView);
		if (dist < thickness) {
			toast.cancel();
			toast.setText("Dist: " + dist);
			toast.show();
		}

		return false;
	}

	private synchronized float getNearestWaySegment(GeoPoint geoPoint, MapView mapView) {
		float min = Float.MAX_VALUE;
		int index = 0;

		byte zoomLevel = this.internalMapView.getMapPosition().getZoomLevel();
		Point drawnPosition = mapView.getProjection().toPoint(this.internalMapView.getMapPosition().getMapCenter(), null, zoomLevel);
		Point p = mapView.getProjection().toPixels(geoPoint, null);
		p.x = p.x - mapView.getWidth() / 2;
		p.y = p.y - mapView.getHeight() / 2;

		for (int i = 0; i < ways.size(); i++) {
			float result = ways.get(i).getDistance(p, drawnPosition, zoomLevel);
			if (result < min) {
				index = i;
				min = result;
			}
		}
		return (float) Math.sqrt(min);
	}

	@Override
	protected synchronized void drawOverlayBitmap(Canvas canvas, Point drawPosition, Projection projection, byte drawZoomLevel) {
		if (ways.isEmpty()) {
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
