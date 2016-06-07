/*
 * Quick sort
 * (Nothing ingenuity)
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class QuickSort implements ISortAlgorithm {
	/**
	 * Quick sort
	 * (Nothing ingenuity)
	 *
	 * クイックソート
	 * （何の工夫もない）
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void quickSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ
		if (range < 2)
			return;

		final T pivot = array[from + range / 2];		//	ピボット値（ソート対象の中央位置）

		int curFrom = from;			//	現在処理中位置の小さい方の位置
		int curTo = to - 1;			//	現在処理中位置の大きい方の位置

		do {
			while (comparator.compare(array[curFrom], pivot) < 0)
				curFrom++;
			while (comparator.compare(pivot, array[curTo]) < 0)
				curTo--;
			if (curFrom <= curTo) {
				final T work = array[curFrom];
				array[curFrom] = array[curTo];
				array[curTo] = work;
				curFrom++;
				curTo--;
			}
		} while (curFrom <= curTo);

		quickSort(array, from, curTo + 1, comparator);
		quickSort(array, curFrom, to, comparator);
	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		quickSort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "Quick Sort";
	}
}
