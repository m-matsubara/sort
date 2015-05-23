/*
 * Quick sort (Median of 3)
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class QuickSortM3 implements ISortAlgorithm {
	/**
	 * Quick sort (Median of 3)
	 * クイックソート（３つのメディアン）
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void quickSortMedian3(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				final T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				final T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			if (comparator.compare(array[from + 1], array[from + 2]) > 0) {
				final T work = array[from + 1];
				array[from + 1] = array[from + 2];
				array[from + 2] = work;
				if (comparator.compare(array[from], array[from + 1]) > 0) {
					final T work2 = array[from];
					array[from] = array[from + 1];
					array[from + 1] = work2;
				}
			}
			return;
		}
/*
		if (range < 50) {
			combSort(array, from, to, comparator);
			return;
		}
*/
		T pivot;							//	ピボット値
//		pivot = array[from + range / 2];

		T pivot1 = array[from];				//	ピボット候補１
		T pivot2 = array[from + range / 2];	//	ピボット候補２
		T pivot3 = array[to - 1];			//	ピボット候補３

		//	３つのピボット候補から中央値を取得し、ピボット値とする
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

		int curFrom = from;			//	現在処理中位置の小さい方の位置
		int curTo = to - 1;			//	現在処理中位置の大きい方の位置

		do {
			while (comparator.compare(array[curFrom], pivot) < 0) {
				curFrom++;
			}
			while (comparator.compare(array[curTo], pivot) > 0) {
				curTo--;
			}
			if (curFrom <= curTo) {
				final T work = array[curFrom];
				array[curFrom] = array[curTo];
				array[curTo] = work;
				curFrom++;
				curTo--;
			}
		} while (curFrom <= curTo);

		if (from < curTo + 1) {
			quickSortMedian3(array, from, curTo + 1, comparator);
		}

		if (curFrom < to - 1) {
			quickSortMedian3(array, curFrom, to, comparator);
		}
	}

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		quickSortMedian3(array, from, to, comparator);
	}

	public boolean isStable()
	{
		return false;
	}

	public String getName()
	{
		return "Quick Sort (Median of 3)";
	}
}