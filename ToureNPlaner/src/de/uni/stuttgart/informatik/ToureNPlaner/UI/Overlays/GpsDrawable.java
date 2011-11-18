package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.*;
import android.graphics.drawable.Drawable;

public class GpsDrawable extends Drawable{
	private final Paint paint;

	public GpsDrawable(Paint paint) {
		this.paint = paint;
	}

	@Override
	public void draw(Canvas canvas) {
		Rect bounds = this.getBounds();
        //add a line for the circle
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width()/2, paint);
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
