package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class User implements Serializable {

	public static User parse(JSONObject object) throws JSONException {
	        return new User();
	}
}
