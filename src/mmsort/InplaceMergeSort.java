/*
 * In-place Merge sort
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class InplaceMergeSort {
	/**
	 * Binary search (head position of the same value)
	 * 二分探索（同じ値がるときは先頭位置）
	 * @param key search key / 探索するキー
	 * @param array search target / 探索対象
	 * @param from index of first element / 探索対象の開始位置
	 * @param to index of last element (exclusive) / 探索対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 * @return search result (element index) / 探索結果（添え字）
	 */
	public static final <T> int binSearchH(final T key, final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		int fromIdx = from;
		int toIdx = to;
		int curIdx = (fromIdx + toIdx) / 2;
		while (fromIdx < toIdx) {
			final int compVal = comparator.compare(key, array[curIdx]);
			if (compVal <= 0) {
				toIdx = curIdx;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			} else {
				fromIdx = curIdx + 1;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			}
		}
		return curIdx;
	}

	/**
	 * Binary search (tail position of the same value)
	 * 二分探索（同じ値がるときは最終位置の後ろ）
	 * @param key search key / 探索するキー
	 * @param array search target / 探索対象
	 * @param from index of first element / 探索対象の開始位置
	 * @param to index of last element (exclusive) / 探索対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 * @return search result (element index) / 探索結果（添え字）
	 */
	public static final <T> int binSearchT(final T key, final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		int fromIdx = from;
		int toIdx = to;
		int curIdx = (fromIdx + toIdx) / 2;
		while (fromIdx < toIdx) {
			final int compVal = comparator.compare(key, array[curIdx]);
			if (compVal < 0) {
				toIdx = curIdx;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			} else {
				fromIdx = curIdx + 1;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			}
		}
		return curIdx;
	}

	/**
	 * rotate
	 * ローテーション
	 * @param array rotate target / ローテーション対象
	 * @param start index of rotate first element / ローテーションの開始位置
	 * @param range1 size of rotation range 1 / ローテーション範囲1のサイズ
	 * @param range2 size of rotation range 2 /  ローテーション範囲2のサイズ
	 */
	public static final <T> void rotateRange(final T[] array, final int start, final int range1, final int range2) {
		int pos = start;
		T posObject = array[pos];
		int startPos = pos;
		int range = range1 + range2;
		for (int idx = 0; idx < range; idx++) {
			int destPos;
			if (pos < start + range1)
				destPos = pos + range2;
			else
				destPos = pos - range1;
			T value = posObject;
			posObject = array[destPos];
			array[destPos] = value;
			pos = destPos;
			if (pos == startPos) {
				pos++;
				posObject = array[pos];
				startPos = pos;
			}
		}
	}


	/**
	 * In-place Merge
	 * インプレース・マージ
	 * @param array merge target / マージ対象
	 * @param from index of range1 first element / マージ対象の開始位置
	 * @param mid index of range2 first element / マージ対象２の開始位置
	 * @param to index of last element (exclusive) / マージ対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void ipMerge(final T[] array, final int from, final int mid, final int to, final Comparator<? super T> comparator)
	{
		if ((mid - from == 0) || (to - mid == 0))
			return;
		if (comparator.compare(array[mid - 1], array[mid]) < 0)
			return;
		if ((mid - from == 1) && (to - mid == 1)) {
			if (comparator.compare(array[from], array[mid]) > 0) {
				T work = array[from];
				array[from] = array[mid];
				array[mid] = work;
			}
			return;
		}

		if (mid - from == 1) {
			T key = array[from];
			int pos = binSearchH(key, array, from + 1, to, comparator);
			if (pos != from + 1)
				rotateRange(array, from, 1, pos - from - 1);
			return;
		}
		if (to - mid == 1) {
			T key = array[to - 1];
			int pos = binSearchT(key, array, from, to - 1, comparator);
			if (pos != to - 1)
				rotateRange(array, pos, to - 1 - pos, 1);
			return;
		}

		int idxRotate1 = from + (mid - from) / 2;
		T pivot = array[idxRotate1];
		int idxRotate2 = binSearchH(pivot, array, mid, to, comparator);
		int range1 = mid - idxRotate1;
		int range2 = idxRotate2 - mid;
		rotateRange(array, idxRotate1, range1, range2);
		ipMerge(array, from, idxRotate1, idxRotate1 + range2, comparator);
		ipMerge(array, idxRotate1 + range2, idxRotate2, to, comparator);
	}


	/**
	 * In-place Merge sort
	 * インプレース・マージソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void ipMergeSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
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
		} else if (range < 200) {
			BinInsertSort.binInsertSort(array, from, to, comparator);
			return;
		}

		int mid = (from + to) / 2;	//	中央位置（範囲１と範囲２の境界）
		ipMergeSort(array, from, mid, comparator);	//	範囲１（最小位置～中間位置）のソート
		ipMergeSort(array, mid, to, comparator);	//	範囲２（中間位置～最大位置）のソート

		ipMerge(array, from, mid, to, comparator);
	}
}
