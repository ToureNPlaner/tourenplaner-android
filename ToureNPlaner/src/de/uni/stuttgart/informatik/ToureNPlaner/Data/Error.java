package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import com.fasterxml.jackson.databind.JsonNode;

public class Error extends Exception {
	public String errorId;
	public String message;
	public String details;

	public static Error parse(JsonNode node) {
		Error error = new Error();

		error.errorId = node.get("errorid").asText();
		error.message = node.get("message").asText();
		error.details = node.get("details").asText();

		return error;
	}

	@Override
	public String toString() {
		return message;
	}
}
