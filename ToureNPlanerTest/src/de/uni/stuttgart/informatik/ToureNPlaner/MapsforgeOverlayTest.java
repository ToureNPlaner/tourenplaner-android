package de.uni.stuttgart.informatik.ToureNPlaner;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.test.ActivityUnitTestCase;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.Overlay;

public class MapsforgeOverlayTest extends ActivityUnitTestCase<MapsforgeOverlayTest.MapActivityImpl> {

	public static class MapActivityImpl extends MapActivity {
	}

	public MapsforgeOverlayTest() {
		super(MapActivityImpl.class);
	}

	static class OverlayImpl extends Overlay {
		@Override
		protected void drawOverlayBitmap(Canvas canvas, Point drawPosition, Projection projection, byte drawZoomLevel) {
			Paint p = new Paint();
			p.setColor(Color.BLACK);
			canvas.drawRect(0.f,0.f,internalMapView.getDrawingHeight(), internalMapView.getDrawingWidth(), p);
		}
	}

	static class MapViewImpl extends MapView {
		public MapViewImpl(Context context) {
			super(context);
		}
		
		int size = 10;

		@Override
		public int getDrawingHeight() {
			return size;
		}

		@Override
		public int getDrawingWidth() {
			return size;
		}
	}

	public void testRedraw() throws Exception {
		Bitmap bitmap100x100 = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap100x100);
		startActivity(new Intent(), null, null);
		MapViewImpl mapView = new MapViewImpl(getActivity());
		mapView.size = 10;
		Overlay overlay = new OverlayImpl();
		overlay.start();
		overlay.setupOverlay(mapView);
		overlay.onSizeChanged();

		// draw
		overlay.requestRedraw();
		// change Size
		mapView.size = 100;
		overlay.onSizeChanged();
		// draw
		overlay.requestRedraw();
		// check
		overlay.draw(canvas);

		Thread.sleep(500);

		int pixel = bitmap100x100.getPixel(99,99);

		assertEquals(Color.BLACK, pixel);

		overlay.interrupt();
		overlay.join();

		bitmap100x100.recycle();
	}

	/**
	 * This test confirms the bug!
	 */
	public void testCanvasBug() {
		Bitmap bitmap10x10 = Bitmap.createBitmap(10,10,Bitmap.Config.ARGB_8888);
		Bitmap bitmap100x100 = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();

		canvas.setBitmap(bitmap10x10);

		// Force caching
		canvas.quickReject(0f,0f,0f,0f, Canvas.EdgeType.BW);

		// Cache won't be cleared
		canvas.setBitmap(bitmap100x100);

		// Should be inside the bitmap, but will be rejected nevertheless!
		assertTrue(canvas.quickReject(15.f, 15f, 15f, 15f, Canvas.EdgeType.BW));

		bitmap10x10.recycle();
		bitmap100x100.recycle();
	}

	/**
	 * This test test the workaround!
	 */
	public void testCanvasWorkaround() {
		Bitmap bitmap10x10 = Bitmap.createBitmap(10,10,Bitmap.Config.ARGB_8888);
		Bitmap bitmap100x100 = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();

		canvas.setBitmap(bitmap10x10);

		// Force caching
		canvas.quickReject(0f,0f,0f,0f, Canvas.EdgeType.BW);

		// Cache won't be cleared
		canvas.setBitmap(bitmap100x100);

		// Force cache update
		canvas.setMatrix(canvas.getMatrix());

		// Should be inside the bitmap, so won't be rejected
		assertFalse(canvas.quickReject(15.f, 15f, 15f, 15f, Canvas.EdgeType.BW));

		bitmap10x10.recycle();
		bitmap100x100.recycle();
	}
}
