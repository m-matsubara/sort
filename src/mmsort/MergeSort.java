/*
 * Merge sort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class MergeSort implements ISortAlgorithm {
	/**
	 * マージソート
	 * 作業用一時領域はソート対象の範囲サイズ / 2（切り上げ）が必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param workArray work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void mergeSort(final T[] array, final int from, final int to, final T[] workArray, final Comparator<? super T> comparator)
	{
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

		int mid = from + (to - from) / 2;	//	中央位置（範囲１と範囲２の境界）
		mergeSort(array, from, mid, workArray, comparator);	//	範囲１（最小位置～中間位置）のソート
		mergeSort(array, mid, to, workArray, comparator);	//	範囲２（中間位置～最大位置）のソート

		int idx = from;		//	現在処理中の位置（範囲１と範囲２の小さい方をこの位置へ配置（移動）する）
		int idx1 = from;	//	範囲１の次の値のインデックス
		int idx2 = mid;		//	範囲２の次の値のインデックス

		if (comparator.compare(array[mid - 1], array[mid]) < 0)
			return;			//	範囲１の値はすべて範囲２の値より小さかった（再配置なし）…ソート済みの配列に対する高速化

		//	範囲１をワーク配列にコピー
		if (mid - idx1 > 0) {
			System.arraycopy(array, idx1, workArray, idx1 - from, mid - idx1);
		}

		//	ワーク領域（範囲１のコピー）と範囲２をマージしてソート対象(array)の先頭から詰めていく
		idx = idx1;	//	現在処理中の位置を設定
		while (idx1 < mid && idx2 < to)  {
			final T value1 = workArray[idx1 - from];
			final T value2 = array[idx2];
			if (comparator.compare(value1, value2) <= 0) {	// virtual code : (value2 < value1) == false
				array[idx] = value1;
				idx1++;
			} else {
				array[idx] = value2;
				idx2++;
			}
			idx++;
		}

		//	残ったワーク領域をソート対象へ詰める
		while (idx1 < mid)  {
			array[idx] = workArray[idx1 - from];
			idx++;
			idx1++;
		}

//		if (idx != idx2)
//			throw new RuntimeException("Position error");
	}


	/**
	 * Merge sort
	 * マージソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void mergeSort(T[] array, int from, int to, Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] workArray = (T[])new Object[(to - from) / 2];

		mergeSort(array, from, to, workArray, comparator);
	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		mergeSort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "Merge Sort";
	}
}
