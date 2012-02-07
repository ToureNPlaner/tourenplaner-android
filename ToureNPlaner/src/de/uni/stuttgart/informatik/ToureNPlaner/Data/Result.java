package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.mapsforge.core.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Result implements Serializable {
	private GeoPoint[][] way;
	private ArrayList<Node> points;

	public GeoPoint[][] getWay() {
		return way;
	}

	public ArrayList<Node> getPoints() {
		return points;
	}

	static void jacksonParse(JsonParser jp, ArrayList<GeoPoint> way, ArrayList<Node> points) throws IOException {
		int lt = 0, ln = 0;
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			if ("points".equals(jp.getCurrentName())) {
				if (jp.nextToken() == JsonToken.START_ARRAY) {
					while (jp.nextToken() != JsonToken.END_ARRAY) {
						while (jp.nextToken() != JsonToken.END_OBJECT) {
							if (jp.getCurrentName().equals("lt")) {
								jp.nextToken();
								lt = jp.getIntValue();
							} else if (jp.getCurrentName().equals("ln")) {
								jp.nextToken();
								ln = jp.getIntValue();
							}
						}
						points.add(new Node("", lt, ln));
					}
				}
			}
			if ("way".equals(jp.getCurrentName())) {
				if (jp.nextToken() == JsonToken.START_ARRAY) {
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
						way.add(new GeoPoint(lt, ln));
					}
				}
			}
		}
	}

	public static Result parse(JacksonManager.ContentType type, InputStream stream) throws IOException {
		Result result = new Result();
		ArrayList<GeoPoint> way = new ArrayList<GeoPoint>();
		ArrayList<Node> points = new ArrayList<Node>();

		ObjectMapper mapper = JacksonManager.getMapper(type);

		JsonParser jp = mapper.getJsonFactory().createJsonParser(stream);
		try {
			jacksonParse(jp, way, points);
		} finally {
			jp.close();
		}

		result.way = new GeoPoint[][]{way.toArray(new GeoPoint[way.size()])};
		result.points = points;

		return result;
	}
}
