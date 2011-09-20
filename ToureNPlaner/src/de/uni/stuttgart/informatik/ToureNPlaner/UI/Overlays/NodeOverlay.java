package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import java.util.ArrayList;
import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.widget.EditText;

@SuppressWarnings("rawtypes")
public class NodeOverlay extends ItemizedOverlay{
	private Context context;
	private ArrayList<OverlayItem> NodeArray = new ArrayList<OverlayItem>();
	
	public NodeOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			this.context = context;
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
//	  OverlayItem item = NodeArray.get(index);
//	  AlertDialog.Builder alert = new AlertDialog.Builder(context);
//
//	  alert.setTitle(item.getTitle());
//	  alert.setMessage("Message");
//
//	  // Set an EditText view to get user input 
//	  final EditText input = new EditText(context);
//	  alert.setView(input);
//
//	  alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	  public void onClick(DialogInterface dialog, int whichButton) {
//	    Editable value = input.getText();
//	    // Do something with value!
//	    }
//	  });
//
//	  alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//	    public void onClick(DialogInterface dialog, int whichButton) {
//	      // Canceled.
//	    }
//	  });
//
//	  alert.show();
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
