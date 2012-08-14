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

package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import com.fasterxml.jackson.databind.JsonNode;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import org.mapsforge.core.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.round;

public class TBTResult implements Serializable {

	private static final double calcDirectDistance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6370.97327862;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;
		return dist * 1000;
	}

	public TBTResult(){
	}

	public int getDist() {
		return dist;
	}

	public List getStreets() {
		return streets;
	}

	private int dist = 0;
	List streets = new ArrayList<String>();

	public static TBTResult parse(JacksonManager.ContentType type, InputStream stream) throws IOException {
		JsonNode result = JacksonManager.getMapper(type).readValue(stream, JsonNode.class);

		TBTResult r = new TBTResult();
		ArrayList<ArrayList<Node>> tbtway = new ArrayList<ArrayList<Node>>();
		double onestreetdist = 0;
		for (JsonNode onestreetlist : result.get("streets")) {
			ArrayList<Node> onestreet = new ArrayList<Node>(onestreetlist.size());
			Object a = onestreetlist.fields();
			for (int i = 0; i < onestreetlist.get("coordinates").size() - 1; i += 1) {
				double lt = onestreetlist.get("coordinates").get(i).get("lt").asDouble();
				double ln = onestreetlist.get("coordinates").get(i).get("ln").asDouble();
				double lt2 = onestreetlist.get("coordinates").get(i + 1).get("lt").asDouble();
				double ln2 = onestreetlist.get("coordinates").get(i + 1).get("ln").asDouble();
				onestreet.add(new Node(i, onestreetlist.get("name") + ": " + String.valueOf(i), String.valueOf(i), new GeoPoint(lt, ln), null));
				onestreet.add(new Node(i + 1, onestreetlist.get("name") + ": " + String.valueOf(i), String.valueOf(i + 1), new GeoPoint(lt2, ln2), null));
//				tbtsubway.add((int) (lt * Math.pow(10,6)));
//				tbtsubway.add((int) (ln * Math.pow(10, 6)));
//				tbtsubway.add((int) (lt2 * Math.pow(10, 6)));
//				tbtsubway.add((int) (ln2 * Math.pow(10, 6)));
				onestreetdist += calcDirectDistance(lt, ln, lt2, ln2);
			}
			tbtway.add(onestreet);
			r.streets.add(onestreetlist.get("name") + " (" + round(onestreetdist) + " meter)");
			r.dist += onestreetdist;
			onestreetdist = 0;
		}
		return r;
	}

	public static class Misc implements Serializable, Cloneable {
		public HashMap<String, String> info = new HashMap<String, String>();

		float distance;
		float time;

		public String getMessage() {
			return info.get("message");
		}

		public double getDistance() {
			return distance;
		}

		public double getTime() {
			return time;
		}

	}
}
