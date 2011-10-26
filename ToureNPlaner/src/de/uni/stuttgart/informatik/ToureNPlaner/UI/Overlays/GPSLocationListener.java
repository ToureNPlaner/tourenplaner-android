package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import org.mapsforge.android.maps.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/* Class My Location Listener */

public class GPSLocationListener implements LocationListener

{
	private Context context;
	private ItemOverlayLocation io;

	public GPSLocationListener(Context context, ItemOverlayLocation io) {
		this.context = context;
		this.io = io;
	}

	@Override
	public void onLocationChanged(Location loc)
	{
		
		GeoPoint gp = new GeoPoint(loc.getLatitude(), loc.getLongitude());
		io.addLocationToMap(gp,"");
		
		
		String Text = "neue GPS Koordinaten erhalten";
		Toast.makeText(context,	Text,Toast.LENGTH_SHORT).show();
	}	@Override
	public void onProviderDisabled(String provider)
	{
		Toast.makeText(context,"Gps Disabled",Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onProviderEnabled(String provider)

	{

		Toast.makeText(context,"Gps Enabled",Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
