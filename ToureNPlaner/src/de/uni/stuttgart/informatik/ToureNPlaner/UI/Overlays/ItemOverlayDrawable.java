package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import java.util.ArrayList;

import android.view.HapticFeedbackConstants;
import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.OverlayItem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;

public class ItemOverlayDrawable extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> list = new ArrayList<OverlayItem>();
	private NodeModel nodeModel;
	private MapView mapview;
	private final AlgorithmInfo algorithmInfo = null;

	public ItemOverlayDrawable(NodeModel nodeModel,MapView mapview) {
		// ColorDrawable is just a workaround until the icons are loaded
		super(boundCenterBottom(new ColorDrawable()));
		this.nodeModel = nodeModel;
		this.mapview = mapview;
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
		Node node = new Node(markerName, geoPoint.getLatitude(),
				geoPoint.getLongitude());
		nodeModel.addNodeToVector(node);
		addMarkerToMap(node);
		
		mapView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

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
	protected boolean onTap(int index) {
		// OverlayItem item = list.get(index);
		// AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		Log.v("tap", "tap triggered");
		// dialog.setTitle(item.getTitle());
		// dialog.setMessage(item.getSnippet());
		// dialog.show();
		return true;
	}

	public void addMarkerToMap(Node node) {
		DrawableMarker dm = new DrawableMarker(mapview, node.getGeoPoint(),true);
		dm.SetIndex(nodeModel.size());
		OverlayItem overlayitem = new OverlayItem(node.getGeoPoint(), "", "",
				dm);
		list.add(overlayitem);
	}
	
	public void clear() {
		nodeModel.clear();
		list.clear();
		requestRedraw();
	}

	private void updateIcons() {
		// if (!algorithmInfo.sourceIsTarget() &&
		// list.size() > 0) {

		if (list.size() > 0) {
			for (int i = 1; i < list.size() - 1; i++) {
				((DrawableMarker) list.get(i).getMarker()).setColor(Color.BLUE);
				((DrawableMarker) list.get(i).getMarker()).SetIndex(i + 1);
			}
			((DrawableMarker) list.get(list.size() - 1).getMarker())
					.setColor(Color.RED);
			((DrawableMarker) list.get(list.size() - 1).getMarker())
					.SetIndex(list.size());
			((DrawableMarker) list.get(0).getMarker()).setColor(Color.GREEN);
			((DrawableMarker) list.get(0).getMarker()).SetIndex(1);
			}
	}
	// }
}