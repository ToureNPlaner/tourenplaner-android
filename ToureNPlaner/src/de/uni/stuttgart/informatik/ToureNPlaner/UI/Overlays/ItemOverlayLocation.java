package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.OverlayItem;

public class ItemOverlayLocation extends ItemizedOverlay<OverlayItem> implements LocationListener {
	private OverlayItem overlayItem;
    private DrawableMarker drawableMarker;
	private GeoPoint gpsLocation = null;

    public ItemOverlayLocation(MapView mapview, GeoPoint geoPoint) {
		// ColorDrawable is just a workaround until the icons are loaded
		super(boundCenterBottom(new ColorDrawable()));
        this.gpsLocation = geoPoint;
        drawableMarker = new DrawableMarker(mapview, geoPoint, false);
        drawableMarker.setColor(Color.YELLOW);
        overlayItem = new OverlayItem(geoPoint, "", "", drawableMarker);
	}


	@Override
	public boolean onLongPress(GeoPoint geoPoint, MapView mapView) {
		return false;
	}

	@Override
	public int size() {
		return gpsLocation == null ? 0 : 1;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlayItem;
	}

	@Override
	protected boolean onTap(int index) {
		return false;
	}

	public void updateLocation(GeoPoint gp) {
        if(gp != null) {
            drawableMarker.setGp(gp);
            overlayItem.setPoint(gp);
            gpsLocation = gp;
            requestRedraw();
        }
	}


    @Override
    public void onLocationChanged(Location location) {
        updateLocation(new GeoPoint(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
