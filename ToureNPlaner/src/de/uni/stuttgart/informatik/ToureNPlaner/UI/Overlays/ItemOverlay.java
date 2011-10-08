package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.AlgorithmInfo;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.OverlayItem;

import java.util.ArrayList;

public class ItemOverlay extends ItemizedOverlay<OverlayItem> {
    private ArrayList<OverlayItem> list = new ArrayList<OverlayItem>();
    private Drawable iconStart;
    private Drawable iconNormal;
    private Drawable iconEnd;
    private NodeModel nodeModel;
    private final AlgorithmInfo algorithmInfo;

    public ItemOverlay(Context context, NodeModel nodeModel, AlgorithmInfo algorithmInfo) {
        // ColorDrawable is just a workaround until the icons are loaded
        super(boundCenterBottom(new ColorDrawable()));
        this.nodeModel = nodeModel;
        this.algorithmInfo = algorithmInfo;
        setupIcons(context);
        loadFromModel();
    }

    public void setNodeModel(NodeModel nodeModel) {
        this.nodeModel = nodeModel;
        loadFromModel();
    }

    private void loadFromModel() {
        list.clear();
        for(int i = 0;i<nodeModel.size();i++) {
            addMarkerToMap(nodeModel.get(i));
        }
        updateIcons();
        requestRedraw();
    }

    @Override
    public boolean onLongPress(GeoPoint geoPoint, MapView mapView) {
        String markerName = "Marker Nr. "
                + String.valueOf(nodeModel.size() + 1);
        Node node = new Node(markerName, geoPoint.getLatitude(),geoPoint.getLongitude());
        addMarkerToMap(node);
        nodeModel.addNodeToVector(node);
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

    public void addMarkerToMap(Node node) {
        OverlayItem overlayitem = new OverlayItem(node.getGeoPoint(), node.getName(), "", iconNormal);
        list.add(overlayitem);
    }

    private void setupIcons(Context context) {
        // initialize the Icons and ItemOverlays
        iconStart = boundCenterBottom(context.getResources().getDrawable(R.drawable.startmarker));
        iconNormal = boundCenterBottom(context.getResources().getDrawable(R.drawable.marker));
        iconEnd = boundCenterBottom(context.getResources().getDrawable(R.drawable.endmarker));
    }

    private void updateIcons() {
        if (!algorithmInfo.sourceIsTarget() &&
                list.size() > 0) {
            for (int i = 1; i < list.size() - 1; i++) {
                list.get(i).setMarker(iconNormal);
            }
            list.get(list.size() - 1).setMarker(iconEnd);
            list.get(0).setMarker(iconStart);
        } else {
            for(int i=0;i < list.size();i++) {
                list.get(i).setMarker(iconNormal);
            }
        }
    }
}
