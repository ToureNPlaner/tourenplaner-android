package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Util;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.smile.SmileFactory;
import org.mapsforge.core.GeoPoint;

import java.io.*;
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
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if ("points".equals(jp.getCurrentName())) {
                int lt = 0, ln = 0;
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
                int lt = 0, ln = 0;
                if (jp.nextToken() == JsonToken.START_ARRAY) {
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        while (jp.nextToken() != JsonToken.END_OBJECT) {
                            if (jp.getCurrentName().equals("lt")) {
                                jp.nextToken();
                                lt = jp.getIntValue()/10;
                            } else if (jp.getCurrentName().equals("ln")) {
                                jp.nextToken();
                                ln = jp.getIntValue()/10;
                            }
                        }
	                    way.add(new GeoPoint(lt, ln));
                    }
                }
            }
        }
    }

    public static Result parse(Util.ContentType type, InputStream stream) throws IOException {
        Result result = new Result();
        ArrayList<GeoPoint> way = new ArrayList<GeoPoint>();
	    ArrayList<Node> points = new ArrayList<Node>();

	    JsonFactory f = null;
	    switch (type) {
		    case JSON:
			    f = new JsonFactory();
			    break;
		    case SMILE:
			    f = new SmileFactory();
			    break;
	    }

        JsonParser jp = f.createJsonParser(stream);

        jacksonParse(jp, way, points);

        result.way = new GeoPoint[][] {way.toArray(new GeoPoint[way.size()])};
	    result.points = points;

        return result;
    }
}
