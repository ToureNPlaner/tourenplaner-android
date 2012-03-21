package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Context;
import com.actionbarsherlock.app.SherlockActivity;
import org.mapsforge.android.maps.IMapActivity;
import org.mapsforge.android.maps.MapView;

public class MapActivity extends SherlockActivity implements IMapActivity {
	private MapView mapView;

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
