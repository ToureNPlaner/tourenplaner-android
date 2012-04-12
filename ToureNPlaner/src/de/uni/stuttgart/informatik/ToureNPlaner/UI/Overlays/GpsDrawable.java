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

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class GpsDrawable extends Drawable {
	private final Paint paint;

	public GpsDrawable(Paint paint) {
		this.paint = paint;
	}

	@Override
	public void draw(Canvas canvas) {
		Rect bounds = this.getBounds();
		//add a line for the circle
		canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2, paint);
	}

	@Override
	public void setAlpha(int i) {

	}

	@Override
	public void setColorFilter(ColorFilter colorFilter) {

	}

	@Override
	public int getOpacity() {
		return 0;
	}
}
