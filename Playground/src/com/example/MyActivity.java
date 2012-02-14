package com.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

public class MyActivity extends Activity {
	public static Drawable boundCenterBottom(Drawable balloon) {
		balloon.setBounds(balloon.getIntrinsicWidth() / -2, -balloon.getIntrinsicHeight(),
				balloon.getIntrinsicWidth() / 2, 0);
		return balloon;
	}

	private static String createName(int num) {
		String str = "";

		int modulo;
		num++;
		while (num > 0) {
			modulo = (num - 1) % 26;
			str = Character.toString((char) (modulo + 'A')) + str;
			num = (num - modulo) / 26;
		}

		return str;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView s = new ScrollView(this);
		s.setFillViewport(true);
		s.addView(new SampleView(this));
		setContentView(s);
	}

	private static class SampleView extends View {
		NodeDrawable[][] drawables = new NodeDrawable[3][1000];

		float height, width;

		public SampleView(Context context) {
			super(context);
			setFocusable(true);

			for (int i = 0; i < drawables[0].length; i++) {
				drawables[0][i] = new NodeDrawable(boundCenterBottom(context.getResources().getDrawable(R.drawable.marker)), getResources().getDisplayMetrics());
				drawables[0][i].setLabel(createName(i));
			}
			for (int i = 0; i < drawables[1].length; i++) {
				drawables[1][i] = new NodeDrawable(boundCenterBottom(context.getResources().getDrawable(R.drawable.markerstart)), getResources().getDisplayMetrics());
				drawables[1][i].setLabel(createName(i));
			}
			for (int i = 0; i < drawables[2].length; i++) {
				drawables[2][i] = new NodeDrawable(boundCenterBottom(context.getResources().getDrawable(R.drawable.markerend)), getResources().getDisplayMetrics());
				drawables[2][i].setLabel(createName(i));
			}

			height = (float) drawables[0][0].getBounds().height() * 1.5f;
			width = (float) drawables[0][0].getBounds().width() * 1.5f;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension((int) (width * 4.f), (int) (height * (float) drawables[0].length));
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.translate(width, height);
			for (int n = 0; n < drawables.length; n++) {
				for (int i = 0; i < drawables[n].length; i++) {
					drawables[n][i].draw(canvas);
					canvas.translate(0.f, height);
				}
				canvas.translate(width, -height * (float) drawables[n].length);
			}
		}
	}
}
