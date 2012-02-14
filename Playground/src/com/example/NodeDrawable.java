package com.example;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class NodeDrawable extends Drawable {
	private final Paint textPaint;
	private final float offsetX;

	private Drawable image;
	private String label = "";

	public void setImage(Drawable image) {
		this.image = image;
	}

	public NodeDrawable(Drawable image, DisplayMetrics displayMetrics) {
		this.image = image;
		this.offsetX = displayMetrics.density * 5.f;
		this.textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setAntiAlias(true);
		textPaint.setSubpixelText(true);
		textPaint.setTextSize(20.f * displayMetrics.density);
		setBounds(image.getBounds());
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
			canvas.drawText(label, bounds.exactCenterX(), bounds.exactCenterY() + offsetX, textPaint);
		}
	}

	public void setLabel(String label) {
		this.label = label;
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