package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.HapticFeedbackConstants;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.NodePreferences;
import org.mapsforge.android.maps.*;

import java.util.ArrayList;

public class ItemOverlayDrawable extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> list = new ArrayList<OverlayItem>();
	private NodeModel nodeModel;
	private MapView mapview;
	private final AlgorithmInfo algorithmInfo = null;
	public static final int RequestCodeItemOverlay = 1;
	private Context context;
	private Point offset = new Point();
	private MapView cMap;
	protected String Tag = "default";
	public Intent ItemOverlayIntent;
	private int mapViewWidth = 0;
	private int mapViewHeight = 0;
	private int xCenterOffset = 0;
	private int yCenterOffset = 0;

	public ItemOverlayDrawable(Context context,NodeModel nodeModel,MapView mapview,int resId, int offsetX, int offsetY) {
		// ColorDrawable is just a workaround until the icons are loaded
		super(boundCenterBottom(new ColorDrawable()));
		this.nodeModel = nodeModel;
		this.mapview = mapview;
		
		cMap = mapview;
		this.offset.x = offsetX;
		offset.y = offsetY;
		this.context = context;

		mapViewHeight = mapview.getHeight();
		mapViewWidth  = mapview.getWidth();

		xCenterOffset = mapViewWidth/2;
		yCenterOffset = mapViewHeight/2;
		loadFromModel();
	}

	public void setNodeModel(NodeModel nodeModel) {
		this.nodeModel = nodeModel;
		loadFromModel();
	}

	private void loadFromModel() {
		list.clear();
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
	public boolean onTap(int i) {
		 ItemOverlayIntent = new Intent(context, NodePreferences.class);
		 ItemOverlayIntent.putExtra("node", nodeModel.getNodeVector().get(i));
		 ItemOverlayIntent.putExtra("index", i);
        ((Activity) context).startActivityForResult(ItemOverlayIntent,RequestCodeItemOverlay);
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
		requestRedraw();
	}
	
	
	protected boolean tapOnMe(GeoPoint p, MapView m){

		final Projection pr = m.getProjection();
		int minX = xCenterOffset + offset.x;
		int minY = yCenterOffset + offset.y;
		int maxX = minX + 9;
		int maxY = minY + 9;

		Point pt = pr.toPixels(p, null);

		if(pt.x >= minX && pt.y >= minY &&
		pt.x <= maxX && pt.y <= maxY){
		return true;
		}

		return false;
		}

	public void updateIcons() {
		// if (!algorithmInfo.sourceIsTarget() &&
		// list.size() > 0) {
int bound = 0;
		if (list.size() > 0) {
			bound = (int) (((DrawableMarker) list.get(0).getMarker()).getBound() *2.25);
			for (int i = 1; i < list.size() - 1; i++) {
				((DrawableMarker) list.get(i).getMarker()).setColor(Color.BLUE);
				((DrawableMarker) list.get(i).getMarker()).SetIndex(i + 1);
			
				((DrawableMarker) list.get(i).getMarker()).setBounds(0, 0, bound,bound);
							}
			((DrawableMarker) list.get(list.size() - 1).getMarker()).setColor(Color.RED);
			((DrawableMarker) list.get(list.size() - 1).getMarker()).SetIndex(list.size());
			((DrawableMarker) list.get(list.size() - 1).getMarker()).setBounds(0, 0, bound,bound);
			((DrawableMarker) list.get(0).getMarker()).setColor(Color.GREEN);
			((DrawableMarker) list.get(0).getMarker()).SetIndex(1);
			((DrawableMarker) list.get(0).getMarker()).setBounds(0, 0, bound,bound);
			}
	}
	// }

	
}