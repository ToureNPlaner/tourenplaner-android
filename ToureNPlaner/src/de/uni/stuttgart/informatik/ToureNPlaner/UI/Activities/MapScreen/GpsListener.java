package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen;

import android.location.Location;
import android.os.Bundle;
import org.mapsforge.core.GeoPoint;

import java.lang.ref.WeakReference;

class GpsListener implements android.location.LocationListener {
	private final WeakReference<MapScreen> mapScreen;
	private boolean enabled;
	private boolean following;
	private GeoPoint lastKnownLocation = null;

	private static final String IDENTIFIER = "GpsListenerFollowing";

	public GpsListener(MapScreen mapScreen, Bundle savedInstanceState, GeoPoint geoPoint) {
		this.mapScreen = new WeakReference<MapScreen>(mapScreen);
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
		MapScreen ms = mapScreen.get();
		if (ms != null) {
			ms.nodeOverlay.updateGpsMarker(geoPoint);
			if (following) {
				ms.mapView.setCenter(geoPoint);
			}
		}
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {
	}

	@Override
	public void onProviderEnabled(String s) {
		enabled = true;
		MapScreen ms = mapScreen.get();
		if (ms != null)
			ms.invalidateOptionsMenu();
	}

	@Override
	public void onProviderDisabled(String s) {
		enabled = false;
		lastKnownLocation = null;
		MapScreen ms = mapScreen.get();
		if (ms != null) {
			ms.nodeOverlay.updateGpsMarker(null);
			ms.invalidateOptionsMenu();
		}
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
			MapScreen ms = mapScreen.get();
			if (ms != null) {
				ms.mapView.setCenter(lastKnownLocation);
			}
		}
	}
}
