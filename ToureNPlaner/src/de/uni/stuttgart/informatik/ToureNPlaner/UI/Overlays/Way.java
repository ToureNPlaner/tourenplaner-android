package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

import android.graphics.Path;
import android.graphics.Point;
import android.util.FloatMath;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Util.ArrayDeque;
import org.mapsforge.core.GeoPoint;
import org.mapsforge.core.Tile;

import java.util.ArrayList;

class Way {
	private static int smallestLevelSize = 16;
	// Must be factors of smallest size
	static final int[] steps = {16, 16, 16, 16, 16, 16, 16, 16, 16, 8, 8, 4, 4, 2, 2, 1, 1, 1, 1, 1};

	private final float[] way;
	private final float[] cache;
	private Level root;
	private final ArrayDeque<Level> stack = new ArrayDeque<Level>();
	private final ArrayList<Level> draw = new ArrayList<Level>();

	private byte lastZoomLevel = -1;

	public Way(GeoPoint[] p) {
		way = new float[p.length * 2];
		cache = new float[p.length * 2];
		initWay(p);
	}

	private void initWay(GeoPoint[] points) {
		long startTime, endTime;
		startTime = System.nanoTime();

		int pointsLeft = points.length;
		// round to the right size
		ArrayList<Level> levels = new ArrayList<Level>((points.length + smallestLevelSize + 1) / smallestLevelSize);
		int begin = 0, end;

		while (pointsLeft > 0) {
			end = Math.min(begin + smallestLevelSize, points.length);
			int leftMin = Integer.MAX_VALUE, topMax = Integer.MIN_VALUE, rightMax = Integer.MIN_VALUE, bottomMin = Integer.MAX_VALUE;
			for (int i = begin; i < end; i++) {
				leftMin = Math.min(leftMin, points[i].longitudeE6);
				topMax = Math.max(topMax, points[i].latitudeE6);
				rightMax = Math.max(rightMax, points[i].longitudeE6);
				bottomMin = Math.min(bottomMin, points[i].latitudeE6);
				way[i * 2] = (float) points[i].longitudeE6 / 1000000f;
				way[i * 2 + 1] = (float) points[i].latitudeE6 / 1000000f;
			}
			levels.add(new Level(leftMin, topMax, rightMax, bottomMin, begin, end));
			pointsLeft -= smallestLevelSize;
			begin = end;
		}

		ArrayList<Level> upper;
		do {
			upper = new ArrayList<Level>(levels.size() / 2);
			for (int i = 0; i + 1 < levels.size(); i += 2) {
				Level first = levels.get(i), second = levels.get(i + 1);
				upper.add(new Level(first, second));
			}
			if (levels.size() % 2 != 0) {
				upper.add(levels.get(levels.size() - 1));
			}
			levels = upper;
		} while (upper.size() != 1);
		root = upper.get(0);
		endTime = System.nanoTime();
		Log.v("TP", "Initialization took: " + (endTime - startTime) / 1000 + "µs");
	}

	private void clip(int leftE6, int topE6, int rightE6, int bottomE6) {
		long startTime;
		long endTime;
		stack.clear();
		stack.push(root);
		startTime = System.nanoTime();
		while (!stack.isEmpty()) {
			Level level = stack.pop();
			// reject
			if (level.rightE6 < leftE6 ||
					level.leftE6 > rightE6 ||
					level.topE6 < bottomE6 ||
					level.bottomE6 > topE6) {
				continue;
			}
			// accept
			if (level.rightE6 <= rightE6 &&
					level.leftE6 >= leftE6 &&
					level.topE6 <= topE6 &&
					level.bottomE6 >= bottomE6) {
				draw.add(level);
				continue;
			}
			if (level.leftChild != null) {
				stack.push(level.rightChild);
				stack.push(level.leftChild);
				continue;
			}
			draw.add(level);
		}
		endTime = System.nanoTime();
		FastWayOverlay.clipping = (endTime - startTime) / 1000;
	}

	private void updatePath(Path path, Point drawPosition, byte drawZoomLevel) {
		long startTime;
		long endTime;
		startTime = System.nanoTime();
		final int step = steps[drawZoomLevel];
		path.moveTo(cache[draw.get(0).begin * 2] - drawPosition.x, cache[draw.get(0).begin * 2 + 1] - drawPosition.y);
		for (Level lvl : draw) {
			for (int i = lvl.begin; i < lvl.end; i += step) {
				path.lineTo(cache[i * 2] - drawPosition.x, cache[i * 2 + 1] - drawPosition.y);
			}
		}
		endTime = System.nanoTime();
		FastWayOverlay.pathing = (endTime - startTime) / 1000;
	}

	private void updateCache(byte drawZoomLevel) {
		long startTime;
		long endTime;
		FastWayOverlay.numPoints = draw.get(draw.size() - 1).end - draw.get(0).begin;
		startTime = System.nanoTime();
		if (drawZoomLevel != lastZoomLevel) {
			invalidateCache();
			lastZoomLevel = drawZoomLevel;
		}
		final float pi = (float) Math.PI;
		final float f = (float) (Tile.TILE_SIZE << drawZoomLevel);
		final float pi4f = (1.f / (4.f * pi)) * f;
		final float pi180 = (pi / 180.0F);
		final float f360 = 1.f / 360.f * f;
		final float f05 = 0.5f * f;
		final int step = steps[drawZoomLevel];
		for (Level lvl : draw) {
			if (lvl.zoomLevelCached != drawZoomLevel) {
				for (int i = lvl.begin; i < lvl.end; i += step) {
					// longitudeToPixelX
					cache[i * 2] = (way[i * 2] + 180.f) * f360;
					float sinLatitude = FloatMath.sin(way[i * 2 + 1] * pi180);
					float t = (1.f + sinLatitude) / (1.f - sinLatitude);
					cache[i * 2 + 1] = f05 - ((float) Math.log(t)) * pi4f;
				}
				lvl.zoomLevelCached = drawZoomLevel;
			}
		}
		endTime = System.nanoTime();
		FastWayOverlay.caching = (endTime - startTime) / 1000;
	}

	public void setupPath(Path path, Point drawPosition, byte drawZoomLevel, int leftE6, int topE6, int rightE6, int bottomE6) {
		draw.clear();

		clip(leftE6, topE6, rightE6, bottomE6);

		path.rewind();

		if (draw.isEmpty())
			return;

		updateCache(drawZoomLevel);

		updatePath(path, drawPosition, drawZoomLevel);
	}

	private void invalidateCache() {
		stack.clear();
		stack.push(root);
		while (!stack.isEmpty()) {
			Level level = stack.pop();
			level.zoomLevelCached = -1;
			if (level.leftChild != null) {
				stack.push(level.rightChild);
				stack.push(level.leftChild);
			}
		}
	}
}
