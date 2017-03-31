/*
 * Binary Insertion sort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class BinInsertionSort implements ISortAlgorithm {
	/**
	 * Binary Insertion sort
	 *
	 * 二分探索版挿入ソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static <T> void sortImpl(final T[] array, final int from, final int to, Comparator<? super T> comparator)
	{
/* 必要に応じてコメントアウト解除
		final int range = to - from;
		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[from + 1], array[from]) < 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[from + 1], array[from]) < 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			if (comparator.compare(array[from + 2], array[from + 1]) < 0) {
				T work = array[from + 1];
				array[from + 1] = array[from + 2];
				array[from + 2] = work;
				if (comparator.compare(array[from + 1], array[from]) < 0) {
					work = array[from];
					array[from] = array[from + 1];
					array[from + 1] = work;
				}
			}
			return;
		}
*/
		int startIdx = from + 1;
/* 必要に応じてコメントアウト解除
		// Omit the alignment already part
		// If the reverse order of the column continues to invert the part
		// 既に整列済みの部分を省く
		// 逆順の列が続く場合はその部分を反転
		if (comparator.compare(array[startIdx - 1], array[startIdx]) <= 0) {			// virtual code : ((array[startIdx] < array[startIdx - 1]) == false)
			for (startIdx++; startIdx < to; startIdx++) {
				if (comparator.compare(array[startIdx - 1], array[startIdx]) > 0) {		// virtual code : array[startIdx] < array[startIdx - 1]
					break;
				}
			}
		} else {
			for (startIdx++; startIdx < to; startIdx++) {
				if (comparator.compare(array[startIdx - 1], array[startIdx]) <= 0) {	// virtual code : ((array[startIdx] < array[startIdx - 1]) == false)
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
*/
		// Binary Insertion Sort body
		// バイナリインサーションソート本体
		for (int i = startIdx; i < to; i++) {
			T key = array[i];
			int fromIdx = from;
			int toIdx = i;
			int curIdx = fromIdx + ((toIdx - fromIdx) >> 1);
			while (fromIdx < toIdx) {
				if (comparator.compare(key, array[curIdx]) < 0) {
					toIdx = curIdx;
				} else {
					fromIdx = curIdx + 1;
				}
				curIdx = fromIdx + ((toIdx - fromIdx) >> 1);
			}
			if (i != curIdx) {
				for (int j = i - 1; j >= curIdx; j--) {
					array[j + 1] = array[j];
				}
				array[curIdx] = key;
			}
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
		return true;
	}

	@Override
	public String getName()
	{
		return "Binary Insert Sort";
	}
}
