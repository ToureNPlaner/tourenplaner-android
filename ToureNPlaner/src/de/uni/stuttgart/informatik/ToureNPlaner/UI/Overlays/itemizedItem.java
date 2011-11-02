package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;

public class itemizedItem extends OverlayItem{
	DrawableMarker di;
	
	public itemizedItem(MapView mv, GeoPoint gp , Boolean text){
	 this.di = new DrawableMarker(null, point, null);
	 this.di.setBounds(0, 0, this.di.getIntrinsicWidth(), this.di.getIntrinsicHeight()); 
	}
	
		
}
