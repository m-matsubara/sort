/*
 * Insertion sort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class InsertionSort implements ISortAlgorithm {
	/**
	 * Insertion sort
	 *
	 * 挿入ソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void insertionSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		for (int idx = from + 1; idx < to; idx++) {
			final T value = array[idx];
			T prevValue = array[idx - 1];
			if (comparator.compare(value, prevValue) < 0) {
				int insIdx = idx;
				do {
					array[insIdx] = prevValue;
					if (--insIdx <= from)
						break;
					prevValue = array[insIdx - 1];
				} while (comparator.compare(value, prevValue) < 0);
				array[insIdx] = value;
			}
		}
	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		insertionSort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "Insertion Sort";
	}
}
