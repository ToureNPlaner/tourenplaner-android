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

	public void setType(MarkerType markerType) {
		switch (markerType) {
			case START:
				image = ToureNPlanerApplication.getContext().getResources().getDrawable(R.drawable.markerstart);
				break;
			case MIDDLE:
				image = ToureNPlanerApplication.getContext().getResources().getDrawable(R.drawable.marker);
				break;
			case END:
				image = ToureNPlanerApplication.getContext().getResources().getDrawable(R.drawable.markerend);
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