package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.AddNodeEdit;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Edit;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.core.GeoPoint;

import java.util.ArrayList;

public class FastWayOverlay extends Overlay {
	private final Session session;

	public synchronized void initWay(int[][] points) {
		for (int[] p : points) {
			ways.add(new Way(p));
		}
		requestRedraw();
	}

	private final Paint paint;
	private final Path path = new Path();
	private final float thickness;

	private final ArrayList<Way> ways = new ArrayList<Way>();

	public FastWayOverlay(Session session, Paint paint) {
		this.session = session;
		this.paint = paint;
		this.path.setFillType(Path.FillType.EVEN_ODD);
		// About 3.1 mm: 20/160 inch
		this.thickness = 20.f * ToureNPlanerApplication.getContext().getResources().getDisplayMetrics().density;
	}

	public synchronized void clear() {
		ways.clear();

		path.reset();
		requestRedraw();
	}

	@Override
	protected String getThreadName() {
		return "FastWayOverlay";
	}

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		if (session.getNodeModel().size() >= session.getSelectedAlgorithm().getMaxPoints()) {
			return false;
		}

		int index = getNearestWaySegment(geoPoint, mapView);
		if (index == -1)
			return false;

		final Node node = session.createNode(geoPoint);
		if (node != null) {
			Edit edit = new AddNodeEdit(session, node, index + 1);
			edit.perform();
		}
		return true;
	}

	private synchronized int getNearestWaySegment(GeoPoint geoPoint, MapView mapView) {
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

		if (Math.sqrt(min) < thickness)
			return index;
		else
			return -1;
	}

	@Override
	protected synchronized void drawOverlayBitmap(Canvas canvas, Point drawPosition, Projection projection, byte drawZoomLevel) {
		if (ways.isEmpty()) {
			// stop working
			return;
		}

		GeoPoint topLeft = projection.fromPixels(0, 0);
		GeoPoint bottomRight = projection.fromPixels(this.internalMapView.getWidth(), this.internalMapView.getHeight());
		for (Way way : ways) {
			way.setupPath(path, drawPosition, drawZoomLevel, topLeft.longitudeE6, topLeft.latitudeE6, bottomRight.longitudeE6, bottomRight.latitudeE6);
			canvas.drawPath(path, paint);
		}
	}
}
