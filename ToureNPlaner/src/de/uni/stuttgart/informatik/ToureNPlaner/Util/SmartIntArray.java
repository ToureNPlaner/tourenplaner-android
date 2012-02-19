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