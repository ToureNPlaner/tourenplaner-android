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

import android.location.Location;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import java.util.Locale;

public class TBTNavigation implements TextToSpeech.OnInitListener {
	private static TextToSpeech tts;
	public static void say(String s) {
		tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}

	public TBTNavigation() {
		tts = new TextToSpeech(ToureNPlanerApplication.getContext(), this);
	}

	public void startTBT() {
		active = true;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				say("Text to Speech initialized!");
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	public void updatedLocation(Location l) {
		say("Location updated with " + l.getAccuracy() + " meter accuracy");
	}

	//TODO: disable per default
	private  boolean active = true;
	public boolean currentlyRunning() {
		return active;
	}
}
