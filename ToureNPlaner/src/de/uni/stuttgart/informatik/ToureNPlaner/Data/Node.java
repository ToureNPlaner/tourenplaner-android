package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import java.util.List;

public class Node {
	private String name;
	private Double latitude;
	private Double longitude;
	private List<Constraint> constraintList;

	public Node(String name, Double latitude, Double longitude,
			List<Constraint> constraintList) {
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
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public List<Constraint> getConstraintList() {
		return constraintList;
	}

	public void setConstraintList(List<Constraint> constraintList) {
		this.constraintList = constraintList;
	}
}
