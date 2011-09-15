package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import java.util.ArrayList;
import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.OverlayItem;
import android.graphics.drawable.Drawable;

@SuppressWarnings("rawtypes")
public class NodeOverlay extends ItemizedOverlay{
	
	private ArrayList<OverlayItem> NodeArray = new ArrayList<OverlayItem>();
	
	public NodeOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return NodeArray.size();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return NodeArray.get(i);
	}
	
	/**
	 * this methode is called when the user tap on an item
	 */
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = NodeArray.get(index);
	
	  //TODO item porperty 
	  return true;
	}
	
	@Override
	protected boolean onLongPress(int index){
		// something to do when an item is long pressed
		
		return true;
	}
	
	public void addOverlay(OverlayItem overlay) {
	    NodeArray.add(overlay);
	    populate();
	}

}
