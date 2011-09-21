package de.uni.stuttgart.informatik.ToureNPlaner.Data;

public class Constraint {
	private String name;
	private Double minimumValue;
	private Double maximumValue;
	
	Constraint(String name, Double minimumValue, Double maximumValue){//,Type)
	this.name = name;
	this.minimumValue = minimumValue;
	this.maximumValue = maximumValue;
	//type
	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(Double minimumValue) {
		this.minimumValue = minimumValue;
	}

	public Double getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(Double maximumValue) {
		this.maximumValue = maximumValue;
	}
}
