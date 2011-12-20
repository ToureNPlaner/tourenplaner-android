package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

public class NodeDrawable extends Drawable {
	public static class Parameters {
		public final Paint circle;
		public final Paint circleLine;
		public final TextPaint textPaint;

		public Parameters(TextPaint textPaint, Paint circleLine, Paint circle) {
			this.textPaint = textPaint;
			this.circleLine = circleLine;
			this.circle = circle;
		}
	}

    private int color = Color.BLUE;

	private final Parameters parameters;
	
	private String label;

	public NodeDrawable(Parameters parameters, String label) {
        this.label = label;
		this.parameters = parameters;
    }

	@Override
    public void draw(Canvas canvas) {
	    parameters.circle.setColor(color);

	    Rect bounds = this.getBounds();

        //add a line for the circle
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width()/2+1, parameters.circleLine);
        // add a factor to customize the standard radius
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.width()/2, parameters.circle);
        // draw Text on the circle
        // x position depending on amount of numbers

        if (label != null) {
	        // TODO parameters.textPaint.getFontMetrics()
            canvas.drawText(label, bounds.centerX(), bounds.centerY()+6.0f, parameters.textPaint);
        }
    }

	public void setLabel(String label) {
		this.label = label;
	}

	public void setColor(int color) {
        this.color = color;

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
