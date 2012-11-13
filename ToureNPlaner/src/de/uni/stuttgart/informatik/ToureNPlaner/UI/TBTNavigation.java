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

package de.uni.stuttgart.informatik.ToureNPlaner.UI;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.SimpleNetworkHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.TurnByTurnHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen.MapScreen;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.CoordinateTools;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TBTNavigation implements TextToSpeech.OnInitListener, Serializable {
	private static TextToSpeech tts;
	private ArrayList<ArrayList<Node>> tbtway = null;

	List<OverlayItem> markings;
	private MapScreen ms;

	private class turnmarker extends Drawable {

		private Paint paint;

		public turnmarker() {
			this.paint = new Paint();
			this.paint.setAlpha(128);
			this.paint.setAntiAlias(true);
			this.paint.setColor(Color.BLUE);
		}

		@Override
		public void draw(Canvas canvas) {
			Rect bounds = this.getBounds();
			//add a line for the circle
			canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2, paint);
		}

		@Override
		public void setAlpha(int i) {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public void setColorFilter(ColorFilter colorFilter) {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public int getOpacity() {
			return 0;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}


	public static void say(String s) {
		tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}

	Session session;

	public TBTNavigation(Session s, MapScreen ms) {
		tts = new TextToSpeech(ToureNPlanerApplication.getContext(), this);
		session = s;
		this.ms = ms;
	}



	public void init(String tbtip, Observer tbtrequestListener) {
		ArrayList<ArrayList<int[]>> sendnodes = new ArrayList<ArrayList<int[]>>();
		for (int[] sw : session.getResult().getWay()) {
			ArrayList<int[]> subway = new ArrayList<int[]>();
			for (int i = 0; i < sw.length; i += 2) {
				//TODO: what's with the reversed lt/ln?
				int[] c = {sw[i + 1] * 10, sw[i] * 10};
				subway.add(c);
			}
			sendnodes.add(subway);
		}

		// the requestlistener will call tbtreqcompleted() when the request is successful
		Session.simplehandler = (SimpleNetworkHandler) new TurnByTurnHandler(tbtrequestListener, tbtip, sendnodes).execute();
	}

	public void tbtreqcompleted() {

		tbtway = session.gettbtResult().gettbtway();
		Toast.makeText(ToureNPlanerApplication.getContext(), "tbtway " + tbtway.size() + " items", Toast.LENGTH_LONG);

		if (tbtway == null) {
			//something is wrong
			Toast.makeText(ToureNPlanerApplication.getContext(), "no tbtway", Toast.LENGTH_LONG);
			return;
		}

		tbtoverlay.clear();
		// TODO: 30?
		markings = new ArrayList<OverlayItem>(tbtway.size() * 30);
		String lastname = "";
		for (ArrayList<Node> nodelist : tbtway) {
			for (Node n : nodelist) {
				if (!n.getName().equals(lastname)) {
					lastname = n.getName();
					OverlayItem item = new OverlayItem();
					item.setMarker(new turnmarker());
					item.setPoint(new GeoPoint(n.getLaE7(), n.getLaE7()));
					markings.add(item);
				}
			}
		}

		for (OverlayItem marking : markings) {
			tbtoverlay.addItem(marking);
		}

		if (!ms.getMapView().getOverlays().contains(tbtoverlay)) {
			ms.getMapView().getOverlays().add(tbtoverlay);
			tbtoverlay.setupOverlay(ms.getMapView());
		}
		Toast.makeText(ToureNPlanerApplication.getContext(), "added " + tbtoverlay.size() + " items", Toast.LENGTH_LONG);
	}

	private ArrayItemizedOverlay  tbtoverlay = new Tbtoverlay(new turnmarker());

	private class Tbtoverlay extends ArrayItemizedOverlay  {

		public Tbtoverlay(Drawable defaultMarker) {
			super(defaultMarker);
		}
	}

	public void startTBT() {
		active = true;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				say("Text to Speech initialized!");
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	double lat;
	double lon;
	double accuracy;
	double lastlat = 0;
	double lastlon = 0;
	double lastaccuracy = Double.POSITIVE_INFINITY;
	public void updatedLocation(Location l) {
		//say("Location updated with " + l.getAccuracy() + " meter accuracy");
		if (tbtway == null) {
			tbtway = session.gettbtResult().gettbtway();
			if (tbtway == null) {
				Toast.makeText(ToureNPlanerApplication.getContext(), "No turn by turn data available, please run a turn by turn request", Toast.LENGTH_LONG);
				 active = false;
				return;
			}
		}
		lat = l.getLatitude();
		lon = l.getLongitude();
		accuracy = l.getAccuracy();
		// Only do something when we didn't only move less than 10 percent of tolerance radius of the last location update
		if (CoordinateTools.directDistance(lat, lon, lastlat, lastlon) < lastaccuracy - (1 - lastaccuracy / lastaccuracy * 1.1)) {
			return;
		}
		double templat;
		double templon;

		// we already have a direction from the compass but I think having a direction based on the last and the current
		// coordinates is better for telling what the user is actually doing
		//double direction = session.getDirection();
		double direction = getBearing(lastlat, lastlon, lat, lon);

		for (ArrayList<Node> way : tbtway) {
			for (Node n : way) {
				templat = n.getGeoPoint().getLatitude();
				templon = n.getGeoPoint().getLongitude();
				double tempdist = CoordinateTools.directDistance(lat, lon, templat, templon);
				double directionlasttothis;
			}
		}
		lastlat = lat;
		lastlon = lon;
		lastaccuracy = accuracy;
	}

	//TODO: disable per default
	private  boolean active = true;
	public boolean currentlyRunning() {
		return active;
	}

	// http://www.smokycogs.com/blog/finding-the-bearing-between-two-gps-coordinates/
	private double getBearing(double lt1, double ln1, double lt2, double ln2) {
		double lat1 = DegToRad(lt1);
		double long1 = DegToRad(ln1);
		double lat2 = DegToRad(lt2);
		double long2 = DegToRad(ln2);

		double deltaLong = long2 - long1;

		double y = Math.sin(deltaLong) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) -
				Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLong);
		double bearing = Math.atan2(y, x);
		return ConvertToBearing(RadToDeg(bearing));
	}

	public static double RadToDeg(double radians) {
		return radians * (180 / Math.PI);
	}

	public static double DegToRad(double degrees) {
		return degrees * (Math.PI / 180);
	}

	public static double ConvertToBearing(double deg) {
		return (deg + 360) % 360;
	}
}