package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class NodeDrawable extends Drawable {
	private final Paint textPaint;
	private float offsetY;
	private final float density;

	private Drawable image;
	private String label = "";

	/**
	 * Must be of the same size
	 *
	 * @param image
	 */
	public void setImage(Drawable image) {
		this.image = image;
	}

	public NodeDrawable(Drawable image, DisplayMetrics displayMetrics) {
		this.image = image;
		this.density = displayMetrics.density;
		this.textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setAntiAlias(true);
		textPaint.setSubpixelText(true);
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