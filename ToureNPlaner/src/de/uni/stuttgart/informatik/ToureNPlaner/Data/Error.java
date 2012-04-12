/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
