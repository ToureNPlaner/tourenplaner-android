package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.codehaus.jackson.JsonNode;
import org.mapsforge.core.GeoPoint;

import java.io.Serializable;

public class ResultNode implements Serializable {
	private final int id;
	private final GeoPoint geoPoint;
	private final String name;
	private final String shortName;

	public ResultNode(int id, GeoPoint geoPoint, String name, String shortName) {
		this.geoPoint = geoPoint;
		this.id = id;
		this.name = name;
		this.shortName = shortName;
	}

	public ResultNode(int id, int lt, int ln, String name, String shortName) {
		this(id, new GeoPoint(lt / 10, ln / 10), name, shortName);
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

	public static ResultNode parse(JsonNode node) {
		int ln = node.path("ln").asInt();
		int lt = node.path("lt").asInt();
		int id = node.path("id").asInt();
		String name = node.path("name").asText();
		String shortName = node.path("shortname").asText();

		return new ResultNode(id, lt, ln, name, shortName);
	}
}
