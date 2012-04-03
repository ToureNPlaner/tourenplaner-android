package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.SmartIntArray;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int[][] getWay() {
		return way;
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
				Misc.parse(misc, jp.readValueAsTree());
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
		ArrayList<ResultNode> points = new ArrayList<ResultNode>();
		Misc misc = new Misc();
		ObjectMapper mapper = JacksonManager.getMapper(type);

		JsonParser jp = mapper.getJsonFactory().createJsonParser(stream);
		try {
			jacksonParse(jp, ways, points, misc);
		} finally {
			jp.close();
		}

		int size = ways.size();
		result.way = new int[size][];
		for (int i = 0; i < size; i++) {
			result.way[i] = ways.get(i).toArray();
		}
		result.points = points;
		result.misc = misc;
		return result;
	}

	public static class Misc implements Serializable, Cloneable {
		public HashMap<String, String> info = new HashMap<String, String>();

		float distance;
		float time;

		public String getMessage() {
			return info.get("message");
		}

		public double getDistance() {
			return distance;
		}

		public double getTime() {
			return time;
		}

		public static void parse(Misc misc, JsonNode node) {
			misc.time = (float) node.path("time").asDouble();
			misc.distance = node.path("distance").asInt();

			Iterator<Map.Entry<String, JsonNode>> ns = node.getFields();
			while (ns.hasNext()) {
				Map.Entry<String, JsonNode> n = ns.next();
				if (!n.getKey().equals("time") && !n.getKey().equals("distance"))
					misc.info.put(n.getKey(), n.getValue().asText());
			}
		}
	}
}
