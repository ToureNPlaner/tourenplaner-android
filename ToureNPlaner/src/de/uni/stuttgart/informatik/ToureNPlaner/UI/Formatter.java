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

package de.uni.stuttgart.informatik.ToureNPlaner.UI;

import android.content.Context;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

import java.text.DecimalFormat;

public class Formatter {
	public static String formatDistance(Context context, double distance) {
		DecimalFormat f = new DecimalFormat("#0.00");

		String distanceUnit = context.getString(R.string.meter_short);
		if (distance > 1000) {
			distance = distance / 1000;
			distanceUnit = context.getString(R.string.kilometer_short);
		}

		return context.getString(R.string.traveldistance) + ": " + f.format(distance) + " " + distanceUnit;
	}

	public static String formatTime(Context context, double time) {
		DecimalFormat f = new DecimalFormat("#0.00");
		return context.getString(R.string.time) + ": " + f.format(time / 60.) + " " + context.getString(R.string.minute_short);
	}
}
