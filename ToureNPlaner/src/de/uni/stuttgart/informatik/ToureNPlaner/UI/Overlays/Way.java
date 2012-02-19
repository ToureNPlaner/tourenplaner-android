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
	private static int smallestLevelSize = 4;
	// Must be factors of smallest size
	static final int[] steps = {128, 128, 64, 64, 64, 64, 64, 32, 16, 8, 8, 4, 4, 2, 2, 1, 1, 1, 1, 1};

	private final float[] way;
	private final float[] cache;
	private Level root;
	private final ArrayDeque<Level> stack = new ArrayDeque<Level>();
	private final ArrayList<Level> clipped = new ArrayList<Level>();

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
			// add overlap or else we could be between two points, when clipping
			if (end != points.length) {
				leftMin = Math.min(leftMin, points[end].longitudeE6);
				topMax = Math.max(topMax, points[end].latitudeE6);
				rightMax = Math.max(rightMax, points[end].longitudeE6);
				bottomMin = Math.min(bottomMin, points[end].latitudeE6);
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
				clipped.add(level);
				continue;
			}
			if (level.leftChild != null) {
				stack.push(level.rightChild);
				stack.push(level.leftChild);
				continue;
			}
			clipped.add(level);
		}
		endTime = System.nanoTime();
		FastWayOverlay.clipping = (endTime - startTime) / 1000;
	}

	private void updatePath(Path path, Point drawPosition, byte drawZoomLevel) {
		long startTime;
		long endTime;
		startTime = System.nanoTime();
		final int step = steps[drawZoomLevel];
		int drawn = 0;
		int begin = Math.max(clipped.get(0).begin - 1, 0);
		path.moveTo(cache[begin * 2] - drawPosition.x, cache[begin * 2 + 1] - drawPosition.y);
		for (int n = 0; n < clipped.size(); n++) {
			Level lvl = clipped.get(n);
			int i;
			if ((n == 0 && lvl.begin != 0) || (n > 0 && lvl.begin != clipped.get(n - 1).end)) {
				i = lvl.begin - 1;
				path.lineTo(cache[i * 2] - drawPosition.x, cache[i * 2 + 1] - drawPosition.y);
				drawn++;
			}
			for (i = lvl.begin; i < lvl.end; i += step) {
				path.lineTo(cache[i * 2] - drawPosition.x, cache[i * 2 + 1] - drawPosition.y);
				drawn++;
			}
			if (i != lvl.end - 1) {
				i = lvl.end - 1;
				path.lineTo(cache[i * 2] - drawPosition.x, cache[i * 2 + 1] - drawPosition.y);
				drawn++;
			}
			if ((n == (clipped.size() - 1) && lvl.end != root.end) || (n < clipped.size() - 1 && lvl.end != clipped.get(n + 1).begin)) {
				i = lvl.end;
				path.lineTo(cache[i * 2] - drawPosition.x, cache[i * 2 + 1] - drawPosition.y);
				drawn++;
			}
		}
		endTime = System.nanoTime();
		FastWayOverlay.pathing = (endTime - startTime) / 1000;
		FastWayOverlay.pointsDrawn = drawn;
	}

	static final float pi = (float) Math.PI;
	static final float pi180 = (pi / 180.0F);

	private void updateCache(byte drawZoomLevel) {
		long startTime;
		long endTime;
		FastWayOverlay.numPoints = clipped.get(clipped.size() - 1).end - clipped.get(0).begin;
		startTime = System.nanoTime();
		if (drawZoomLevel != lastZoomLevel) {
			invalidateCache();
			lastZoomLevel = drawZoomLevel;
		}
		float f = (float) (Tile.TILE_SIZE << drawZoomLevel);
		float pi4f = (1.f / (4.f * pi)) * f;
		float f360 = 1.f / 360.f * f;
		float f05 = 0.5f * f;
		final int step = steps[drawZoomLevel];

		// update the first before
		for (Level lvl : clipped) {
			if (lvl.zoomLevelCached != drawZoomLevel) {
				lvl.zoomLevelCached = drawZoomLevel;
				if (lvl.begin != 0) {
					updatePoint(lvl.begin - 1, pi4f, f360, f05);
				}
				int i;
				for (i = lvl.begin; i < lvl.end; i += step) {
					updatePoint(i, pi4f, f360, f05);
				}
				if (i != lvl.end - 1) {
					updatePoint(lvl.end - 1, pi4f, f360, f05);
				}
				if (lvl.end != root.end) {
					updatePoint(lvl.end, pi4f, f360, f05);
				}
			}
		}
		endTime = System.nanoTime();
		FastWayOverlay.caching = (endTime - startTime) / 1000;
	}

	private void updatePoint(int i, float pi4f, float f360, float f05) {
		// longitudeToPixelX
		cache[i * 2] = (way[i * 2] + 180.f) * f360;
		float sinLatitude = FloatMath.sin(way[i * 2 + 1] * pi180);
		float t = (1.f + sinLatitude) / (1.f - sinLatitude);
		cache[i * 2 + 1] = f05 - ((float) Math.log(t)) * pi4f;
	}

	public void setupPath(Path path, Point drawPosition, byte drawZoomLevel, int leftE6, int topE6, int rightE6, int bottomE6) {
		clipped.clear();

		clip(leftE6, topE6, rightE6, bottomE6);

		path.rewind();

		if (clipped.isEmpty())
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
