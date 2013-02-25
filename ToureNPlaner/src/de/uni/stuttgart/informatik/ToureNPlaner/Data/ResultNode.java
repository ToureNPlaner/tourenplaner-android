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
import org.mapsforge.core.GeoPoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResultNode implements Serializable {
	private final int id;
	private final GeoPoint geoPoint;
	private final String name;
	private final String shortName;
	private final HashMap<String, String> misc;
	private final double distToPrev;
	private final double timeToPrev;

	public ResultNode(int id, GeoPoint geoPoint, String name, String shortName, HashMap<String, String> misc, double distToPrev, double timeToPrev) {
		this.geoPoint = geoPoint;
		this.id = id;
		this.name = name;
		this.shortName = shortName;
		this.misc = misc;
		this.distToPrev = distToPrev;
		this.timeToPrev = timeToPrev;
	}

	public ResultNode(int id, int lt, int ln, String name, String shortName, HashMap<String, String> misc, double distToPrev, double timeToPrev) {
		this(id, new GeoPoint(lt / 10, ln / 10), name, shortName, misc, distToPrev, timeToPrev);
	}

	public ResultNode(GeoPoint geoPoint) {
		this(-1, geoPoint, "", "", new HashMap<String, String>(), 0, 0);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public double getDistToPrev() {
		return distToPrev;
	}

	public double getTimeToPrev() {
		return timeToPrev;
	}

	public HashMap<String, String> getMisc() {
		return misc;
	}

	public static ResultNode parse(JsonNode node) {
		int ln = node.path("ln").asInt();
		int lt = node.path("lt").asInt();
		int id = node.path("id").asInt();
		String name = node.path("name").asText();
		String shortName = node.path("shortname").asText();
		double distToPrev = node.path("distToPrev").asInt();
		double timeToPrev = node.path("timeToPrev").asDouble();

		HashMap<String, String> misc = new HashMap<String, String>();

		Iterator<Map.Entry<String, JsonNode>> ns = node.fields();
		while (ns.hasNext()) {
			Map.Entry<String, JsonNode> n = ns.next();
			if (!n.getKey().equals("ln") &&
					!n.getKey().equals("lt") &&
					!n.getKey().equals("id") &&
					!n.getKey().equals("name") &&
					!n.getKey().equals("shortname") &&
					!n.getKey().equals("distToPrev") &&
					!n.getKey().equals("timeToPrev")) {
				misc.put(n.getKey(), n.getValue().asText());
			}
		}

		return new ResultNode(id, lt, ln, name, shortName, misc, distToPrev, timeToPrev);
	}
}
