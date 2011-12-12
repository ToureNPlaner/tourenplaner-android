package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextPaint;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.RequestNN;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.EditNodeScreen;
import org.mapsforge.android.maps.*;

import java.util.ArrayList;

public class NodeOverlay extends ItemizedOverlay<OverlayItem> implements LocationListener {
	private ArrayList<OverlayItem> list = new ArrayList<OverlayItem>();

	private final Context context;
	private NodeModel nodeModel;
	private final AlgorithmInfo algorithmInfo;

	public static final int REQUEST_CODE_ITEM_OVERLAY = 1;
	private final static int MARKER_BOUNDS = 10;
	private final static int GPS_RADIUS = 8;
	private String markerName = "noName";
	private OverlayItem gpsMarker;

	private boolean useGps = false;
	private byte lastZoomLevel = Byte.MIN_VALUE;

	private GpsDrawable gpsDrawable;
	private NodeDrawable.Parameters nodeParameters;
	private Drawable defaultDrawable;
	public NodeOverlay(Context context, AlgorithmInfo algorithmInfo, NodeModel nodeModel, GeoPoint gpsPoint,Drawable defaultDrawable) {
		// ColorDrawable is just a workaround until the icons are loaded
		super(boundCenterBottom(defaultDrawable));
		this.algorithmInfo = algorithmInfo;
		this.nodeModel = nodeModel;
		this.context = context;
this.defaultDrawable= defaultDrawable;
		setupParameters();
		setupGpsDrawable();

		loadFromModel();
		updateGpsMarker(gpsPoint);
	}

