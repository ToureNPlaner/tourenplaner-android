package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class User implements Serializable {
	private String email;
	private String address;
	private String status;
	private String admin;
	private String firstname;
	private String lastname;

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getStatus() {
		return status;
	}

	public String getAdmin() {
		return admin;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public static User parse(JsonNode object) {
		User user = new User();
		user.email = object.get("email").asText();
		user.address = object.get("address").asText();
		user.status = object.get("status").asText();
		user.admin = object.get("admin").asText();
		user.firstname = object.get("firstname").asText();
		user.lastname = object.get("lastname").asText();

		return new User();
	}
}
