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
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.ItemOverlay;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.ItemOverlayDrawable;

import org.mapsforge.android.maps.*;

public class MapScreen extends MapActivity {
    public MapView mapView;
    private ArrayWayOverlay wayOverlay;
    private Session session;
    //private ItemOverlay itemizedoverlay;
    private ItemOverlayDrawable itemizedoverlay;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Session.IDENTIFIER, session);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If we get created for the first time we get our data from the intent
        if(savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }

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

//        itemizedoverlay = new ItemOverlay(this,session.getNodeModel(),session.getSelectedAlgorithm());
//        mapView.getOverlays().add(itemizedoverlay);
        itemizedoverlay = new ItemOverlayDrawable(this,session.getNodeModel(), mapView);
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
              case R.id.nodelist:
                // generates an intent from the class NodeListScreen
                Intent myIntent = new Intent(this, NodelistScreen.class);
                myIntent.putExtra(Session.IDENTIFIER,session);
                startActivityForResult(myIntent, 0);

                return true;
            case R.id.reset:
            			// clear nodes
                       	itemizedoverlay.clear();
                       	// clear path
                        wayOverlay.clear();
                        return true;
            case R.id.calculate:
                addPathToMap();
            	return true;
            	
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        session.setNodeModel((NodeModel) data.getExtras().getSerializable(NodeModel.IDENTIFIER));
        itemizedoverlay.setNodeModel(session.getNodeModel());
    }

    public void addPathToMap() {
        wayOverlay.clear();
        OverlayWay way;
        NodeModel nodeModel = session.getNodeModel();
        GeoPoint[][] geoArray = new GeoPoint[1][nodeModel.size()];
        for (int i = 0; i < nodeModel.size(); i++) {
            geoArray[0][i] = nodeModel.get(i).getGeoPoint();
        }
        way = new OverlayWay(geoArray);
        wayOverlay.addWay(way);
    }
}
