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

package de.uni.stuttgart.informatik.ToureNPlaner.Util;

public class SmartIntArray {
	int sp = 0; // "stack pointer" to keep track of position in the array
	private int[] array;

	public SmartIntArray() {
		this(16);
	}

	public SmartIntArray(int initialSize) {
		array = new int[initialSize];
	}

	public int size() {
		return sp;
	}

	public int get(int i) {
		return array[i];
	}

	public void add(int i) {
		if (sp >= array.length) // time to grow!
		{
			int[] tmpArray = new int[array.length * 2];
			System.arraycopy(array, 0, tmpArray, 0, array.length);
			array = tmpArray;
		}
		array[sp] = i;
		sp += 1;
	}

	public int[] toArray() {
		int[] trimmedArray = new int[sp];
		System.arraycopy(array, 0, trimmedArray, 0, trimmedArray.length);
		return trimmedArray;
	}
}
