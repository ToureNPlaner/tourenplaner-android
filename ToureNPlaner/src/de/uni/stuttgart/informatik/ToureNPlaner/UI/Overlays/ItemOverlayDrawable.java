package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import java.util.ArrayList;

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.NodePreferences;

public class ItemOverlayDrawable extends ItemizedOverlay<OverlayItem> implements LocationListener {
	private ArrayList<OverlayItem> list = new ArrayList<OverlayItem>();
	private NodeModel nodeModel;
	private MapView mapview;
	private final AlgorithmInfo algorithmInfo = null;
	public static final int RequestCodeItemOverlay = 1;
	private Context context;
	protected String Tag = "default";
	public Intent ItemOverlayIntent;
	private GeoPoint GPSGeoPoint = null;
	private DrawableMarker GPSMarker;
	private OverlayItem overlayItem;
	public ItemOverlayDrawable(Context context,NodeModel nodeModel,MapView mapview) {
		// ColorDrawable is just a workaround until the icons are loaded
		super(boundCenterBottom(new ColorDrawable()));
		this.nodeModel = nodeModel;
		this.mapview = mapview;
		this.context = context;
		loadFromModel();
	}

	public void setNodeModel(NodeModel nodeModel) {
		this.nodeModel = nodeModel;
		loadFromModel();
	}

	private void loadFromModel() {
		list.clear();
		addGPSMarkerToMap(nodeModel.getGPSGeoPoint());
		for (int i = 0; i < nodeModel.size(); i++) {
			addMarkerToMap(nodeModel.get(i));
		}
			updateIcons();
		requestRedraw();
	}

	@Override
	public boolean onLongPress(GeoPoint geoPoint, MapView mapView) {
		String markerName = "Marker Nr. "
				+ String.valueOf(nodeModel.size() + 1);
		Node node = new Node(markerName, geoPoint.getLatitudeE6()*10,
				geoPoint.getLongitudeE6()*10);
		nodeModel.addNodeToVector(node);
		addMarkerToMap(node);
		
		mapView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

		updateIcons();
		requestRedraw();

		return true;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return list.get(i);
	}

	@Override
	public boolean onTap( int i) {
		
		if(((DrawableMarker)list.get(i).getMarker()).isChangeable() == false){
			final int gpsindex = i;
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Wollen Sie ihren aktuellen Standort als Startpunkt setzen?")
			       .setCancelable(false)
			       .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   // transform the GpsMarker into a regular mapMarker on Position 0
			        	   ((DrawableMarker)list.get(gpsindex).getMarker()).setChangable(true);
			        	   ((DrawableMarker)list.get(gpsindex).getMarker()).setDrawText(true);
			        	   nodeModel.setGPSGeoPoint(null);
			        	   Node gpsStartnode = new Node("gpsLocation",list.get(gpsindex).getPoint().getLatitudeE6()*10,list.get(gpsindex).getPoint().getLongitudeE6()*10);
			        	   nodeModel.addNodeAtIndexToVector(0, gpsStartnode);
			        	   updateIcons();
			        	   requestRedraw();
			           }
			       })
			       .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
			
		Log.d("pressed","gps");	
		}else{
			// correction of index because of additional gpsmarker when GpsLocation exists
			if(((DrawableMarker)list.get(0).getMarker()).isChangeable() == false){
			
			i -=1;
			}
			 ItemOverlayIntent = new Intent(context, NodePreferences.class);
			 ItemOverlayIntent.putExtra("node", nodeModel.getNodeVector().get(i));
			 ItemOverlayIntent.putExtra("index", i);
	        ((Activity) context).startActivityForResult(ItemOverlayIntent,RequestCodeItemOverlay);
			
		}
		
	return false;
	}
	
	public void addMarkerToMap(Node node) {
		DrawableMarker dm = new DrawableMarker(mapview, node.getGeoPoint(),true);
		OverlayItem overlayitem = new OverlayItem(node.getGeoPoint(), "", "",
				dm);
		ItemizedOverlay.boundCenterBottom(dm);
		overlayitem.setMarker(dm);
		list.add(overlayitem);
	}
	
	public void clear() {
		nodeModel.clear();
		list.clear();
		addGPSMarkerToMap(nodeModel.getGPSGeoPoint());
		requestRedraw();
	}
	
	public void updateIcons() {
		// if (!algorithmInfo.sourceIsTarget() &&
		// list.size() > 0) {
		boolean hasStartpoint = false;
		int bound = 0;
		int index = 1;
		if (list.size() > 0) {
			// set boundsize
			bound = (int) (((DrawableMarker) list.get(0).getMarker()).getBound());
			// checks whether a marker is changeable or not. if a marker is changeable and it is the first of this kind
			// it is the startmarker 
			for (int i = 0; i < list.size() ; i++) {
				if(((DrawableMarker) list.get(i).getMarker()).isChangeable()){
					if(hasStartpoint == false){
						((DrawableMarker) list.get(i).getMarker()).setColor(Color.GREEN);
						((DrawableMarker) list.get(i).getMarker()).setBounds(0, 0, bound,bound);
						((DrawableMarker) list.get(i).getMarker()).SetIndex(index);
						index+=1;
						hasStartpoint = true;
					}else{
					((DrawableMarker) list.get(i).getMarker()).setColor(Color.BLUE);
					((DrawableMarker) list.get(i).getMarker()).SetIndex(index);
					index+=1;
					((DrawableMarker) list.get(i).getMarker()).setBounds(0, 0, bound,bound);
								}
				}
			}
			if(((DrawableMarker) list.get(list.size() - 1).getMarker()).isChangeable() && list.size() >2){
			((DrawableMarker) list.get(list.size() - 1).getMarker()).setColor(Color.RED);
			((DrawableMarker) list.get(list.size() - 1).getMarker()).SetIndex(index-1);
			((DrawableMarker) list.get(list.size() - 1).getMarker()).setBounds(0, 0, bound,bound);
			}
			
			}
	}
	// -----------------GPS------------------

	public void addGPSMarkerToMap(GeoPoint GPSGeoPoint){
		if (GPSGeoPoint != null){
			  GPSMarker = new DrawableMarker(mapview, GPSGeoPoint, false);
			  GPSMarker.setColor(Color.YELLOW);
			  GPSMarker.setChangable(false);
			  GPSMarker.setBounds(0, 0, 8,8);
			  overlayItem = new OverlayItem(GPSGeoPoint, "", "", GPSMarker);
			  nodeModel.setGPSGeoPoint(GPSGeoPoint);
			  list.add(overlayItem);
			}
	}
	
	@Override
    public void onLocationChanged(Location location) {
    
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

	
}