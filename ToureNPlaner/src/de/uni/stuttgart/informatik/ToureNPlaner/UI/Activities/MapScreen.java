package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.ItemOverlay;
import org.mapsforge.android.maps.*;

import java.util.Vector;

public class MapScreen extends MapActivity {
    public MapView mapView;
    private Vector<OverlayItem> overlayItemVector = new Vector<OverlayItem>();
    private ArrayWayOverlay wayOverlay;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting properties of the mapview
        mapView = new MapView(this);
        mapView.setClickable(true);
        mapView.setLongClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMapViewMode(MapViewMode.MAPNIK_TILE_DOWNLOAD);
        // mapView.setMapFile("/sdcard/berlin.map");
        mapView.setFpsCounter(true);
        mapView.setMemoryCardCachePersistence(true);
        mapView.setMemoryCardCacheSize(1000);
        setContentView(mapView);

        ItemOverlay itemizedoverlay = new ItemOverlay(this);
        mapView.getOverlays().add(itemizedoverlay);

        setupWayOverlay();

        // set focus of MapScreen
        GeoPoint geoPoint1 = new GeoPoint(52.514446, 13.350150); // Berlin
        mapView.getController().setCenter(geoPoint1);
    }

    private void setupWayOverlay() {
        // ----------------WayOverlay Properties-----------------
        // create the default paint objects for overlay ways
        Paint wayDefaultPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayDefaultPaintFill.setStyle(Paint.Style.STROKE);
        wayDefaultPaintFill.setColor(Color.BLUE);
        wayDefaultPaintFill.setAlpha(160);
        wayDefaultPaintFill.setStrokeWidth(7);
        wayDefaultPaintFill.setStrokeJoin(Paint.Join.ROUND);
        wayDefaultPaintFill.setPathEffect(new DashPathEffect(new float[]{20,
                20}, 0));
        Paint wayDefaultPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayDefaultPaintOutline.setStyle(Paint.Style.STROKE);
        wayDefaultPaintOutline.setColor(Color.BLUE);
        wayDefaultPaintOutline.setAlpha(128);
        wayDefaultPaintOutline.setStrokeWidth(7);
        wayDefaultPaintOutline.setStrokeJoin(Paint.Join.ROUND);

        // create an individual paint object for an overlay way
        Paint wayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayPaint.setStyle(Paint.Style.FILL);
        wayPaint.setColor(Color.YELLOW);
        wayPaint.setAlpha(192);
        // create the WayOverlay and add the ways
        wayOverlay = new ArrayWayOverlay(wayDefaultPaintFill,
                wayDefaultPaintOutline);
        mapView.getOverlays().add(wayOverlay);
    }

    // ----------------Menu-----------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mapscreenmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.calculate:
                // generates an intent from the class NodeListScreen
                Intent myIntent = new Intent(this, NodelistScreen.class);
                startActivity(myIntent);

                return true;
            case R.id.reset:
                addPathToMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPathToMap() {
        wayOverlay.clear();
        OverlayWay way;
        GeoPoint[][] geoArray = new GeoPoint[1][NodeModel.getInstance().size()];
        for (int i = 0; i < NodeModel.getInstance().size(); i++) {
            geoArray[0][i] = NodeModel.getInstance().get(i).getGeoPoint();
        }
        way = new OverlayWay(geoArray);
        wayOverlay.addWay(way);
    }

    /*public void printAllMarkersToMap() {
        // initialize components of ItemOverlay
        overlayItemVector.clear();
        //TODO update view when pressing "backbutton" on "NodeListScreen" Activitiy
        Log.v("clearMap", "ClearMap");
        for (int i = 0; i < NodeModel.getInstance().size(); i++) {
            Log.v("add marker", "add marker");
            addMarkerToMap(NodeModel.getInstance().get(i));
        }
    }*/

}
