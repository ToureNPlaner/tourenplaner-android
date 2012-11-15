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
import de.uni.stuttgart.informatik.ToureNPlaner.Util.CoordinateTools;
import org.mapsforge.core.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.round;

public class TBTResult implements Serializable {

	public TBTResult(){
	}

	public int getCompleteDist() {
		return dist;
	}

	public List<String> getStreetNames() {
		return streets;
	}

	public ArrayList<ArrayList<Node>> gettbtway() {
		return tbtway;
	}

	private int dist = 0;
	List streets = new ArrayList<String>();

	private static ArrayList<ArrayList<Node>> tbtway = null;
	public static TBTResult parse(JacksonManager.ContentType type, InputStream stream) throws IOException {
		JsonNode result = JacksonManager.getMapper(type).readValue(stream, JsonNode.class);

		TBTResult r = new TBTResult();
		tbtway = new ArrayList<ArrayList<Node>>();
		double onestreetdist = 0;
		for (JsonNode onestreetlist : result.get("streets")) {
			ArrayList<Node> onestreet = new ArrayList<Node>(onestreetlist.size());
			for (int i = 0; i < onestreetlist.get("coordinates").size() - 1; i += 1) {
				double lt = onestreetlist.get("coordinates").get(i).get("lt").asDouble();
				double ln = onestreetlist.get("coordinates").get(i).get("ln").asDouble();
				double lt2 = onestreetlist.get("coordinates").get(i + 1).get("lt").asDouble();
				double ln2 = onestreetlist.get("coordinates").get(i + 1).get("ln").asDouble();

				//+": " + String.valueOf(i)
				onestreet.add(new Node(i, onestreetlist.get("name").asText() , String.valueOf(i), new GeoPoint(lt, ln), null));
				onestreet.add(new Node(i + 1, onestreetlist.get("name").asText(), String.valueOf(i + 1), new GeoPoint(lt2, ln2), null));
//				tbtsubway.add((int) (lt * Math.pow(10,6)));
//				tbtsubway.add((int) (ln * Math.pow(10, 6)));
//				tbtsubway.add((int) (lt2 * Math.pow(10, 6)));
//				tbtsubway.add((int) (ln2 * Math.pow(10, 6)));
				onestreetdist += CoordinateTools.directDistance(lt, ln, lt2, ln2);
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
