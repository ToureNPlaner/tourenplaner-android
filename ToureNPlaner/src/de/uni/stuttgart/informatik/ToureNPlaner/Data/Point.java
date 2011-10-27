package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapsforge.android.maps.GeoPoint;

public class Point {

    public static GeoPoint parseFloat(JSONObject object) throws JSONException {
        return new GeoPoint(object.getDouble("lt"), object.getDouble("ln"));
    }

    public static GeoPoint parseInt(JSONObject object) throws JSONException {
        return new GeoPoint(object.getInt("lt"), object.getInt("ln"));
    }
}
