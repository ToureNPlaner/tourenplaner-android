package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.ConstraintType;
import org.mapsforge.core.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {
	private String name;
	private String shortName;
	private GeoPoint geoPoint;

	private final int id;

	private ArrayList<Constraint> constraintList;

	public Node(int id, String name, String shortName, GeoPoint point, ArrayList<ConstraintType> constraintTypes) {
		this.id = id;
		this.name = name;
		this.shortName = shortName;
		this.geoPoint = point;
		constraintList = new ArrayList<Constraint>(constraintTypes.size());
		for (int i = 0; i < constraintTypes.size(); i++) {
			constraintList.add(new Constraint(constraintTypes.get(i)));
		}
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
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

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public int getId() {
		return id;
	}

	public void setConstraintList(ArrayList<Constraint> constraintList) {
		this.constraintList = constraintList;
	}
}

