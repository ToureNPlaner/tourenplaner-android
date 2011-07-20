package com.Overlayers;

import java.util.ArrayList;

import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

@SuppressWarnings("rawtypes")
public class MapItemOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	public MapItemOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		}
	
	public MapItemOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}

	@Override
	public int size() {
	  return mOverlays.size();
	}

	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	/**
	 * this methode is called when the user tap on an item
	 */
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
	@Override
	protected boolean onLongPress(int index){
		// something to do when an item is long pressed
		
		return true;
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

}
