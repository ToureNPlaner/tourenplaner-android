/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.*;
import android.hardware.Sensor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.TBTResult;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.*;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.*;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.CustomTileDownloader;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.FastWayOverlay;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays.NodeOverlay;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;
import org.mapsforge.android.maps.mapgenerator.tiledownloader.MapnikTileDownloader;
import org.mapsforge.android.maps.mapgenerator.tiledownloader.OpenCycleMapTileDownloader;
import org.mapsforge.core.GeoPoint;
import org.mapsforge.map.reader.header.FileOpenResult;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static de.uni.stuttgart.informatik.ToureNPlaner.UI.Formatter.formatDistance;
import static de.uni.stuttgart.informatik.ToureNPlaner.UI.Formatter.formatTime;

public class MapScreen extends MapActivity implements Session.Listener {
	MapView mapView;
	private FastWayOverlay fastWayOverlay;
	private Session session;
	public static final int REQUEST_NODEMODEL = 0;
	public static final int REQUEST_NODE = 1;
	public static final int REQUEST_CONSTRAINTS = 2;
	NodeOverlay nodeOverlay;
	private SimpleNetworkHandler simplehandler = null;
	private SessionAwareHandler sesshandler = null;
	private LocationManager locManager;
	private MapScreenPreferences.Instant instantRequest;
	private Toast messageToast;

	private GpsListener gpsListener;

	private final ArrayList<RequestNN> requestList = new ArrayList<RequestNN>();


