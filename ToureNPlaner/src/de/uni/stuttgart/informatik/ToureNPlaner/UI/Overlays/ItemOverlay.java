package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.SessionData;
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

    public ItemOverlay(Context context) {
        // ColorDrawable is just a workaround until the icons are loaded
        super(boundCenterBottom(new ColorDrawable()));
        setupIcons(context);
    }

    @Override
    public boolean onLongPress(GeoPoint geoPoint, MapView mapView) {
        String markerName = "Marker Nr. "
                + String.valueOf(NodeModel.getInstance().size() + 1);
        Node node = new Node(markerName, geoPoint);
        addMarkerToMap(node);
        NodeModel.getInstance().addNodeToVector(node);
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
        updateIcons();
    }

    private void setupIcons(Context context) {
        // initialize the Icons and ItemOverlays
        iconStart = boundCenterBottom(context.getResources().getDrawable(R.drawable.startmarker));
        iconNormal = boundCenterBottom(context.getResources().getDrawable(R.drawable.marker));
        iconEnd = boundCenterBottom(context.getResources().getDrawable(R.drawable.endmarker));
    }

    private void updateIcons() {
        if (SessionData.Instance.getAlgorithmHasStarAndEndMarker()) {
            for (int i = 1; i < list.size() - 1; i++) {
                list.get(i).setMarker(iconNormal);
            }
            list.get(0).setMarker(iconStart);
            list.get(list.size() - 1).setMarker(iconEnd);
        }
    }
}
