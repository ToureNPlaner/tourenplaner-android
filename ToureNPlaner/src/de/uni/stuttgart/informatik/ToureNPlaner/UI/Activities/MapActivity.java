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

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import org.mapsforge.android.maps.IMapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;

public class MapActivity extends FragmentActivity implements IMapActivity {
	private MapView mapView;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable("Center", mapView.getMapPosition().getMapCenter());
		outState.putByte("ZoomLvl", mapView.getMapPosition().getZoomLevel());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.mapscreen);

		if (savedInstanceState != null) {
			mapView.setCenter((GeoPoint) savedInstanceState.get("Center"));
			// TODO restore zoom
		}
	}


	@Override
	protected void onDestroy() {
		mapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public int getMapViewId() {
		return 0;
	}

	@Override
	public void registerMapView(MapView mapView) {
		this.mapView = mapView;
	}
}
