/*
 * Quick sort (Median of 3)
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class QuickSortM3 implements ISortAlgorithm {
	// Insersion Sortなどに切り替える要素数
	private static final int ALGORITHM_THRESHOLD = 20;

	/**
	 * Quick sort (Median of 3)
	 * クイックソート（３つのメディアン）
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void sortImpl(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ

		//	ソート対象配列サイズが一定数以下のときは特別扱い
		if (range < ALGORITHM_THRESHOLD) {
			InsertionSort.sortImpl(array, from, to, comparator);
			//BinInsertionSort.sortImpl(array, from, to, comparator);
			return;
		}

		T pivot;							// pivot value / ピボット値
//		pivot = array[from + range / 2];

		T pivot1 = array[from];				// pivot candidate 1 / ピボット候補１
		T pivot2 = array[from + range / 2];	// pivot candidate 2 / ピボット候補２
		T pivot3 = array[to - 1];			// pivot candidate 3 / ピボット候補３

		// It obtains the median of the three pivot candidate, and pivoting value
		// ３つのピボット候補から中央値を取得し、ピボット値とする
		if (comparator.compare(pivot1, pivot2) < 0) {
			if (comparator.compare(pivot2, pivot3) < 0) {
				pivot = pivot2;		//	pivot1 < pivot2 < pivot3
			} else if (comparator.compare(pivot1, pivot3) < 0) {
				pivot = pivot3;		//	pivot1 < pivot3 < pivot2
			} else {
				pivot = pivot1;		//	pivot3 < pivot1 < pivot2
			}
		} else {
			if (comparator.compare(pivot1, pivot3) < 0) {
				pivot = pivot1;		//	pivot2 < pivot1 < pivot3
			} else if (comparator.compare(pivot2, pivot3) < 0) {
				pivot = pivot3;		//	pivot2 < pivot3 < pivot1
			} else {
				pivot = pivot2;		//	pivot3 < pivot2 < pivot1
			}
		}

		int curFrom = from;				//	min index / 現在処理中位置の小さい方の位置
		int curTo = to - 1;				//	max index / 現在処理中位置の大きい方の位置
		while (true) {
			while (comparator.compare(array[curFrom++], pivot) < 0);
			while (comparator.compare(pivot, array[curTo--]) < 0);
			curFrom--;
			curTo++;
			if (curFrom >= curTo)
				break;
			T work = array[curFrom];
			array[curFrom++] = array[curTo];
			array[curTo--] = work;
		}

		//	小さいパーティション・大きいパーティションそれぞれで再起
		sortImpl(array, from, curTo + 1, comparator);
		sortImpl(array, curFrom, to, comparator);
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
		return "Quick Sort (Median of 3)";
	}
}
