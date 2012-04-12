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

public class NodeDrawable extends Drawable {
	private final Paint textPaint;
	private float offsetY;

	private Drawable image;
	private String label = "";

	public static enum MarkerType {
		START, MIDDLE, END
	}

	private static Drawable imageMarkerStart = null;
	private static Drawable imageMarker = null;
	private static Drawable imageMarkerEnd = null;

	public void setType(MarkerType markerType) {
		if (imageMarkerStart == null) {
			imageMarkerStart = ToureNPlanerApplication.getContext().getResources().getDrawable(R.drawable.markerstart);
			imageMarker = ToureNPlanerApplication.getContext().getResources().getDrawable(R.drawable.marker);
			imageMarkerEnd = ToureNPlanerApplication.getContext().getResources().getDrawable(R.drawable.markerend);
		}
		switch (markerType) {
			case START:
				image = imageMarkerStart;
				break;
			case MIDDLE:
				image = imageMarker;
				break;
			case END:
				image = imageMarkerEnd;
				break;
		}
	}

	public NodeDrawable(MarkerType markerType) {
		this.textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setAntiAlias(true);
		textPaint.setSubpixelText(true);

		setType(markerType);

		setBounds(0, 0, getIntrinsicWidth(), getIntrinsicHeight());
	}

	@Override
	public void draw(Canvas canvas) {
		Rect imageBounds = image.getBounds();
		Rect bounds = this.getBounds();
		image.setBounds(bounds);
		image.draw(canvas);
		image.setBounds(imageBounds);

		if (label != null) {
			// TODO parameters.textPaint.getFontMetrics()
			canvas.drawText(label, bounds.exactCenterX(), bounds.exactCenterY() + offsetY, textPaint);
		}
	}

	@Override
	public int getIntrinsicWidth() {
		return image.getIntrinsicWidth();
	}

	@Override
	public int getIntrinsicHeight() {
		return image.getIntrinsicHeight();
	}

	public void setLabel(String label) {
		float density = ToureNPlanerApplication.getContext().getResources().getDisplayMetrics().density;
		this.label = label;
		if (label.length() == 1) {
			textPaint.setTextSize(20.f * density);
			offsetY = density * 5.f;
		} else {
			textPaint.setTextSize(15.f * density);
			offsetY = 0.f;
		}
	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
}
