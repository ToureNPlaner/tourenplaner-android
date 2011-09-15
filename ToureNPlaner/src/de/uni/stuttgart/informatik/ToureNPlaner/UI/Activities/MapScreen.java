package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewMode;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import android.os.Bundle;


public class MapScreen extends MapActivity{
	
	
	public static MapView mapView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
	        setContentView(R.layout.mapscreen);
	        
	        
	     // setting properties of the mapview
			mapView = new MapView(this);
			mapView.setClickable(true);
			mapView.setLongClickable(true);
			mapView.setBuiltInZoomControls(true);
			mapView.setMapViewMode(MapViewMode.MAPNIK_TILE_DOWNLOAD);
			mapView.setFpsCounter(true);
			setContentView(mapView);
		
			
	        
	}
	
}
