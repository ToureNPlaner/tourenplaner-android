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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.abs;

public class TBTNavigation implements TextToSpeech.OnInitListener, Serializable {
	private static TextToSpeech tts;
	private ArrayList<ArrayList<Node>> tbtway = null;

	List<OverlayItem> markings;
	private MapScreen ms;
	private static long lastdirectionspeech = (new Date()).getTime();

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
			Log.d("tp", "circle");
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
		Log.i("tp", "Doing tbt request");
		ArrayList<ArrayList<int[]>> sendnodes = new ArrayList<ArrayList<int[]>>();
		if (session.getResult() == null) {
			Toast.makeText(ToureNPlanerApplication.getContext(), "TBT called but we don't have a result, WTF", Toast.LENGTH_LONG).show();
		}
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
		if (Session.simplehandler != null) {
			Session.simplehandler.cancel(true);
		}
		Session.simplehandler = (SimpleNetworkHandler) new TurnByTurnHandler(tbtrequestListener, tbtip, sendnodes).execute();
	}

	public void tbtreqcompleted() {
		sayEnglish("Turn by Turn request complete");
		tbtway = session.gettbtResult().gettbtway();

		// 10 streets with 10 characters each?
		StringBuilder str = new StringBuilder(tbtway.size()  * 10 * 10);
		for (ArrayList<Node> nodes : tbtway) {
			if (!nodes.get(0).getName().trim().contains("??")) {
				str.append(nodes.get(0).getName()).append("! ");
			}
		}

		Log.d("tp", str.toString());
		//say("Deine Route ist: " + str.toString());

		Toast.makeText(ToureNPlanerApplication.getContext(), "tbtway " + tbtway.size() + " items", Toast.LENGTH_LONG).show();

		if (tbtway == null) {
			//something is wrong
			return;
		}

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

		if (!ms.getMapView().getOverlays().contains(tbtoverlay)) {
			//tbtoverlay.setupOverlay(ms.getMapView());
			//ms.getMapView().getOverlays().add(tbtoverlay);
		}
		tbtoverlay.clear();
		tbtoverlay.addItems(markings);
		tbtoverlay.requestRedraw();

		Toast.makeText(ToureNPlanerApplication.getContext(), "added " + tbtoverlay.size() + " items", Toast.LENGTH_LONG).show();
		Log.i("tp", "added " + tbtoverlay.size() + " items");
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

			int result = tts.setLanguage(Locale.GERMAN);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				//say("Text to Speech initialized!");
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	double lat;
	double lon;
	double currentaccuracy;
	double lastlat = 0;
	double lastlon = 0;
	double currentlon;
	double currentlat;
	double lastaccuracy = Double.POSITIVE_INFINITY;
	public void updatedLocation(Location l) {
		//say("Location updated with " + l.getAccuracy() + " meter currentaccuracy");
		if (tbtway == null) {
			tbtway = session.gettbtResult().gettbtway();
			if (tbtway == null) {
				Toast.makeText(ToureNPlanerApplication.getContext(), "No turn by turn data available, please run a turn by turn request", Toast.LENGTH_LONG);
				active = false;
				return;
			}
		}

		currentlat = l.getLatitude();
		currentlon = l.getLongitude();
		currentaccuracy = l.getAccuracy();
		if (currentaccuracy > 50) {
			Log.i("tp", "Location update with accuracy > 50: " + currentaccuracy);
			// useless coordinates
			return;
		}

		// Only do something when we didn't only move less than 10 percent of tolerance radius of the last location update
		//if (CoordinateTools.directDistance(lat, lon, lastlat, lastlon) < lastaccuracy - (1 - lastaccuracy / lastaccuracy * 1.1)) {
		//	return;
		//}

		// we already have a direction from the compass but I think having a direction based on the last and the current
		// coordinates is better for telling what the user is actually doing
		//double direction = session.getDirection();
		double direction = getBearing(lastlat, lastlon, lat, lon);

//		for (ArrayList<Node> way : tbtway) {
//			for (Node n : way) {
//				currentlat = n.getGeoPoint().getLatitude();
//				currentlon = n.getGeoPoint().getLongitude();
//				double tempdist = CoordinateTools.directDistance(lat, lon, currentlat, currentlon);
//				double directionlasttothis;
//			}
//		}



//		Node[] nearestnodes = getTwoNearestNodes(lat,lon);
//		if (nearestnodes == null || nearestnodes[0] == null || nearestnodes[1] == null) {
//			//meh
//			return;
//		}
		//double nodesdirection = getBearing(nearestnodes[0].getGeoPoint().getLatitude(), nearestnodes[0].getGeoPoint().getLongitude(), nearestnodes[1].getGeoPoint().getLatitude(), nearestnodes[0].getGeoPoint().getLongitude());

		Node n = nearestNode(currentlat, currentlon);
		Log.d("tp", "Nearest node: " + n.getName());
		if (n == null) {
			Log.i("tp", "tbtway too short??");
			//meh
			return;
		}

		double lastcurrentstreetdirection = Double.NaN;
		double firstnextstreetdirection = Double.NaN;
		// we go from the current node along the way until we reach some interesting point
		// if
		double distToPOI = Double.POSITIVE_INFINITY;
		boolean interestingway = false;
		Node lastnode = null;
		String lastname = "";
		String tempname = "";
		boolean brokesearch = false;
		boolean stillneednextnode = true;
		SEARCH:
		for (ArrayList<Node> nodes : tbtway) {
			tempname = nodes.get(0).getName();
			for (Node node : nodes) {
				if (node.equals(n)) {
					interestingway = true;
					lastname = tempname;
					// begin with our distance to the nearest point on the way
					distToPOI = CoordinateTools.directDistance(n.getGeoPoint().getLatitude(), n.getGeoPoint().getLongitude(), node.getGeoPoint().getLatitude(), node.getGeoPoint().getLongitude());
				}

				if (lastnode != null) {
					if (interestingway) {
						distToPOI += CoordinateTools.directDistance(lastnode.getGeoPoint().getLatitude(), lastnode.getGeoPoint().getLongitude(), node.getGeoPoint().getLatitude(), node.getGeoPoint().getLongitude());
						if (tempname != lastname) {
							if (stillneednextnode) {
								stillneednextnode = false;
								continue;
							}
							firstnextstreetdirection = getBearing(lastnode.getGeoPoint().getLatitude(), lastnode.getGeoPoint().getLongitude(), node.getGeoPoint().getLatitude(), node.getGeoPoint().getLongitude());
							brokesearch = true;
							break SEARCH;
						}
					}
					lastcurrentstreetdirection = getBearing(lastnode.getGeoPoint().getLatitude(), lastnode.getGeoPoint().getLongitude(), node.getGeoPoint().getLatitude(), node.getGeoPoint().getLongitude());
				}
				lastnode = node;
			}
			lastname = tempname;
		}

		if (!brokesearch && distToPOI < 100) {
			sayGerman("Das Ziel liegt innerhalb von 100 Metern");
			this.active = false;
			return;
		}

		Log.i("tp", "next poi: " + lastname + " -> " + tempname);
		Log.i("tp", "distance to poi: " + (int) distToPOI);
		if (distToPOI < 800) {
			Log.i("tp", "change direction from " + lastcurrentstreetdirection + " to " + firstnextstreetdirection);
			// at least 5 seconds between direction speeches
			if ((new Date()).getTime() - lastdirectionspeech > 5000) {

				double diff = (lastcurrentstreetdirection - firstnextstreetdirection + 360) % 360;
				Log.i("tp", String.valueOf(diff));
				String directionwords = "geradeaus fahren";
				if (!brokesearch) {
					//TODO: Sprache
					directionwords = "zum Ziel";
				}
				// ~ 0 to 180 = everything to the left, ~ 20 in each direction = no turn
				if (diff > 10 && diff < 170) {
					directionwords = "nach links abbiegen";
				} else if (diff < 350 && diff > 190) {
					directionwords = " nach rechts abbiegen ";
				} else if (abs(diff) <= 10 || abs(diff) >= 350) {
					directionwords = "wenden";
				}
				String say = "In " + (int) distToPOI + " Metern " + directionwords + " auf " + (tempname.startsWith("??") ? " eine unbenannte Straße" : " die " + tempname) + "!";
				Log.i("tp", "Say: " + say);
				Toast.makeText(ToureNPlanerApplication.getContext(), say, Toast.LENGTH_LONG).show();
				sayGerman(say);
			}
		}

		lastlat = lat;
		lastlon = lon;
		lastaccuracy = currentaccuracy;
	}

	private Node nearestNode(double lat,double lon) {

		double shortestdist = Double.POSITIVE_INFINITY;
		double tempdist = Double.POSITIVE_INFINITY;
		Node result = null;
		for (ArrayList<Node> nodes : tbtway) {
			for (Node node : nodes) {
				tempdist = CoordinateTools.directDistance(lat, lon, node.getGeoPoint().getLatitude(), node.getGeoPoint().getLongitude());
				if (tempdist < shortestdist) {
					shortestdist = tempdist;
					result = node;
				}
			}

		}
		return result;
	}

	//TODO: this is linear complexity for every single location update
	private Node[] getTwoNearestNodes(double lat, double lon) {
		if (tbtway.size() < 2 && tbtway.get(0).size() < 2 ) {
			return null;
		}

		Node nearest1 = null;
		Node nearest2 = null;

		double shortestdist = Double.POSITIVE_INFINITY;
		Node lastnode = null;
		double lastnodedist = Double.POSITIVE_INFINITY;
		double tempdist = Double.POSITIVE_INFINITY;

		for (ArrayList<Node> nodes : tbtway) {
			for (Node node : nodes) {
				if (lastnode == null) {
					continue;
				}

				tempdist = CoordinateTools.directDistance(lat, lon, node.getGeoPoint().getLatitude(), node.getGeoPoint().getLongitude());
				if (lastnodedist + tempdist < shortestdist) {
					shortestdist = lastnodedist + tempdist;
					nearest1 = lastnode;
					nearest2 = node;
				}
				lastnode = node;
				lastnodedist = tempdist;
			}
		}

		return new Node[] {nearest1, nearest2};
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

	public void sayGerman(final String s)  {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Locale l = tts.getLanguage();
				tts.setLanguage(Locale.GERMAN);
				say(s);
				tts.setLanguage(l);
			}
		}).start();
	}

	public void sayEnglish(String s) {
		Locale l = tts.getLanguage();
		tts.setLanguage(Locale.ENGLISH);
		say(s);
		tts.setLanguage(l);
	}

}