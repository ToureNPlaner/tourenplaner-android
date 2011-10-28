package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.mapsforge.android.maps.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Result {
    private ArrayList<GeoPoint> points;

    public ArrayList<GeoPoint> getPoints() {
        return points;
    }

    static void jacksonParse(JsonParser jp, ArrayList<GeoPoint> array) throws IOException {
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if ("points".equals(jp.getCurrentName())) {
                double lt = 0, ln = 0;
                if (jp.nextToken() == JsonToken.START_ARRAY) {
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        while (jp.nextToken() != JsonToken.END_OBJECT) {
                            if (jp.getCurrentName().equals("lt")) {
                                jp.nextToken();
                                lt = jp.getDoubleValue();
                            } else if (jp.getCurrentName().equals("ln")) {
                                jp.nextToken();
                                ln = jp.getDoubleValue();
                            }
                        }
                        array.add(new GeoPoint(lt, ln));
                    }
                }
            }
        }
    }

    public static Result parse(InputStream stream) throws IOException {
        Result result = new Result();
        result.points = new ArrayList<GeoPoint>();

        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createJsonParser(stream);

        jacksonParse(jp,result.points);

        return result;
    }
}
