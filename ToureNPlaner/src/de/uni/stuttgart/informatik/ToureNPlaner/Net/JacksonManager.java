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

package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;

public class JacksonManager {
	static ObjectMapper jsonMapper;
	static ObjectMapper smileMapper;

	public static ObjectMapper getJsonMapper() {
		if (jsonMapper == null)
			jsonMapper = new ObjectMapper(new JsonFactory());
		return jsonMapper;
	}

	public static ObjectMapper getSmileMapper() {
		if (smileMapper == null)
			smileMapper = new ObjectMapper(new SmileFactory());
		return smileMapper;
	}

	public static ObjectMapper getMapper(ContentType type) {
		switch (type) {
			case SMILE:
				return getSmileMapper();
			case JSON:
			default:
				return getJsonMapper();
		}
	}

	public enum ContentType {
		JSON("application/json"), SMILE("application/x-jackson-smile");

		public final String identifier;

		private ContentType(String s) {
			identifier = s;
		}

		public static ContentType parse(String header) {
			if (header == null)
				throw new IllegalArgumentException("No Content-Type Header.");
			String[] s = header.split(";");
			if (s.length < 0)
				throw new IllegalArgumentException("Wrong Content-Type Header. Was: \"" + header + "\".");

			for (ContentType t : ContentType.values()) {
				if (s[0].toLowerCase().equals(t.identifier))
					return t;
			}

			throw new IllegalArgumentException("Wrong Content-Type Header. Was: \"" + header + "\".");
		}
	}
}
