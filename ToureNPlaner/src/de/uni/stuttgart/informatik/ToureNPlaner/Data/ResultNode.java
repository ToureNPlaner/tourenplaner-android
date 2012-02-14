package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.mapsforge.core.GeoPoint;

import java.io.Serializable;

public class ResultNode implements Serializable {
	private final int id;
	private final GeoPoint geoPoint;

	public ResultNode(int id, GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
		this.id = id;
	}

	public ResultNode(int id, int lt, int ln) {
		this(id, new GeoPoint(lt / 10, ln / 10));
	}

	public int getId() {
		return id;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
}
