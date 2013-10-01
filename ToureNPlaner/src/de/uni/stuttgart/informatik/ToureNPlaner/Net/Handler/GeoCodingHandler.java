/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import android.graphics.RectF;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Observer;
import org.mapsforge.core.GeoPoint;

public abstract class GeoCodingHandler extends SimpleNetworkHandler {
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
