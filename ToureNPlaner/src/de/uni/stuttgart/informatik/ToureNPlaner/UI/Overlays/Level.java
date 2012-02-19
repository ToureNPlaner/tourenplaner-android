package de.uni.stuttgart.informatik.ToureNPlaner.UI.Overlays;

class Level {
	final int leftE6, topE6, rightE6, bottomE6;
	// end, the element after the last one!
	final int begin, end;

	Level leftChild, rightChild;

	byte zoomLevelCached = -1;

	public Level(int leftE6, int topE6, int rightE6, int bottomE6, int begin, int end) {
		this.leftE6 = leftE6;
		this.topE6 = topE6;
		this.rightE6 = rightE6;
		this.bottomE6 = bottomE6;
		this.begin = begin;
		this.end = end;
	}

	public Level(Level first, Level second) {
		this(Math.min(first.leftE6, second.leftE6),
				Math.max(first.topE6, second.topE6),
				Math.max(first.rightE6, second.rightE6),
				Math.min(first.bottomE6, second.bottomE6),
				Math.min(first.begin, second.begin),
				Math.max(first.end, second.end));
		leftChild = first;
		rightChild = second;
	}
}
