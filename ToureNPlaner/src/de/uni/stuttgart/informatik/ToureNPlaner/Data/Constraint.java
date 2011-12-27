package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.json.JSONException;
import org.json.JSONObject;

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
    
   	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value =  value;
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

    public static Constraint parse(JSONObject object) throws JSONException {
        String name = object.getString("name");
        String type = object.getString("type");
        Double min = object.getDouble("min");
        Double max = object.getDouble("max");
        return new Constraint(name,type,min,max);
    }
}
