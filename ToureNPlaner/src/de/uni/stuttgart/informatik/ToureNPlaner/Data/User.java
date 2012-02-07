package de.uni.stuttgart.informatik.ToureNPlaner.Data;


import org.codehaus.jackson.JsonNode;

import java.io.Serializable;


public class User implements Serializable {
	private String email;
	private String address;
	private String status;
	private String admin;
	private String firstname;
	private String lastname;

	public static User parse(JsonNode object) {
		User user = new User();
		user.address = object.get("email").asText();
		user.address = object.get("address").asText();
		user.address = object.get("status").asText();
		user.address = object.get("admin").asText();
		user.address = object.get("firstname").asText();
		user.address = object.get("lastname").asText();

		return new User();
	}
}
