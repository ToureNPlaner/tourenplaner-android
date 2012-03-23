package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen;

import android.location.Location;
import android.os.Bundle;
import org.mapsforge.core.GeoPoint;

class GpsListener implements android.location.LocationListener {
	private MapScreen mapScreen;
	private boolean enabled;

	public GpsListener(MapScreen mapScreen) {
		this.mapScreen = mapScreen;
	}

	@Override
	public void onLocationChanged(Location location) {
		GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
		mapScreen.nodeOverlay.updateGpsMarker(geoPoint);
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {
	}

	@Override
	public void onProviderEnabled(String s) {
		enabled = true;
		mapScreen.invalidateOptionsMenu();
	}

	@Override
	public void onProviderDisabled(String s) {
		enabled = false;
		mapScreen.nodeOverlay.updateGpsMarker(null);
		mapScreen.invalidateOptionsMenu();
	}

	public boolean isEnabled() {
		return enabled;
	}
}
