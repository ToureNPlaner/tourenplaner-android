package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen;

import android.location.Location;
import android.os.Bundle;
import org.mapsforge.core.GeoPoint;

class GpsListener implements android.location.LocationListener {
	private MapScreen mapScreen;
	private boolean enabled;
	private boolean following;
	private GeoPoint lastKnownLocation = null;

	private static final String IDENTIFIER = "GpsListenerFollowing";

	public GpsListener(MapScreen mapScreen, Bundle savedInstanceState, GeoPoint geoPoint) {
		this.mapScreen = mapScreen;
		lastKnownLocation = geoPoint;
		enabled = lastKnownLocation != null;
		if (savedInstanceState != null)
			following = savedInstanceState.getBoolean(IDENTIFIER, false);
	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(IDENTIFIER, following);
	}

	@Override
	public void onLocationChanged(Location location) {
		GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
		lastKnownLocation = geoPoint;
		mapScreen.nodeOverlay.updateGpsMarker(geoPoint);
		if (following) {
			mapScreen.mapView.setCenter(geoPoint);
		}
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
		lastKnownLocation = null;
		mapScreen.nodeOverlay.updateGpsMarker(null);
		mapScreen.invalidateOptionsMenu();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
		if (following && lastKnownLocation != null) {
			mapScreen.mapView.setCenter(lastKnownLocation);
		}
	}
}
