package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import java.util.List;

public class Node {
	private String name;
	private Float latitude;
	private Float longitude;
	private List<Constraint> constraintList;
	
	public Node(String name,Float latitude,Float longitude, List<Constraint> constraintList){
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.constraintList = constraintList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public List<Constraint> getConstraintList() {
		return constraintList;
	}

	public void setConstraintList(List<Constraint> constraintList) {
		this.constraintList = constraintList;
	}
	}
