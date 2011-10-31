package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.test.InstrumentationTestCase;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.tests.R;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.mapsforge.android.maps.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;

public class Jackson extends InstrumentationTestCase {
    final static String TAG = "TP";
    final static int COUNT = 10;

    static String SampleResponseFloatJson = null;
    static String SampleResponseIntJson = null;

    protected void setUp() throws Exception {
        if (SampleResponseFloatJson == null)
            SampleResponseFloatJson = Util.streamToString(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_float));
        if (SampleResponseIntJson == null)
            SampleResponseIntJson = Util.streamToString(getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_int));
    }

    void jacksonParse(JsonParser jp, ArrayList<GeoPoint> array) throws IOException {
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

    public void testJacksonFloatNormal() throws Exception {
        final long t0 = System.currentTimeMillis();
        for(int i = 0;i<COUNT;i++) {
            ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
            JsonFactory f = new JsonFactory();
            JsonParser jp = f.createJsonParser(SampleResponseFloatJson);
            jacksonParse(jp, points);
        }
        Log.w(TAG, "JacksonFloatNormal: " + (System.currentTimeMillis() - t0)/COUNT + " ms");
    }
    
    public void testJacksonFloatPreAllocSmall() throws Exception {
        final long t0 = System.currentTimeMillis();
        for(int i = 0;i<COUNT;i++) {
            ArrayList<GeoPoint> points = new ArrayList<GeoPoint>(10);
            JsonFactory f = new JsonFactory();
            JsonParser jp = f.createJsonParser(SampleResponseFloatJson);
            jacksonParse(jp, points);
        }
        Log.w(TAG, "testJacksonFloatPreAllocSmall: " + (System.currentTimeMillis() - t0)/COUNT + " ms");
    }
    
    public void testJacksonFloatPreAllocMiddle() throws Exception {
        final long t0 = System.currentTimeMillis();
        for(int i = 0;i<COUNT;i++) {
            ArrayList<GeoPoint> points = new ArrayList<GeoPoint>(50);
            JsonFactory f = new JsonFactory();
            JsonParser jp = f.createJsonParser(SampleResponseFloatJson);
            jacksonParse(jp, points);
        }
        Log.w(TAG, "testJacksonFloatPreAllocMiddle: " + (System.currentTimeMillis() - t0)/COUNT + " ms");
    }
    
    public void testJacksonFloatPreAllocLarge() throws Exception {
        final long t0 = System.currentTimeMillis();
        for(int i = 0;i<COUNT;i++) {
            ArrayList<GeoPoint> points = new ArrayList<GeoPoint>(100);
            JsonFactory f = new JsonFactory();
            JsonParser jp = f.createJsonParser(SampleResponseFloatJson);
            jacksonParse(jp, points);
        }
        Log.w(TAG, "testJacksonFloatPreAllocLarge: " + (System.currentTimeMillis() - t0)/COUNT + " ms");
    }

    public void testJacksonFloatNoIntern() throws Exception {
        final long t0 = System.currentTimeMillis();
        for(int i = 0;i<COUNT;i++) {
            ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
            JsonFactory f = new JsonFactory();
            f.configure(JsonParser.Feature.INTERN_FIELD_NAMES,false);
            JsonParser jp = f.createJsonParser(SampleResponseFloatJson);
            jacksonParse(jp, points);
        }
        Log.w(TAG, "JacksonFloatNoIntern: " + (System.currentTimeMillis() - t0)/COUNT + " ms");
    }
}
