package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits.Constraints;

import org.codehaus.jackson.JsonNode;

import java.io.Serializable;

public class Constraint implements Serializable {
	private String name;
	private String type;
	private Object value;
	private Double minimumValue;
	private Double maximumValue;

	public Constraint(String name, String type, Double minimumValue, Double maximumValue) {
		this.name = name;
		this.type = type;

		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
	}

	public Constraint(Constraint constraint) {
		this.name = constraint.name;
		this.type = constraint.type;

		this.minimumValue = constraint.minimumValue;
		this.maximumValue = constraint.maximumValue;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return name;
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

	public static Constraint parse(JsonNode object) {
		String name = object.path("name").asText();
		String type = object.path("type").asText();
		Double min = object.path("min").asDouble();
		Double max = object.path("max").asDouble();
		return new Constraint(name, type, min, max);
	}
}
