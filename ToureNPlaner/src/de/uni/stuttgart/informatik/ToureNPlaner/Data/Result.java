package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.mapsforge.android.maps.GeoPoint;

import java.io.*;
import java.util.ArrayList;

public class Result implements Serializable {
    private GeoPoint[][] points;

    public GeoPoint[][] getPoints() {
        return points;
    }

    static void jacksonParse(JsonParser jp, ArrayList<GeoPoint> array) throws IOException {
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if ("points".equals(jp.getCurrentName())) {
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
                        array.add(new GeoPoint(lt, ln));
                    }
                }
            }
        }
    }

    public static Result parse(InputStream stream) throws IOException {
        Result result = new Result();
        ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();

        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createJsonParser(stream);

        jacksonParse(jp,points);

        result.points = new GeoPoint[][] {points.toArray(new GeoPoint[points.size()])};

        return result;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(points[0].length);
        for(int i=0;i<points[0].length;i++) {
            out.writeInt(points[0][i].getLatitudeE6());
            out.writeInt(points[0][i].getLongitudeE6());
        }
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int length = in.readInt();
        points = new GeoPoint[1][length];
        for(int i=0;i<points[0].length;i++) {
            int lat = in.readInt();
            int lon = in.readInt();
            points[0][i] = new GeoPoint(lat,lon);
        }
    }


}
