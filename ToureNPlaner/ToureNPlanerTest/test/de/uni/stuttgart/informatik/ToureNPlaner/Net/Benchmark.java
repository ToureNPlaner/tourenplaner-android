package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.test.InstrumentationTestCase;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Point;
import de.uni.stuttgart.informatik.ToureNPlaner.tests.R;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
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
        if (SampleResponseFloatJson == null)
            SampleResponseFloatJson = Util.streamToString(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_float));
        if (SampleResponseIntJson == null)
            SampleResponseIntJson = Util.streamToString(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_int));
    }

    public void testOrgJsonFloat() throws Exception {
        final long t0 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            new JSONObject(SampleResponseFloatJson);
        }
        Log.w(TAG, "OrgJsonFloat: " + (System.currentTimeMillis() - t0) / COUNT + " ms");
    }

    public void testJsonFloat() throws Exception {
        JSONArray points = new JSONObject(SampleResponseFloatJson).getJSONArray("points");

        final long t0 = System.currentTimeMillis();
        for (int i = 0; i < COUNT * 10; i++) {
            ArrayList<GeoPoint> array = new ArrayList<GeoPoint>(points.length());
            for (int j = 0; j < points.length(); j++) {
                array.add(Point.parseFloat(points.getJSONObject(j)));
            }
        }
        Log.w(TAG, "JsonFloat: " + (System.currentTimeMillis() - t0) / COUNT / 10 + " ms");
    }

    public void testOrgJsonInt() throws Exception {
        final long t0 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            new JSONObject(SampleResponseIntJson);
        }
        Log.w(TAG, "OrgJsonInt: " + (System.currentTimeMillis() - t0) / COUNT + " ms");
    }

    public void testJsonInt() throws Exception {
        JSONArray points = new JSONObject(SampleResponseIntJson).getJSONArray("points");

        final long t0 = System.currentTimeMillis();
        for (int i = 0; i < COUNT * 10; i++) {
            ArrayList<GeoPoint> array = new ArrayList<GeoPoint>(points.length());
            for (int j = 0; j < points.length(); j++) {
                array.add(Point.parseInt(points.getJSONObject(j)));
            }
        }
        Log.w(TAG, "JsonInt: " + (System.currentTimeMillis() - t0) / COUNT / 10 + " ms");
    }

    public void testJsonIntComplete() throws Exception {
        final long t0 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            SampleResponseIntJson = Util.streamToString(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_float));
            JSONArray points = new JSONObject(SampleResponseIntJson).getJSONArray("points");

            ArrayList<GeoPoint> array = new ArrayList<GeoPoint>(points.length());
            for (int j = 0; j < points.length(); j++) {
                array.add(Point.parseInt(points.getJSONObject(j)));
            }
        }
        Log.w(TAG, "JsonIntComplete: " + (System.currentTimeMillis() - t0) / COUNT + " ms");
    }

    public void testJacksonInt() throws Exception {
        JsonFactory f = new JsonFactory();
        final long t0 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            JsonParser jp = f.createJsonParser(SampleResponseIntJson);

            ArrayList<GeoPoint> array = new ArrayList<GeoPoint>();

            jacksonParse(jp, array);
            jp.close(); // ensure resources get cleaned up timely and properly
        }
        Log.w(TAG, "JacksonInt: " + (System.currentTimeMillis() - t0) / COUNT + " ms");
    }

    private void jacksonParse(JsonParser jp, ArrayList<GeoPoint> array) throws Exception {
        JsonToken current;

        while ((current = jp.nextToken()) != JsonToken.END_OBJECT) {
            if ("points".equals(jp.getCurrentName())) {
                current = jp.nextToken(); // move to value, or START_OBJECT/START_ARRAY
                int lt = 0, ln = 0;
                if (current == JsonToken.START_ARRAY) {
                    current = jp.nextToken();
                    while (current != JsonToken.END_ARRAY) {
                        while ((current = jp.nextToken()) != JsonToken.END_OBJECT) {
                            if (jp.getCurrentName().equals("lt")) {
                                jp.nextToken();
                                lt = jp.getIntValue();
                            } else if (jp.getCurrentName().equals("ln")) {
                                jp.nextToken();
                                ln = jp.getIntValue();
                            }
                        }
                        array.add(new GeoPoint(lt, ln));
                        current = jp.nextToken();
                    }
                }
            }
        }
    }

    public void testJackson() throws Exception {
        final long t0 = System.currentTimeMillis();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createJsonParser(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_int));

        ArrayList<GeoPoint> array = new ArrayList<GeoPoint>();

        jacksonParse(jp, array);
        jp.close(); // ensure resources get cleaned up timely and properly

        SampleResponseIntJson = Util.streamToString(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_int));
        JSONArray points = new JSONObject(SampleResponseIntJson).getJSONArray("points");

        ArrayList<GeoPoint> array2 = new ArrayList<GeoPoint>(points.length());
        for (int j = 0; j < points.length(); j++) {
            array2.add(Point.parseInt(points.getJSONObject(j)));
        }

        assertEquals(array2, array);
        Log.w(TAG, "testJackson: " + (System.currentTimeMillis() - t0) + " ms");
    }
}
