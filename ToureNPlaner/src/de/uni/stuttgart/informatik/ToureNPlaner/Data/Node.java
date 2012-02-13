package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints.Constraint;
import org.mapsforge.core.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {
	private String name;
	private String type;
	private ArrayList<Constraint> constraintList;
	private GeoPoint geoPoint;

	public Node(String name, GeoPoint point) {
		this.name = name;
		this.geoPoint = point;
	}

	public Node(String name, GeoPoint point, ArrayList<Constraint> constraintList) {
		this(name, point);
		this.constraintList = constraintList;
	}

	public Node(String name, int laE7, int loE7) {
		this.name = name;
		this.geoPoint = new GeoPoint(laE7 / 10, loE7 / 10);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLaE7() {
		return geoPoint.latitudeE6 * 10;
	}

	public int getLoE7() {
		return geoPoint.longitudeE6 * 10;
	}

	public ArrayList<Constraint> getConstraintList() {
		return constraintList;
	}

	public void setConstraintList(ArrayList<Constraint> constraintList) {
		this.constraintList = constraintList;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
}

