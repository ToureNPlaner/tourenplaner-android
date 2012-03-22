package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import android.graphics.RectF;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import org.mapsforge.core.GeoPoint;

public abstract class GeoCodingHandler extends RawHandler {
	public static class GeoCodingResult {
		public GeoPoint location;
	}

	public GeoCodingHandler(Observer listener) {
		super(listener);
	}

	public static GeoCodingHandler createDefaultHandler(Observer listener, String query, RectF viewbox) {
		return new NominatimHandler(listener, query, viewbox);
	}
}
