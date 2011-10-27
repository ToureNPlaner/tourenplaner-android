package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapsforge.android.maps.GeoPoint;

import java.util.ArrayList;

public class Result {
    private ArrayList<GeoPoint> points;

    public ArrayList<GeoPoint> getPoints() {
        return points;
    }

    public static Result parse(JSONObject jsonObject) throws JSONException {
        Result result = new Result();

        JSONObject misc = jsonObject.getJSONObject("misc");

        JSONArray points = jsonObject.getJSONArray("points");

        result.points = new ArrayList<GeoPoint>(points.length());
        for(int i = 0;i<points.length();i++) {
            result.points.add(Point.parseFloat(points.getJSONObject(i)));
        }

        return result;
    }
}
