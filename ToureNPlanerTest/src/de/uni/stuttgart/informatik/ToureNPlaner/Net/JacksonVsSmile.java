package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import android.test.InstrumentationTestCase;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.tests.R;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.smile.SmileFactory;
import org.mapsforge.core.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JacksonVsSmile extends InstrumentationTestCase {
	final static String TAG = "TP";
	final static int COUNT = 10;

	static byte[] SampleResponseIntJson = null;
	static byte[] SampleResponseIntSmile = null;

	protected void setUp() throws Exception {
		if (SampleResponseIntJson == null) {
			InputStream stream = getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_int_new);
			stream.read(SampleResponseIntJson = new byte[stream.available()]);
		}
		if (SampleResponseIntSmile == null) {
			InputStream stream = getInstrumentation().getContext().getResources().openRawResource(R.raw.sample_response_int_binary);
			stream.read(SampleResponseIntSmile = new byte[stream.available()]);
		}
	}

	public void testJsonInt() throws Exception {
		JsonFactory f = new JsonFactory();
		final long t0 = System.currentTimeMillis();
		ArrayList<GeoPoint> array = null;
		for (int i = 0; i < COUNT; i++) {
			JsonParser jp = f.createJsonParser(SampleResponseIntJson);

			array = new ArrayList<GeoPoint>();

			jacksonParse(jp, array);
			jp.close(); // ensure resources get cleaned up timely and properly
		}
		Log.w(TAG, "JsonInt: " + array.size() + " " + (System.currentTimeMillis() - t0) / COUNT + " ms");
	}

	public void testSmileInt() throws Exception {
		JsonFactory f = new SmileFactory();
		final long t0 = System.currentTimeMillis();
		ArrayList<GeoPoint> array = null;
		for (int i = 0; i < COUNT; i++) {
			JsonParser jp = f.createJsonParser(SampleResponseIntSmile);

			array = new ArrayList<GeoPoint>();

			jacksonParse(jp, array);
			jp.close(); // ensure resources get cleaned up timely and properly
		}
		Log.w(TAG, "SmileInt: " + array.size() + " " + (System.currentTimeMillis() - t0) / COUNT + " ms");
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
								lt = jp.getIntValue() / 10;
							} else if (jp.getCurrentName().equals("ln")) {
								jp.nextToken();
								ln = jp.getIntValue() / 10;
							}
						}
						// TODO
						//array.add(new GeoPoint(lt, ln));
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
								lt = jp.getIntValue() / 10;
							} else if (jp.getCurrentName().equals("ln")) {
								jp.nextToken();
								ln = jp.getIntValue() / 10;
							}
						}
						array.add(new GeoPoint(lt, ln));
					}
				}
			}
		}
	}
}
