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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.SmartIntArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Result implements Serializable {
	private int[][] way;
	private ArrayList<ResultNode> points;
	private Misc misc;

	private int version = 0;

	public Result(){
		this.way = null;
		this.points = new ArrayList<ResultNode>();
		this.misc = new Misc();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int[][] getWay() {
		return way;
	}

	public void setWay(int[][] way) {
		this.way = way;
	}

	public ArrayList<ResultNode> getPoints() {
		return points;
	}

	public Misc getMisc() {
		return misc;
	}

	static void jacksonParse(JsonParser jp, ArrayList<SmartIntArray> ways, ArrayList<ResultNode> points, Misc misc) throws IOException {
		int lt = 0, ln = 0;
		while (jp.nextToken() != JsonToken.END_OBJECT && jp.getCurrentToken() != null) {
			if ("constraints".equals(jp.getCurrentName())) {
				// consume
				while (jp.nextToken() != JsonToken.END_OBJECT && jp.getCurrentToken() != JsonToken.VALUE_NULL) {
				}
			}
			if ("misc".equals(jp.getCurrentName())) {
				jp.nextToken();
				JsonNode node = jp.readValueAsTree();
				Misc.parse(misc, node);
			}
			if ("points".equals(jp.getCurrentName())) {
				jp.nextToken();
				JsonNode nodes = jp.readValueAsTree();
				for (JsonNode node : nodes) {
					points.add(ResultNode.parse(node));
				}
			}
			if ("way".equals(jp.getCurrentName())) {
				if (jp.nextToken() == JsonToken.START_ARRAY) {
					while (jp.nextToken() != JsonToken.END_ARRAY) {
						if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
							SmartIntArray currentWay = new SmartIntArray();
							while (jp.nextToken() != JsonToken.END_ARRAY) {
								while (jp.nextToken() != JsonToken.END_OBJECT) {
									if (jp.getCurrentName().equals("lt")) {
										jp.nextToken();
										lt = jp.getIntValue() / 10;
									} else if (jp.getCurrentName().equals("ln")) {
										jp.nextToken();
										ln = jp.getIntValue() / 10;
									}
								}
								currentWay.add(ln);
								currentWay.add(lt);
							}
							if (currentWay.size() > 0) {
								ways.add(currentWay);
							}
						}
					}
				}
			}
		}
	}

	public static Result parse(JacksonManager.ContentType type, InputStream stream) throws IOException {
		Result result = new Result();
		ArrayList<SmartIntArray> ways = new ArrayList<SmartIntArray>();
		ObjectMapper mapper = JacksonManager.getMapper(type);

		JsonParser jp = mapper.getJsonFactory().createJsonParser(stream);
		try {
			jacksonParse(jp, ways, result.points, result.misc);
		} finally {
			jp.close();
		}

		int size = ways.size();
		result.way = new int[size][];
		for (int i = 0; i < size; i++) {
			result.way[i] = ways.get(i).toArray();
		}
		return result;
	}

	public static class Misc implements Serializable, Cloneable {
		public HashMap<String, String> info = new HashMap<String, String>();

		float distance;
		float time;

		public String getAlgorithm() {
			return info.get("algorithm");
		}

		public void setAlgorithm(AlgorithmInfo s) {
			info.put("algorithm", s.getName());
		}

		public String getMessage() {
			return info.get("message");
		}

		public void setMessage(String message) {
			info.put("message", message);
		}

		public double getDistance() {
			return distance;
		}

		public void setDistance(float distance) {
			this.distance = distance;
		}

		public double getTime() {
			return time;
		}

		public void setTime(float time) {
			this.time = time;
		}

		public static void parse(Misc misc, JsonNode node) {
			misc.time = (float) node.path("time").asDouble();
			misc.distance = node.path("distance").asInt();

			Iterator<Map.Entry<String, JsonNode>> ns = node.fields();
			while (ns.hasNext()) {
				Map.Entry<String, JsonNode> n = ns.next();
				if (!n.getKey().equals("time") && !n.getKey().equals("distance"))
					misc.info.put(n.getKey(), n.getValue().asText());
			}
		}
	}
}