	private final Observer tbtrequestListener = new Observer() {
		@Override
		public void onCompleted(AsyncHandler caller, Object object) {
			//Log.d("tp", "tbt request completed: " +object.toString());
			sesshandler = null;
			Edit edit = new TBTResultEdit(session, (TBTResult) object);
			//Log.d("tp", "tbt response: " + object.toString());
			edit.perform();
			setSupportProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onError(AsyncHandler caller, Object object) {
			Log.d("tp", "error: " + object.toString());
			simplehandler = null;
			setSupportProgressBarIndeterminateVisibility(false);
			Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
		}
	};

	private final Observer requestListener = new Observer() {
		@Override
		public void onCompleted(AsyncHandler caller, Object object) {
			sesshandler = null;
			Edit edit = new SetResultEdit(session, (Result) object);
			edit.perform();
			setSupportProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onError(AsyncHandler caller, Object object) {
			sesshandler = null;
			setSupportProgressBarIndeterminateVisibility(false);
			Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
		}
	};

	private final Observer nnsListener = new Observer() {
		@Override
		public void onCompleted(AsyncHandler caller, Object object) {
			Edit edit = new UpdateNNSEdit(session, ((RequestNN) caller).getNode(), ((Result) object).getPoints().get(0).getGeoPoint());
			edit.perform();
			requestList.remove(caller);
		}

		@Override
		public void onError(AsyncHandler caller, Object object) {
			Toast.makeText(getApplicationContext(), object.toString(),
					Toast.LENGTH_LONG).show();
			requestList.remove(caller);
		}
	};

	private final Observer geoCodingListener = new Observer() {
		@Override
		public void onCompleted(AsyncHandler caller, Object object) {
			mapView.setCenter(((GeoCodingHandler.GeoCodingResult) object).location);
		}

		@Override
		public void onError(AsyncHandler caller, Object object) {
			Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Session.IDENTIFIER, session);
		gpsListener.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean isFirstStart = savedInstanceState == null;
		// If we get created for the first time we get our data from the intent
		Bundle data = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
		session = (Session) data.getSerializable(Session.IDENTIFIER);

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setClickable(true);
		mapView.setLongClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.getFileSystemTileCache().setPersistent(false);

		getSupportActionBar().setSubtitle(session.getSelectedAlgorithm().toString());
		initializeHandler();

		setupWayOverlay();

		setupGPS(savedInstanceState, isFirstStart);

		if (session.getResult() != null) {
			updateDistancePopup();
			if (session.getResult().getPoints() != null) {
				mapView.setCenter(session.getResult().getPoints().get(0).getGeoPoint());
			}
		}
		if (!mapView.getMapPosition().isValid()) {
			mapView.setCenter(new GeoPoint(51.33, 10.45));
		}

		mapView.getOverlays().add(nodeOverlay);

		session.registerListener(NodeOverlay.class, nodeOverlay);
		session.registerListener(MapScreen.class, this);
	}

	private String tileServer;
	private String offlineMapLocation;
	private MapScreenPreferences.MapGenerator mapGenerator;

	private void setupMapView(SharedPreferences preferences) {
		String newTileServer = preferences.getString("tile_server", MapScreenPreferences.defaultTileServer);
		String newOfflineMapLocation = preferences.getString("offline_map_location", MapScreenPreferences.defaultMapLocation);
		MapScreenPreferences.MapGenerator newMapGenerator = MapScreenPreferences.MapGenerator.valueOf(preferences.getString("map_generator", MapScreenPreferences.MapGenerator.MAPQUEST.name()));

		if (mapGenerator != newMapGenerator) {
			switch (newMapGenerator) {
				case MAPQUEST:
					try {
						mapView.setMapGenerator(new CustomTileDownloader(new URL("http://otile1.mqcdn.com/tiles/1.0.0/osm/%1$d/%2$d/%3$d.jpg"), (byte) 18));
					} catch (MalformedURLException e) {
						// shouldn't happen
					}
					break;
				case MAPNIK:
					mapView.setMapGenerator(new MapnikTileDownloader());
					break;
				case OPENCYCLE:
					mapView.setMapGenerator(new OpenCycleMapTileDownloader());
					break;
			}
		}
		if (newMapGenerator == MapScreenPreferences.MapGenerator.CUSTOM &&
				((newMapGenerator != mapGenerator) || !tileServer.equals(newTileServer))) {
			try {
				mapView.setMapGenerator(new CustomTileDownloader(new URL(newTileServer), (byte) 17));
			} catch (MalformedURLException e) {
				mapView.setMapGenerator(new MapnikTileDownloader());
				Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			}
		} else if (newMapGenerator == MapScreenPreferences.MapGenerator.FILE &&
				((newMapGenerator != mapGenerator) || !offlineMapLocation.equals(newOfflineMapLocation))) {
			if (mapGenerator != newMapGenerator) {
				mapView.setMapGenerator(new DatabaseRenderer());
			}
			FileOpenResult result;
			try {
				result = mapView.setMapFile(new File(newOfflineMapLocation));
			} catch (Exception e) {
				result = new FileOpenResult(getResources().getString(R.string.map_file_error));
			}
			if (!result.isSuccess()) {
				mapView.setMapGenerator(new MapnikTileDownloader());
				Toast.makeText(this, result.getErrorMessage(), Toast.LENGTH_LONG).show();
			}
		}

		((TextView) findViewById(R.id.copyright)).setText(
				newMapGenerator == MapScreenPreferences.MapGenerator.MAPQUEST ? R.string.mapquest_copyright : R.string.osm_copyright);

		tileServer = newTileServer;
		mapGenerator = newMapGenerator;
		offlineMapLocation = newOfflineMapLocation;
	}

	private void setupGPS(Bundle savedInstanceState, boolean isFirstStart) {
		Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		GeoPoint gpsGeoPoint = null;

		if (loc != null) {
			gpsGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
		}

		// setting up LocationManager and set MapFocus on lastknown GPS-Location
		if (isFirstStart && gpsGeoPoint != null) {
			mapView.getController().setCenter(gpsGeoPoint);
		}

		gpsListener = new GpsListener(this, savedInstanceState, gpsGeoPoint, session);
		nodeOverlay = new NodeOverlay(this, session, gpsGeoPoint);
	}

	private void setupWayOverlay() {
		Path p = new Path();
		p.moveTo(4.f, 0.f);
		p.lineTo(0.f, -4.f);
		p.lineTo(8.f, -4.f);
		p.lineTo(12.f, 0.f);
		p.lineTo(8.f, 4.f);
		p.lineTo(0.f, 4.f);

		Paint fastWayOverlayColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		fastWayOverlayColor.setStyle(Paint.Style.STROKE);
		fastWayOverlayColor.setColor(Color.BLUE);
		fastWayOverlayColor.setAlpha(160);
		fastWayOverlayColor.setStrokeWidth(5.f);
		fastWayOverlayColor.setStrokeJoin(Paint.Join.ROUND);
		fastWayOverlayColor.setPathEffect(new ComposePathEffect(
				new PathDashPathEffect(p, 12.f, 0.f, PathDashPathEffect.Style.ROTATE),
				new CornerPathEffect(30.f)));

		// create the WayOverlay and add the ways
		this.fastWayOverlay = new FastWayOverlay(session, fastWayOverlayColor);
		mapView.getOverlays().add(this.fastWayOverlay);
		Result result = session.getResult();
		if (result != null) {
			addPathToMap(result.getWay());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.mapscreenmenu, menu);
		setupSearchMenu(menu.findItem(R.id.search));
		setupGpsMenu(menu.findItem(R.id.gps));
		setupToggleCompassMenu(menu.findItem(R.id.togglecompass));
		return true;
	}

	private void setupToggleCompassMenu(MenuItem item) {
		// TODO: always enabled at start? Remember last setting?
		item.setChecked(true);
		Sensor sensorMag = null;
		Sensor sensorGrav = null;
		List<Sensor> sensors = gpsListener.sensorMgr
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		Log.d("tp", "Found " + sensors.size() + " accelerometers");
		if (sensors.size() > 0) {
			sensorGrav = sensors.get(0);
			gpsListener.setSensorGrav(sensorGrav);
		}

		sensors = gpsListener.sensorMgr
				.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
		Log.d("tp", "Found " + sensors.size() + " magnetic field sensors");
		if (sensors.size() > 0) {
			sensorMag = sensors.get(0);
			gpsListener.setSensorMag(sensorMag);
		}

		gpsListener.sensorMgr.registerListener(gpsListener, sensorGrav,
				GpsListener.sensordelay);
		gpsListener.sensorMgr.registerListener(gpsListener, sensorMag,
				GpsListener.sensordelay);

		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (item.isChecked()) {
					gpsListener.sensorMgr.unregisterListener(gpsListener);
					// TODO: replace arrow with circle
					nodeOverlay.setDirection(0);
					nodeOverlay.requestRedraw();
				} else {
					gpsListener.sensorMgr.registerListener(gpsListener,
							gpsListener.getSensorGrav(), GpsListener.sensordelay);
					gpsListener.sensorMgr.registerListener(gpsListener,
							gpsListener.getSensorMag(), GpsListener.sensordelay);
				}
				item.setChecked(!item.isChecked());
				return true;
			}
		});
	}

	private void setupGpsMenu(MenuItem item) {
		item.setChecked(gpsListener.isFollowing());
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				item.setChecked(!item.isChecked());
				gpsListener.setFollowing(item.isChecked());
				MapScreen.this.supportInvalidateOptionsMenu();
				return true;
			}
		});
	}

	private MenuItem search = null;

	private void setupSearchMenu(MenuItem searchMenu) {
		search = searchMenu;
		searchMenu.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				final EditText field = (EditText) item.getActionView().findViewById(R.id.search_field);
				field.requestFocus();
				InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				keyboard.toggleSoftInput(0, 0);
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				EditText field = (EditText) item.getActionView().findViewById(R.id.search_field);
				InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				keyboard.hideSoftInputFromWindow(field.getWindowToken(), 0);
				return true;
			}
		});
		searchMenu.getActionView().findViewById(R.id.search_field).setOnKeyListener(new View.OnKeyListener() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					EditText field = ((EditText) search.getActionView().findViewById(R.id.search_field));
					search.collapseActionView();
					Projection projection = mapView.getProjection();
					GeoPoint topLeft = projection.fromPixels(0, 0);
					GeoPoint bottomRight = projection.fromPixels(mapView.getWidth(), mapView.getHeight());
					GeoCodingHandler.createDefaultHandler(geoCodingListener, field.getText().toString(),
							new RectF((float) topLeft.getLongitude(), (float) topLeft.getLatitude(),
									(float) bottomRight.getLongitude(), (float) bottomRight.getLatitude())
					).execute();
				}
				return false;
			}
		});
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (search != null) {
				search.expandActionView();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.nodelist:
				myIntent = new Intent(this, InfoScreen.class);
				myIntent.putExtra(Session.IDENTIFIER, session);
				startActivityForResult(myIntent, REQUEST_NODEMODEL);
				return true;
			case R.id.reset:
				if (sesshandler != null) {
					sesshandler.cancel(true);
					setSupportProgressBarIndeterminateVisibility(false);
					sesshandler = null;
				}
				Edit edit = new ClearEdit(session);
				edit.perform();
				return true;
			case R.id.calculate:
				performRequest(true);
				return true;
			case R.id.tbt:
				performtbtRequest();
				session.getTBTNavigation().startTBT();
				return true;
			case R.id.gps:
				GeoPoint pos = nodeOverlay.getGpsPosition();
				if (pos != null)
					mapView.getController().setCenter(nodeOverlay.getGpsPosition());
				return true;
			case R.id.algorithm_constraints:
				myIntent = new Intent(this, AlgorithmConstraintsScreen.class);
				myIntent.putExtra(AlgorithmConstraintsScreen.IDENTIFIER, session.getConstraints());
				startActivityForResult(myIntent, REQUEST_CONSTRAINTS);
				return true;
			case R.id.settings:
				startActivity(new Intent(this, MapScreenPreferences.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void performtbtRequest() {
		if (session.getResult() == null) {
			return;
		}
		if (simplehandler != null)
			simplehandler.cancel(true);

		try {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String tbtip = preferences.getString("tbtip", MapScreenPreferences.defaulttbtip);

			simplehandler = session.performtbtRequest(tbtrequestListener, tbtip);
			setSupportProgressBarIndeterminateVisibility(true);
		} catch (Session.RequestInvalidException e) {
			if (messageToast != null) {
				messageToast.setText(e.getMessage());
				messageToast.show();
			} else {
				messageToast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
				messageToast.show();
			}
			simplehandler = null;
		}
	}

	private void performRequest(boolean force) {
		if (sesshandler != null && !force)
			return;

		if (sesshandler != null)
			sesshandler.cancel(true);

		try {
			sesshandler = session.performRequest(requestListener, force);
			setSupportProgressBarIndeterminateVisibility(true);
		} catch (Session.RequestInvalidException e) {
			if (messageToast != null) {
				messageToast.setText(e.getMessage());
				messageToast.show();
			} else {
				messageToast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
				messageToast.show();
			}
			sesshandler = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Edit edit;
		switch (requestCode) {
			case REQUEST_NODEMODEL:
				switch (resultCode) {
					case RESULT_OK:
						edit = new ChangeNodeModelEdit(session, (ArrayList<Node>) data.getExtras().getSerializable(NodeModel.IDENTIFIER));
						edit.perform();
						break;
				}
				break;
			case REQUEST_NODE:
				switch (resultCode) {
					case RESULT_OK:
						edit = new UpdateNodeEdit(session, data.getExtras().getInt("index"), (Node) data.getSerializableExtra("node"));
						edit.perform();
						break;
					case EditNodeScreen.RESULT_DELETE:
						edit = new RemoveNodeEdit(session, data.getExtras().getInt("index"));
						edit.perform();
						break;
				}
				break;
			case REQUEST_CONSTRAINTS:
				switch (resultCode) {
					case RESULT_OK:
						ArrayList<Constraint> constraints = (ArrayList<Constraint>) data.getExtras().get(AlgorithmConstraintsScreen.IDENTIFIER);
						edit = new ChangeConstraintsEdit(session, constraints);
						edit.perform();
				}
				break;
		}
	}

	public void addPathToMap(int[][] points) {
		fastWayOverlay.clear();
		if (points != null && points.length > 0) {
			fastWayOverlay.initWay(points);
		}
	}

	@SuppressWarnings("unchecked")
	public void performNNSearch(Node node) {
		requestList.add((RequestNN) new RequestNN(nnsListener, session, node).execute());
	}

	@SuppressWarnings("deprecation")
	private void initializeHandler() {
		sesshandler = (RequestHandler) getLastNonConfigurationInstance();

		if (sesshandler != null) {
			sesshandler.setListener(requestListener);
			setSupportProgressBarIndeterminateVisibility(true);
		} else {
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public Object onRetainNonConfigurationInstance() {
		return sesshandler;
	}

	@Override
	protected void onPause() {
		locManager.removeUpdates(gpsListener);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		//-----get mapScreen_Preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		instantRequest = MapScreenPreferences.Instant.valueOf(preferences.getString("instant_request", MapScreenPreferences.Instant.ALWAYS.name()));

		this.supportInvalidateOptionsMenu();

		setupMapView(preferences);

		try {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, gpsListener);
		} catch (IllegalArgumentException e) {
			// happens on emulator
		}
	}

	@Override
	protected void onDestroy() {
		nodeOverlay.setMapScreen(null);

		mapView.getOverlays().remove(nodeOverlay);

		session.removeListener(NodeOverlay.class);
		session.removeListener(MapScreen.class);

		if (sesshandler != null)
			sesshandler.setListener(null);

		for (RequestNN request : requestList) {
			request.setListener(null);
		}
		super.onDestroy();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.algorithm_constraints).setVisible(
				!session.getSelectedAlgorithm().getConstraintTypes().isEmpty());
		if (instantRequest == MapScreenPreferences.Instant.NEVER) {
			menu.findItem(R.id.calculate).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		} else {
			menu.findItem(R.id.calculate).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}

		menu.findItem(R.id.gps).setVisible(gpsListener.isEnabled());
		menu.findItem(R.id.gps).setIcon(gpsListener.isFollowing() ? R.drawable.location_enabled : R.drawable.location_disabled);
		return true;
	}


	@Override
	public void onChange(final Session.Change change) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (change.isResultChange()) {
					if (session.getResult() == null) {
						addPathToMap(null);
						((TextView) findViewById(R.id.overlay)).setText("");
					} else {
						updateDistancePopup();
						addPathToMap(session.getResult().getWay());
						String msg = session.getResult().getMisc().getMessage();
						if (msg != null && !msg.equals("")) {
							if (messageToast != null)
								messageToast.cancel();
							messageToast = Toast.makeText(MapScreen.this, msg, Toast.LENGTH_LONG);
							messageToast.show();
						}
					}
				}
				if (change.isDndChange() && instantRequest == MapScreenPreferences.Instant.ALWAYS) {
					performRequest(false);
				}
				if (change.isModelChange()) {
					if (change.isAddChange()) {
						// Important! Try to perform the NNS Search before, notifying any one else about the change.
						// else through HTTP pipelining the NNS might be performed after the request!
						performNNSearch(change.getNode());
						mapView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
					} else {
						Edit edit = new SetResultEdit(session, null);
						edit.perform();
					}
					if (instantRequest != MapScreenPreferences.Instant.NEVER) {
						performRequest(true);
					}
				}
			}
		});
	}

	private void updateDistancePopup() {
		((TextView) findViewById(R.id.overlay)).setText(
				formatDistance(this, session.getResult().getMisc().getDistance()) + " " +
						formatTime(this, session.getResult().getMisc().getTime()));
	}
}
