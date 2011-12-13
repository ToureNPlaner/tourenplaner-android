package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {
	private String email;
	private String address;
	private String status;
	private String admin;
	private String firstname;
	private String lastname;

	public static User parse(JSONObject object) throws JSONException {
		User user = new User();
		user.address = object.getString("email");
		user.address = object.getString("address");
		user.address = object.getString("status");
		user.address = object.getString("admin");
		user.address = object.getString("firstname");
		user.address = object.getString("lastname");

        return new User();
	}
}
