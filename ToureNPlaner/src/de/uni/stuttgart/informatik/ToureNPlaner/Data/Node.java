package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.Constraint;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Constraints.ConstraintType;
import org.mapsforge.core.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {
	private String name;
	private GeoPoint geoPoint;

	private final int id;

	private final ArrayList<Constraint> constraintList;

	public Node(int id, String name, GeoPoint point, ArrayList<ConstraintType> constraintTypes) {
		this.id = id;
		this.name = name;
		this.geoPoint = point;
		constraintList = new ArrayList<Constraint>(constraintTypes.size());
		for (int i = 0; i < constraintTypes.size(); i++) {
			constraintList.add(new Constraint(constraintTypes.get(i)));
		}
	}

	public Node(int id, String name, int laE7, int loE7, ArrayList<ConstraintType> constraintTypes) {
		this(id, name, new GeoPoint(laE7 / 10, loE7 / 10), constraintTypes);
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
}

