/*
 * Improved Merge sort
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class ImprovedMergeSort implements ISortAlgorithm {
	/**
	 * Improved Merge sort
	 * (Outputs directly to sort the results in the work area, to return while merge from the work area to the original sequence)
	 * Temporary area for work need the same size as the sort object
	 *
	 * 改良版マージソート
	 * （ソート結果を作業領域に直接出力し、作業領域から元の配列にマージしながら戻す）
	 * 作業用一時領域はソート対象と同じサイズが必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param works work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void improvedMergeSort(final T[] arrayFrom, final T[] arrayTo, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			arrayTo[from] = arrayFrom[from];
			return;
		} else if (range == 2) {
			if (comparator.compare(arrayFrom[from], arrayFrom[from + 1]) > 0) {
				arrayTo[from]     = arrayFrom[from + 1];
				arrayTo[from + 1] = arrayFrom[from];
			} else {
				arrayTo[from]     = arrayFrom[from];
				arrayTo[from + 1] = arrayFrom[from + 1];
			}
			return;
		} else if (range == 3) {
			System.arraycopy(arrayFrom, from, arrayTo, from, to - from);
			if (comparator.compare(arrayTo[from], arrayTo[from + 1]) > 0) {
				T work = arrayTo[from];
				arrayTo[from] = arrayTo[from + 1];
				arrayTo[from + 1] = work;
			}
			if (comparator.compare(arrayTo[from + 1], arrayTo[from + 2]) > 0) {
				T work = arrayTo[from + 1];
				arrayTo[from + 1] = arrayTo[from + 2];
				arrayTo[from + 2] = work;
				if (comparator.compare(arrayTo[from], arrayTo[from + 1]) > 0) {
					work = arrayTo[from];
					arrayTo[from] = arrayTo[from + 1];
					arrayTo[from + 1] = work;
				}
			}
			return;
		} else if (range < 200) {
			System.arraycopy(arrayFrom, from, arrayTo, from, to - from);
			BinInsertSort.binInsertSort(arrayTo, from, to, comparator);
			return;
		}

		int mid = (from + to) / 2;	//	中央位置（範囲１と範囲２の境界）
		improvedMergeSort(arrayTo, arrayFrom, from, mid, comparator);	//	範囲１（最小位置～中間位置）のソート
		improvedMergeSort(arrayTo, arrayFrom, mid, to, comparator);	//	範囲２（中間位置～最大位置）のソート

		int idx = from;		//	現在処理中の位置（範囲１と範囲２の小さい方をこの位置へ配置（移動）する）
		int idx1 = from;	//	範囲１の次の値のインデックス
		int idx2 = mid;		//	範囲２の次の値のインデックス

		//	ワーク領域（範囲１のコピー）と範囲２をマージしてソート対象(array)の先頭から詰めていく
		idx = idx1;	//	現在処理中の位置を設定
		while (idx1 < mid && idx2 < to)  {
			final T value1 = arrayFrom[idx1];
			final T value2 = arrayFrom[idx2];
			if (comparator.compare(value1, value2) <= 0) {
				arrayTo[idx] = value1;
				idx1++;
			} else {
				arrayTo[idx] = value2;
				idx2++;
			}
			idx++;
		}

		//	残ったワーク領域をソート対象へ詰める
		while (idx1 < mid)  {
			arrayTo[idx] = arrayFrom[idx1];
			idx++;
			idx1++;
		}
		while (idx2 < to)  {
			arrayTo[idx] = arrayFrom[idx2];
			idx++;
			idx2++;
		}
	}


	/**
	 * Improved Merge sort
	 * (Outputs directly to sort the results in the work area, to return while merge from the work area to the original sequence)
	 * Temporary area for work need the same size as the sort object
	 *
	 * 改良版マージソート
	 * （ソート結果を作業領域に直接出力し、作業領域から元の配列にマージしながら戻す）
	 * 作業用一時領域はソート対象と同じサイズが必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void improvedMergeSort(final T[] array, final int from, final int to, Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] works = (T[])new Object[array.length];

		System.arraycopy(array, from, works, from, to - from);
		improvedMergeSort(works, array, from, to, comparator);
	}

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		improvedMergeSort(array, from, to, comparator);
	}

	public boolean isStable()
	{
		return true;
	}

	public String getName()
	{
		return "Improved Merge Sort";
	}
}
