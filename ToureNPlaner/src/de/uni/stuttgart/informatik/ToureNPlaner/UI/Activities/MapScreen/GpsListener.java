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

import android.content.Context;
import android.hardware.*;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;
import org.mapsforge.core.GeoPoint;

import java.lang.ref.WeakReference;

import static java.lang.Math.pow;

class GpsListener implements android.location.LocationListener, SensorEventListener {
	private final WeakReference<MapScreen> mapScreen;
	private boolean enabled;
	private boolean following;
	private GeoPoint lastKnownLocation = null;

	private static final String IDENTIFIER = "GpsListenerFollowing";
	public SensorManager sensorMgr;
	public static final int sensordelay = SensorManager.SENSOR_DELAY_NORMAL; // (int)
	// pow(10,6);
	Session session;

	public GpsListener(MapScreen mapScreen, Bundle savedInstanceState, GeoPoint geoPoint, Session s) {
		this.mapScreen = new WeakReference<MapScreen>(mapScreen);
		lastKnownLocation = geoPoint;
		enabled = lastKnownLocation != null;
		if (savedInstanceState != null)
			following = savedInstanceState.getBoolean(IDENTIFIER, false);

		// TODO: too much battery drain when running all the time?
		sensorMgr = (SensorManager) ToureNPlanerApplication.getContext()
				.getSystemService(Context.SENSOR_SERVICE);
		this.session = s;
	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(IDENTIFIER, following);
	}

	private static final float grav[] = new float[3]; //Gravity (a.k.a accelerometer data)
	private static final float mag[] = new float[3]; //Magnetic
	private static final float rotation[] = new float[9]; //Rotation matrix in Android format
	private static final float orientation[] = new float[3]; //azimuth, pitch, roll
	private static float smoothed[] = new float[3];

	@Override
	public void onLocationChanged(Location location) {
		gmf = new GeomagneticField((float) location.getLatitude(),
				(float) location.getLongitude(),
				(float) location.getAltitude(),
				System.currentTimeMillis());
		GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
		lastKnownLocation = geoPoint;
		MapScreen ms = mapScreen.get();
		if (ms != null) {
			//TODO: Do we want to use bearing data from gps or is the compass enough for us?
			//ms.nodeOverlay.updateGpsMarker(geoPoint, location.hasBearing() ? location.getBearing() : -1);
			ms.nodeOverlay.updateGpsMarker(geoPoint);
			if (following) {
				ms.mapView.setCenter(geoPoint);
			}
			if (session.getTBTNavigation() != null && session.getTBTNavigation().currentlyRunning()) {
				session.getTBTNavigation().updatedLocation(location);
			}
		} else {
			Log.d("tp", "Updated location, but have no mapscreen to display it");
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

	// http://code.google.com/p/android-compass/source/browse/trunk/src/com/jwetherell/compass/common/LowPassFilter.java
	public static float[] lowpassfilter(float[] input, float[] prev) {
		if (input == null || prev == null)
			throw new NullPointerException("input and prev float arrays must be non-NULL");
		if (input.length != prev.length)
			throw new IllegalArgumentException("input and prev must be the same length");

		for (int i = 0; i < input.length; i++) {
			prev[i] = prev[i] + 0.2f * (input[i] - prev[i]);
		}
		return prev;
	}

	private GeomagneticField gmf = null;
	private double floatBearing = 0;
	private Sensor sensorGrav = null;
	private Sensor sensorMag = null;
	private long lastmagneticevent = 0;
	private long lastaccelerometerevent = 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		// mapsforge cannot really handle so much updates and we can't tell the sensor reliably how often we want
		// updates, so throw too quickly arriving events away
		// x * 10^9 = x seconds
		if (event.timestamp - lastaccelerometerevent < 0.2*pow(10,9) && event.timestamp - lastmagneticevent < 0.2 * pow(10, 9)) {
			return;
		}

		//Log.d("tp", "Sensoracc, " + event.sensor.getName() + " " + event.accuracy);
		// We probably get so much events that we can just throw away all the low accuracy ones
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && event.accuracy < SensorManager.SENSOR_STATUS_ACCURACY_LOW) {
			Log.v("tp", "omitted " + event.sensor.getName() + " event, because its accuracy was " + event.accuracy + ", it must be at least low (" + SensorManager.SENSOR_STATUS_ACCURACY_LOW + ")");
			return;
		}

		//Log.d("tp", event.sensor.getName() + " Event / " + event.values.toString());

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			lastaccelerometerevent = event.timestamp;
			smoothed = lowpassfilter(event.values, grav);
			grav[0] = smoothed[0];
			grav[1] = smoothed[1];
			grav[2] = smoothed[2];
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			lastmagneticevent = event.timestamp;
			smoothed = lowpassfilter(event.values, mag);
			mag[0] = smoothed[0];
			mag[1] = smoothed[1];
			mag[2] = smoothed[2];
		}

		//Get rotation matrix given the gravity and geomagnetic matrices
		SensorManager.getRotationMatrix(rotation, null, grav, mag);
		SensorManager.getOrientation(rotation, orientation);
		//TODO: rotated screen doesn't work properly yet
		floatBearing = orientation[0];
		//Log.d("tp", "Azimuth: " + orientation[0] + " pitch " + orientation[1] + " roll " + orientation[2]);

		//Convert from radians to degrees
		floatBearing = Math.toDegrees(floatBearing); //degrees east of true north (180 to -180)

		//Compensate for the difference between true north and magnetic north
		if (gmf != null) floatBearing += gmf.getDeclination();

		//adjust to 0-360
		if (floatBearing < 0) floatBearing += 360;

		Log.v("tp", "direction " + floatBearing);
		if (mapScreen != null && mapScreen.get() != null && mapScreen.get().nodeOverlay != null) {
			session.setDirection(floatBearing);
			mapScreen.get().nodeOverlay.updateGPSDrawableDirection();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
	public Sensor getSensorGrav() {
		return sensorGrav;
	}

	public Sensor getSensorMag() {
		return sensorMag;
	}

	public void setSensorGrav(Sensor sensorGrav) {
		this.sensorGrav = sensorGrav;
	}

	public void setSensorMag(Sensor sensorMag) {
		this.sensorMag = sensorMag;
	}
}
