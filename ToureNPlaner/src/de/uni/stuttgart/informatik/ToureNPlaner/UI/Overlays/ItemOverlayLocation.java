package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.OverlayItem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;

public class ItemOverlayLocation extends ItemizedOverlay<OverlayItem> {
	private OverlayItem overlayItem = new OverlayItem();
	private Location gpsLocation = new Location("");
	private MapView mapview;

	public ItemOverlayLocation(Context context, MapView mapview,
			Location gpsLocation) {
		// ColorDrawable is just a workaround until the icons are loaded
		super(boundCenterBottom(new ColorDrawable()));
		this.gpsLocation = gpsLocation;
		this.mapview = mapview;
		loadFromModel();
	}


	private void loadFromModel() {
		GeoPoint gp = new GeoPoint(gpsLocation.getLatitude(),
				gpsLocation.getLongitude());
		addLocationToMap(gp, "");
		requestRedraw();
	}

	@Override
	public boolean onLongPress(GeoPoint geoPoint, MapView mapView) {
		return true;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlayItem;
	}

	@Override
	protected boolean onTap(int index) {
		// OverlayItem item = list.get(index);
		// AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		Log.v("tap", "tap triggered");
		// dialog.setTitle(item.getTitle());
		// dialog.setMessage(item.getSnippet());
		// dialog.show();
		return true;
	}

	public void addLocationToMap(GeoPoint gp, String name) {
		Node node = new Node(name, gp.getLatitude(), gp.getLongitude());
		DrawableMarker dm = new DrawableMarker(mapview, node.getGeoPoint());
		dm.setColor(Color.YELLOW);
		overlayItem = new OverlayItem(node.getGeoPoint(), "", "",dm);
		gpsLocation.setLatitude(gp.getLatitude());
		gpsLocation.setLongitude(gp.getLongitude());
		requestRedraw();
	}

	
}