	private void setupGpsDrawable() {
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.YELLOW);
		gpsDrawable = new GpsDrawable(p);
		gpsDrawable.setBounds(-GPS_RADIUS,-GPS_RADIUS,GPS_RADIUS,GPS_RADIUS);
	}

	private void setupParameters() {
		Paint circle = new Paint();
		circle.setAntiAlias(true);
		TextPaint textPaint = new TextPaint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(16);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setAntiAlias(true);
		textPaint.toString();
		Paint circleLine = new Paint();
		circleLine.setAntiAlias(true);
		circleLine.setColor(Color.BLACK);
		nodeParameters = new NodeDrawable.Parameters(textPaint, circleLine, circle);
	}

	public GeoPoint getGpsPosition() {
		return gpsMarker == null ? null : gpsMarker.getPoint();
	}

	public void setNodeModel(NodeModel nodeModel) {
		this.nodeModel = nodeModel;
		loadFromModel();
	}
	
	public NodeModel getNodeModel() {
		return nodeModel;
	}

	private void loadFromModel() {
		list.clear();
		for (int i = 0; i < nodeModel.size(); i++) {
			addMarkerToMap(nodeModel.get(i));
		}
		updateIcons();
	}
	
	
	
	@Override
	public boolean onLongPress(GeoPoint geoPoint, MapView mapView) {
		markerName = String.valueOf(nodeModel.size() + 1);
		final Node node = Node.createNode(markerName, geoPoint);
		addMarkerToMap(node);
		nodeModel.add(node);
		mapView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		updateIcons();

		((MapScreen)context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((MapScreen)context).triggerNNlookup(node);
			}
		});
		return true;
	}

	public void onModelChanged() {
		loadFromModel();
	}

	/***
	 * 
	 * @param gp
	 * @param gparray
	 * @return returns whether gparray contains already gp  
	 */
	private Boolean checkForDuplicates(GeoPoint gp, GeoPoint[][] gparray){
		Boolean isDuplicate = false;
		GeoPoint arrayGP;
		for (int i =0; i < gparray[0].length-1;i++){
			arrayGP = new GeoPoint(gparray[0][i].getLatitudeE6(),gparray[0][i].getLongitudeE6());
			if(gp.getLatitudeE6() == arrayGP.getLatitudeE6() && gp.getLongitudeE6() == arrayGP.getLongitudeE6()){
				isDuplicate = true;
			}
			}
		return isDuplicate;
	}

	@Override
	public int size() {
		return list.size() + (gpsMarker == null ? 0 : 1);
	}

	@Override
	protected OverlayItem createItem(int i) {
		if (i == list.size())
			return gpsMarker;
		else
			return list.get(i);
	}

	@Override
	public boolean onTap(int i) {
 
		if (i == list.size()) {
			final GeoPoint gpsPoint = this.gpsMarker.getPoint();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Wollen Sie ihren aktuellen Standort als Startpunkt setzen?")
					.setCancelable(true)
					.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// transform the GpsMarker into a regular mapMarker on Position 0
							Node gpsStartnode = Node.createNode("gpsLocation", gpsPoint);
							useGps = true;
							nodeModel.addBeginning(gpsStartnode);
							loadFromModel();
						}
					})
					.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					}).create().show();
		} else {
			Intent intent = new Intent(context, EditNodeScreen.class);
			intent.putExtra("node", nodeModel.getNodeVector().get(i));
			intent.putExtra("index", i);
			((Activity) context).startActivityForResult(intent, REQUEST_CODE_ITEM_OVERLAY);
		}

		return false;
	}

	public void addMarkerToMap(Node node) {
	//	NodeDrawable dm = new NodeDrawable(nodeParameters, node.getName());
		OverlayItem overlayitem = new OverlayItem(node.getGeoPoint(), null, null, defaultDrawable);
		list.add(overlayitem);
		populate();
		requestRedraw();
		updateIcons();
		
		
		}

	public void clear() {
		nodeModel.clear();
		list.clear();
		useGps = false;
		populate();
	}

	@Override
	protected void drawOverlayBitmap(Canvas canvas, Point drawPosition, Projection projection, byte drawZoomLevel) {
		// When the zoom level changes update all markers
		//final int scaled = Math.max(8, MARKER_BOUNDS / 2 * drawZoomLevel);
		final int scaled = 10;
		final Rect bounds = new Rect(-scaled, -scaled, scaled, scaled);
		if(lastZoomLevel != drawZoomLevel) {
			for(int i=0; i < list.size(); i++) {
				Drawable d = list.get(i).getMarker();
				d.setBounds(bounds);
			}

			lastZoomLevel = drawZoomLevel;
		}
		super.drawOverlayBitmap(canvas, drawPosition, projection, drawZoomLevel);
	}



	public void updateIcons() {
//		if (!algorithmInfo.sourceIsTarget() && list.size() > 0) {
//            for (int i = 1; i < list.size() - 1; i++) {
//	            NodeDrawable d = (NodeDrawable) list.get(i).getMarker();
//                d.setColor(Color.BLUE);
//            }
//
//			((NodeDrawable)list.get(list.size() - 1).getMarker()).setColor(Color.RED);
//			((NodeDrawable)list.get(0).getMarker()).setColor(Color.GREEN);
//		}
//
//		// Also update the zoom level
//		lastZoomLevel = Byte.MIN_VALUE;
//		requestRedraw();
		if (!algorithmInfo.sourceIsTarget() && list.size() > 0) {
            for (int i = 1; i < list.size() - 1; i++) {
	            Drawable d = (Drawable) list.get(i).getMarker();
               d =  context.getResources().getDrawable(R.drawable.markericon);
               list.get(i).setMarker(d);
            }

			Drawable dend =list.get(list.size() - 1).getMarker();
			dend = context.getResources().getDrawable(R.drawable.markerendger);
			list.get(list.size() - 1).setMarker(dend);
			Drawable dstart = list.get(0).getMarker();
			dstart = context.getResources().getDrawable(R.drawable.markerstart);
			list.get(0).setMarker(dstart);
		}

		// Also update the zoom level
		lastZoomLevel = Byte.MIN_VALUE;
		requestRedraw();
	}

	private void updateGpsMarker(GeoPoint geoPoint) {
		// If we have a valid point
		if (geoPoint != null) {
			// Then create a new Overlayitem or update the old one
			if (gpsMarker == null)
				gpsMarker = new OverlayItem(geoPoint, null, null, gpsDrawable);
			else
				gpsMarker.setPoint(geoPoint);
			
		} else {
			gpsMarker = null;
		}
		populate();
	}

	@Override
	public void onLocationChanged(Location location) {
		GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
		gpsMarker.setPoint(geoPoint);
		if(useGps && !list.isEmpty()) {
			list.get(0).setPoint(geoPoint);
			nodeModel.get(0).setGeoPoint(geoPoint);
		}
		populate();
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {
	}

	@Override
	public void onProviderEnabled(String s) {
	}

	@Override
	public void onProviderDisabled(String s) {
		gpsMarker = null;
	}


}