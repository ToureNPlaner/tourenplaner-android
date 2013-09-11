/*
 * Copyright 2013 ToureNPlaner
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
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Edit;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.TBTResultEdit;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.TBTResult;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.AsyncHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.SimpleNetworkHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.TurnByTurnHandler;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.MapScreen.MapScreen;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.CoordinateTools;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.abs;

public class TBTNavigation implements TextToSpeech.OnInitListener, Serializable, Observer {
	private static TextToSpeech tts;
	private ArrayList<ArrayList<Node>> tbtway = null;

	private MapScreen ms;
	private static long lastdirectionspeech = (new Date()).getTime();

	public void stopTBT() {
		active = false;
	}

	@Override
	public void onCompleted(AsyncHandler caller, Object object) {
		Log.d("tp", "tbt request completed: " +object.toString());
		Session.sesshandler = null;
		Session.simplehandler = null;
		Edit edit = new TBTResultEdit(session, (TBTResult) object);
		//Log.d("tp", "tbt response: " + object.toString());
		edit.perform();
		//setSupportProgressBarIndeterminateVisibility(false);

		session.getTBTNavigation().tbtreqcompleted();
		active = true;
	}

	@Override
	public void onError(AsyncHandler caller, Object object) {
		Log.d("tp", "error: " + object.toString());
		Session.simplehandler = null;
		//setSupportProgressBarIndeterminateVisibility(false);
		Toast.makeText(ToureNPlanerApplication.getContext(), "TBT Request Error:\n" + object.toString(), Toast.LENGTH_LONG).show();
	}

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
		lastdirectionspeech = (new Date()).getTime();
		Log.i("tp", "said: " + s);
		tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}

	Session session;

	public TBTNavigation(Session s) {
		tts = new TextToSpeech(ToureNPlanerApplication.getContext(), this);
		session = s;
	}

	public void initTBT(String tbtip) {
		Log.i("tp", "Doing tbt request");
		// create a data structure out of the current ways that we can send to the tbt server
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
		Session.simplehandler = (SimpleNetworkHandler) new TurnByTurnHandler(this, tbtip, sendnodes).execute();
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

		String lastname = "";
		for (ArrayList<Node> nodelist : tbtway) {
			for (Node n : nodelist) {
				if (!n.getName().equals(lastname)) {
					lastname = n.getName();
					OverlayItem item = new OverlayItem();
					item.setMarker(new turnmarker());
					item.setPoint(new GeoPoint(n.getLaE7(), n.getLaE7()));
				}
			}
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
	double currentaccuracy = Double.POSITIVE_INFINITY;
	double lastlat = -1;
	double lastlon = -1;
	double currentlon = -1;
	double currentlat = -1;
	double lastaccuracy = Double.POSITIVE_INFINITY;
	public void updatedLocation(Location l) {
		if (!active) {
			return;
		}
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
		if (currentaccuracy - lastaccuracy > 20 && CoordinateTools.directDistance(lastlat, lastlon, lat,lon) < 20) {
			Log.i("tp", "accuracy got worse more than 10 meters since last check, discarding (" + lastaccuracy + "->" + currentaccuracy  +")");
			return;
		}

		// Only do something when we didn't only move less than 10 percent of tolerance radius of the last location update
		//if (CoordinateTools.directDistance(lat, lon, lastlat, lastlon) < lastaccuracy - (1 - lastaccuracy / lastaccuracy * 1.1)) {
		//	return;
		//}

		//TODO: Better method for determining the progress on the way
		Node n = nearestNode(currentlat, currentlon);
		Log.d("tp", "Nearest node: " + n.getName());
		if (n == null) {
			Log.i("tp", "tbtway too short??");
			//meh
			return;
		}

		double tempdist = 0;
		int nodesindex = -1;
		int tbtwayindex = -1;
		for (ArrayList<Node> nodes : tbtway) {
			if (nodes.contains(n)) {
				tbtwayindex = tbtway.indexOf(nodes);
				nodesindex = nodes.indexOf(n);
				break;
			}
		}
		if (nodesindex == -1 || tbtwayindex == -1) {
			sayGerman("Irgendetwas stimmt hier nicht!");
			return;
		}

		ArrayList<Node> tempway = tbtway.get(tbtwayindex);
		tempdist += CoordinateTools.directDistance(currentlat, currentlon,
				tempway.get(nodesindex).getGeoPoint().getLatitude(), tempway.get(nodesindex).getGeoPoint().getLongitude());
		// from the node after the current node to the last node on the current street
		for (int i = nodesindex + 1; i <= tempway.size() - 1; i++) {
			tempdist += CoordinateTools.directDistance(tempway.get(i-1).getGeoPoint().getLatitude(), tempway.get(i - 1).getGeoPoint().getLongitude(),
					tempway.get(i).getGeoPoint().getLatitude(), tempway.get(i).getGeoPoint().getLongitude());
		}
		if (tempdist > 800) {
			// no street change in the next 800 meters so we don't say anything
			Log.d("tp", tempdist + " meters to the next turn");
			return;
		}

		if (tbtwayindex == tbtway.size() - 1) {
			// we are on the street where the destination is
			Log.d("tp", tempdist + " to destination");
			if (tempdist < 20) {
				sayGerman("Sie erreichen Ihr Ziel in " + (int) tempdist + " Metern!");
				this.active = false;
			} else {
				sayGerman("Ihr Ziel liegt auf dieser Straße in " + (int) tempdist + " Metern!");
			}
			return;
		}

		int directionbeforeturn = (int) getBearing(tempway.get(tempway.size() - 2).getGeoPoint().getLatitude(), tempway.get(tempway.size() - 2).getGeoPoint().getLongitude(),
				tempway.get(tempway.size() - 1).getGeoPoint().getLatitude(), tempway.get(tempway.size() - 1).getGeoPoint().getLongitude());
		ArrayList<Node> tempwaynext = tbtway.get(tbtwayindex + 1);
		int directionafterturn = (int) getBearing(tempwaynext.get(0).getGeoPoint().getLatitude(), tempwaynext.get(0).getGeoPoint().getLongitude(),
				tempwaynext.get(1).getGeoPoint().getLatitude(), tempwaynext.get(1).getGeoPoint().getLongitude());

		Log.d("tp", "Direction 1: " + directionbeforeturn + " -> " + directionafterturn);

		double directionchange = 0;
		double tempdirection;
		int tempdirectionindex = tempway.size() - 1; // we already have the direction from this node to the last
		double tempdirectiondist = 0;
		double lastdirection = directionbeforeturn;
		if (tempway.size() > 2) {
			do {
				tempdirectiondist += CoordinateTools.directDistance(tempway.get(tempdirectionindex - 1).getGeoPoint().getLatitude(), tempway.get(tempdirectionindex - 1).getGeoPoint().getLongitude(),
						tempway.get(tempdirectionindex).getGeoPoint().getLatitude(), tempway.get(tempdirectionindex).getGeoPoint().getLongitude());
				tempdirection = getBearing(tempway.get(tempdirectionindex - 1).getGeoPoint().getLatitude(), tempway.get(tempdirectionindex - 1).getGeoPoint().getLongitude(),
						tempway.get(tempdirectionindex).getGeoPoint().getLatitude(), tempway.get(tempdirectionindex).getGeoPoint().getLongitude());
				directionchange += tempdirection - lastdirection;
				lastdirection = tempdirection;
				tempdirectionindex -= 1;
			} while (tempdirectiondist < 25 && tempdirectionindex > 0);
			directionbeforeturn += directionchange;
		}
		if (tempwaynext.size() > 2) {
			lastdirection = directionafterturn;
			// reuse directionchange, tempdirectionindex, lastdirection and tempdirectiondist
			directionchange = 0;
			tempdirectiondist = 0;
			tempdirectionindex = 2;
			do {
				tempdirectiondist += CoordinateTools.directDistance(tempwaynext.get(tempdirectionindex).getGeoPoint().getLatitude(), tempwaynext.get(tempdirectionindex).getGeoPoint().getLongitude(),
						tempwaynext.get(tempdirectionindex + 1).getGeoPoint().getLatitude(), tempwaynext.get(tempdirectionindex + 1).getGeoPoint().getLongitude());
				tempdirection = getBearing(tempwaynext.get(tempdirectionindex).getGeoPoint().getLatitude(), tempwaynext.get(tempdirectionindex).getGeoPoint().getLongitude(),
						tempwaynext.get(tempdirectionindex + 1).getGeoPoint().getLatitude(), tempwaynext.get(tempdirectionindex + 1).getGeoPoint().getLongitude());
				directionchange += tempdirection- lastdirection;
				lastdirection = tempdirection;
				tempdirectionindex += 1;
			} while (tempdirectiondist < 25 && tempdirectionindex < tempwaynext.size() - 1);
			directionafterturn += directionchange;
		}

		Log.d("tp", "Direction 2: " + directionbeforeturn + " -> " + directionafterturn);

		String currentstreetname = tempway.get(0).getName().startsWith("??") ? "eine unbenannte Straße" : tempway.get(0).getName();
		String nextstreetname = tempwaynext.get(0).getName().startsWith("??") ? "eine unbenannte Straße" : tempwaynext.get(0).getName();

		double diff = (directionbeforeturn - directionafterturn + 360) % 360;
		String directionwords = "geradeaus fahren";

		// ~ 0 to 180 = everything to the left, ~ 20 in each direction = no turn
		if (diff > 10 && diff < 170) {
			directionwords = "nach links abbiegen";
		} else if (diff < 350 && diff > 190) {
			directionwords = "nach rechts abbiegen";
		} else if (abs(diff) <= 190 && abs(diff) >= 170) {
			directionwords = "wenden";
		}

		//TODO: Here?
		if ((new Date()).getTime() - lastdirectionspeech < 5000) {
			return;
		}
		String say = "In " + (int) tempdist + " Metern " + directionwords + " auf " + nextstreetname + "!";
		sayGerman(say);

		lastlat = lat;
		lastlon = lon;
		lastaccuracy = currentaccuracy;
	}

	private Node nearestNode(double lat,double lon) {

		double shortestdist = Double.POSITIVE_INFINITY;
		double tempdist;
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

	//TODO: this is horrible and not even used
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

	private  boolean active = false;
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
		return (RadToDeg(bearing) + 360 ) % 360;
	}

	public static double RadToDeg(double radians) {
		return radians * (180 / Math.PI);
	}

	public static double DegToRad(double degrees) {
		return degrees * (Math.PI / 180);
	}

	public void sayGerman(final String s)  {
		Toast.makeText(ToureNPlanerApplication.getContext(), "Sprachausgabe:\n" + s, Toast.LENGTH_LONG).show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				// this is all sorts of wrong
				synchronized (tts) {
					Locale l = tts.getLanguage();
					tts.setLanguage(Locale.GERMAN);
					// Workarounds:
					// the german voice can't say "-" for some reason, it says something like "a" instead. wat
					// make sure, "straße" is pronounced correctly, additional space won't hurt but will force the correct pronounciation of "st"
					// weg often needs a long "e"
					say(s.replace('-',' ').replace("straße", " Straße").replace("weg", " Weeg"));
					tts.setLanguage(l);
				}
			}
		}).start();
	}

	public void sayEnglish(String s) {
		synchronized (tts) {
			Locale l = tts.getLanguage();
			tts.setLanguage(Locale.ENGLISH);
			say(s);
			tts.setLanguage(l);
		}
	}

}