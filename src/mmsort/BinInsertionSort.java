/*
 * Binary Insertion sort
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class BinInsertionSort implements ISortAlgorithm {
	/**
	 * Binary Insertion sort
	 * 二分探索版挿入ソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static <T> void binInsertionSort(T[] array, int from, int to, Comparator<? super T> comparator)
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
		//	前から既に整列済みの部分を省く・逆順の列が続く場合はその部分を反転
		int startIdx = from + 1;
		if (comparator.compare(array[startIdx - 1], array[startIdx]) <= 0) {
			for (startIdx++; startIdx < to; startIdx++) {
				if (comparator.compare(array[startIdx - 1], array[startIdx]) > 0) {
					break;
				}
			}
		} else {
			for (startIdx++; startIdx < to; startIdx++) {
				if (comparator.compare(array[startIdx - 1], array[startIdx]) <= 0) {
					break;
				}
			}
			int fromIdx = from;
			int toIdx = startIdx - 1;
			while (fromIdx < toIdx) {
				T temp = array[fromIdx];
				array[fromIdx] = array[toIdx];
				array[toIdx] = temp;
				fromIdx++;
				toIdx--;
			}
		}

		for (int i = startIdx; i < to; i++) {
			T key = array[i];
			int fromIdx = from;
			int toIdx = i;
			int curIdx = (fromIdx + toIdx) / 2;
			while (fromIdx < toIdx) {
				final int compVal = comparator.compare(key, array[curIdx]);
				if (compVal < 0) {
					toIdx = curIdx;
				} else {
					fromIdx = curIdx + 1;
				}
				curIdx = (fromIdx + toIdx) / 2;
			}
			for (int j = i - 1; j >= curIdx; j--) {
				array[j + 1] = array[j];
			}
			array[curIdx] = key;
		}
	}

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		binInsertionSort(array, from, to, comparator);
	}

	public boolean isStable()
	{
		return true;
	}

	public String getName()
	{
		return "Binary Insert Sort";
	}
}