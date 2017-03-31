/*
 * HeapSort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2017 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class HeapSort implements ISortAlgorithm {
	public static <T> void makeHeap(final T[] array, final int from, int to, final Comparator<? super T> comparator) {
		int range = to - from;

		//	array[from] が最大の値となるヒープツリーを形成
		int idx = range - 1;
		while (idx > 0) {
			int parent = ((idx + 1) >> 1) - 1;
			if (comparator.compare(array[from + parent], array[from + idx]) < 0) {
				int idx2 = idx;
				do {
					final T temp = array[from + parent];
					array[from + parent] = array[from + idx2];
					array[from + idx2] = temp;
					idx2 = parent;
					parent = ((idx2 + 1) >> 1) - 1;
				} while (idx2 > 0 && comparator.compare(array[from + parent], array[from + idx2]) < 0);
			}
			idx--;
		}
	}

	public static <T> void upHeap(final T[] array, final int from, int idx, final Comparator<? super T> comparator) {
		while (idx > 0) {
			final int parent = ((idx + 1) >> 1) - 1;
			if (comparator.compare(array[from + parent], array[from + idx]) < 0) {
				final T temp = array[from + parent];
				array[from + parent] = array[from + idx];
				array[from + idx] = temp;
			} else
				break;
			idx = parent;
		}
	}

	public static <T> void downHeap(final T[] array, final int from, int to, final Comparator<? super T> comparator) {
		final int range = to - from;
		int idx = 0;
		int changeIdx = idx;
		for (;;) {
			final int child2 = ((idx + 1) << 1);
			final int child1 = child2 - 1;

			if (child1 >= range)
				break;
			if (comparator.compare(array[from + changeIdx], array[from + child1]) < 0) {
				changeIdx = child1;
			}
			if ((child2 < range) && (comparator.compare(array[from + changeIdx], array[from + child2]) < 0)) {
				changeIdx = child2;
			}
			if (changeIdx == idx)
				break;
			T temp = array[from + changeIdx];
			array[from + changeIdx] = array[from + idx];
			array[from + idx] = temp;
			idx = changeIdx;
		}
	}


	public static <T> void sortImpl(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		for (int idx = 1; idx < range; idx++) {
			upHeap(array, from, idx, comparator);
		}

//		makeHeap(array, from, to, comparator);

		for (int idx = range - 1; idx >= 0; idx--) {
			T temp = array[from + idx];
			array[from + idx] = array[from];
			array[from] = temp;

			downHeap(array, from, idx, comparator);
		}
	}
	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		sortImpl(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "HeapSort";
	}

}
