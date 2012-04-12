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
