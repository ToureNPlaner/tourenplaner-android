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
