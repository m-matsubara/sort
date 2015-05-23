/*
 * Comb sort
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class CombSort implements ISortAlgorithm {
	/**
	 * コムソート
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void combSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;	//	ソート範囲サイズ

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

		int gap = range;
		boolean done = false;
		while (!done || gap > 1) {
			gap = gap * 10 / 13;
			if (gap == 9 || gap == 10)
				gap = 11;
			else if (gap == 0)
				gap = 1;
			done = true;
			for (int i = from; i + gap < to; i++) {
				if (comparator.compare(array[i + gap], array[i]) < 0) {
					final T work = array[i];
					array[i] = array[i + gap];
					array[i + gap] = work;
					done = false;
				}
			}
		}
	}

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		combSort(array, from, to, comparator);
	}

	public boolean isStable()
	{
		return false;
	}

	public String getName()
	{
		return "CombSort";
	}
}