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

import android.graphics.*;
import android.graphics.drawable.Drawable;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

public class GpsDrawable extends Drawable {
	private final Paint paint;
	private Bitmap arrow;

	public GpsDrawable() {
		this.mutate();
		this.arrow = BitmapFactory.decodeResource(ToureNPlanerApplication.getContext().getResources(), R.drawable.arrow);
		this.paint = new Paint();
		this.paint.setAlpha(128);
		this.paint.setAntiAlias(true);
	}

	private double degrees = 0;
	@Override
	public void draw(Canvas canvas) {
		if (canvas != null) {
			//Log.d("tp", "Redraw gps marker with rotation " + degrees);
			Rect bounds = this.getBounds();
			m.reset();
			// the device is pointed x degrees clockwise from north. The arrow should also point x deegrees clockwise
			// from north to show device/car orientation correctly on the map which displays north always on the top
			m.setRotate((float) degrees, arrow.getWidth() / 2, arrow.getHeight() / 2);
			// it's not our responsibility to find out where to draw here, we already get a rectangle "bounds"
			// which is the correct place to draw the arrow
			m.postTranslate(bounds.left - arrow.getWidth() / 2, bounds.top - arrow.getHeight() / 2);
			canvas.drawBitmap(arrow, m, paint);
		}
	}

	@Override
	/**
	 * The Alpha value will be changed on the next redraw of the arrow
	 */
	public void setAlpha(int i) {
		paint.setAlpha(i);
	}

	@Override
	public void setColorFilter(ColorFilter colorFilter) {

	}

	@Override
	public int getOpacity() {
		return 0;
	}

	Matrix m = new Matrix();
	public void setrotation(double degrees) {
		this.degrees = degrees;
	}
}
