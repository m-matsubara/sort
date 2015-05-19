/*
 * Insertion sort
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class InsertSort {
	/**
	 * Insertion sort
	 * 挿入ソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void insertSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			if (comparator.compare(array[from + 1], array[from + 2]) > 0) {
				T work = array[from + 1];
				array[from + 1] = array[from + 2];
				array[from + 2] = work;
			if (comparator.compare(array[from], array[from + 1]) > 0) {
					work = array[from];
					array[from] = array[from + 1];
					array[from + 1] = work;
			}
			}
			return;
		}
		for (int i = from; i < to; i++) {
			final T key = array[i];
			int j = i - 1;
			while (j >= from && comparator.compare(array[j], key) > 0) {
				array[j + 1] = array[j];
				j--;
			}
			array[j + 1] = key;
		}
	}


}
