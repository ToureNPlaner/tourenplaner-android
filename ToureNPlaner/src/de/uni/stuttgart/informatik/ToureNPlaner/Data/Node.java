package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.mapsforge.android.maps.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {
	private String name;
	private int laE7;
	private int loE7;
	private ArrayList<Constraint> constraintList;
    private transient GeoPoint geoPoint;

	public Node(String name, int laE7, int loE7,
			ArrayList<Constraint> constraintList) {
		this.name = name;
		this.laE7 = laE7;
		this.loE7 = loE7;
		this.constraintList = constraintList;
	}

	public Node(String name, int laE7, int loE7) {
		this.name = name;
		this.laE7 = laE7;
		this.loE7 = loE7;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLaE7() {
		return this.laE7;
	}

	public int getLoE7() {
		return this.loE7;
	}

	public ArrayList<Constraint> getConstraintList() {
		return constraintList;
	}

	public void setConstraintList(ArrayList<Constraint> constraintList) {
		this.constraintList = constraintList;
	}

    public GeoPoint getGeoPoint() {
        if(geoPoint == null) {
            geoPoint = new GeoPoint(laE7/10, loE7/10);
        }
        return geoPoint;
    }

    }

