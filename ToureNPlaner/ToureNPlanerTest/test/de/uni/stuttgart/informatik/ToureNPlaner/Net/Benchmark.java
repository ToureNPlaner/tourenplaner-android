package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.test.InstrumentationTestCase;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Point;
import de.uni.stuttgart.informatik.ToureNPlaner.tests.R;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mapsforge.android.maps.GeoPoint;

import java.util.ArrayList;

public class Benchmark extends InstrumentationTestCase {
    static String SampleResponseFloatJson = null;
    static String SampleResponseIntJson = null;
    
    final static String TAG = "TP";
    
    final static int COUNT = 10;

    public Benchmark() throws Exception {
        super();
    }

    protected void setUp() throws Exception {
        final long t0 = System.currentTimeMillis();
        if(SampleResponseFloatJson == null)
            SampleResponseFloatJson = Util.streamToString(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_float));
        if(SampleResponseIntJson == null)
            SampleResponseIntJson = Util.streamToString(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_int));
        Log.w(TAG, "Setup: " + (System.currentTimeMillis() - t0) + " ms");
    }
    
    public void testOrgJsonFloat() throws Exception {
        final long t0 = System.currentTimeMillis();
        for(int i=0;i<COUNT;i++) {
            new JSONObject(SampleResponseFloatJson);
        }
        Log.w(TAG, "OrgJsonFloat: " + (System.currentTimeMillis() - t0)/COUNT + " ms");
    }
    
    public void testJsonFloat() throws Exception {
        JSONArray points = new JSONObject(SampleResponseFloatJson).getJSONArray("points");

        final long t0 = System.currentTimeMillis();
        for(int i=0;i<COUNT*10;i++) {
            ArrayList<GeoPoint> array = new ArrayList<GeoPoint>(points.length());
            for(int j = 0;i<points.length();i++) {
                array.add(Point.parseFloat(points.getJSONObject(j)));
            }
        }
        Log.w(TAG, "JsonFloat: " + (System.currentTimeMillis() - t0)/COUNT/10 + " ms");
    }

    public void testOrgJsonInt() throws Exception {
        final long t0 = System.currentTimeMillis();
        for(int i=0;i<COUNT;i++) {
            new JSONObject(SampleResponseIntJson);
        }
        Log.w(TAG, "OrgJsonInt: " + (System.currentTimeMillis() - t0)/COUNT + " ms");
    }

    public void testJsonInt() throws Exception {
            JSONArray points = new JSONObject(SampleResponseIntJson).getJSONArray("points");

            final long t0 = System.currentTimeMillis();
            for(int i=0;i<COUNT*10;i++) {
                ArrayList<GeoPoint> array = new ArrayList<GeoPoint>(points.length());
                for(int j = 0;i<points.length();i++) {
                    array.add(Point.parseInt(points.getJSONObject(j)));
                }
            }
            Log.w(TAG, "JsonInt: " + (System.currentTimeMillis() - t0)/COUNT/10 + " ms");
        }
}
