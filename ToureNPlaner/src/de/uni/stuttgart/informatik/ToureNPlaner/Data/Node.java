package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.mapsforge.android.maps.GeoPoint;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Node implements Serializable {
	private String name;
	private double latitude;
	private double longitude;
	private ArrayList<Constraint> constraintList;
    private transient GeoPoint geoPoint;

	public Node(String name, Double latitude, Double longitude,
			ArrayList<Constraint> constraintList) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.constraintList = constraintList;
	}

	public Node(String name, Double latitude, Double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public Double getLongitude() {
		return this.longitude;
	}

	public ArrayList<Constraint> getConstraintList() {
		return constraintList;
	}

	public void setConstraintList(ArrayList<Constraint> constraintList) {
		this.constraintList = constraintList;
	}

    public GeoPoint getGeoPoint() {
        if(geoPoint == null) {
            geoPoint = new GeoPoint(latitude, longitude);
        }
        return geoPoint;
    }

    }

