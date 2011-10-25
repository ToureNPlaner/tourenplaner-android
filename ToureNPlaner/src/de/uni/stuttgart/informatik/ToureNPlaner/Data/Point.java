package de.uni.stuttgart.informatik.ToureNPlaner.Data;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapsforge.android.maps.GeoPoint;

public class Point {

    public static GeoPoint parse(JSONObject object) throws JSONException {
        return new GeoPoint(object.getDouble("lt"),object.getDouble("ln"));
       }
}
