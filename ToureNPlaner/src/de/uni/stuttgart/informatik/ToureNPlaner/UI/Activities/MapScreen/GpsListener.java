/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
			ms.supportInvalidateOptionsMenu();
	}

	@Override
	public void onProviderDisabled(String s) {
		enabled = false;
		lastKnownLocation = null;
		MapScreen ms = mapScreen.get();
		if (ms != null) {
			ms.nodeOverlay.updateGpsMarker(null);
			ms.supportInvalidateOptionsMenu();
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
