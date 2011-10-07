package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.mapsforge.android.maps.GeoPoint;

import java.util.ArrayList;

public class Node {
	private String name;
	private Double latitude;
	private Double longitude;
	private GeoPoint geoPoint;
	private ArrayList<Constraint> constraintList;

	public Node(String name, Double latitude, Double longitude,
			ArrayList<Constraint> constraintList) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.constraintList = constraintList;
	}

	public GeoPoint getGeoPoint() {
		return this.geoPoint;
	}

	public Node(String name, Double latitude, Double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Node(String name, GeoPoint gp) {
		this.name = name;
		this.geoPoint = gp;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public ArrayList<Constraint> getConstraintList() {
		return constraintList;
	}

	public void setConstraintList(ArrayList<Constraint> constraintList) {
		this.constraintList = constraintList;
	}
}
