package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import java.io.Serializable;

public class Constraint implements Serializable {
	private String name;
	private Double minimumValue;
	private Double maximumValue;
    private String type;
	
	Constraint(String name, Double minimumValue, Double maximumValue){//,Type)
	this.name = name;
	this.minimumValue = minimumValue;
	this.maximumValue = maximumValue;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
