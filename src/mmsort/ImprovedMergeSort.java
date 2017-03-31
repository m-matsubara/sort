/*
 * Improved Merge sort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class ImprovedMergeSort implements ISortAlgorithm {
	public static final <T> void sort5(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		if (range <= 2) {
			if (range == 2) {
				if (comparator.compare(array[from], array[from + 1]) > 0) {
					final T temp = array[from];
					array[from] = array[from + 1];
					array[from + 1] = temp;
				}
			}
			return ;
		}

		final int p1 = from;
		final int p2 = from + 1;
		final int p3 = from + 2;
		final int p4 = from + 3;
		final int p5 = from + 4;
		final T v1 = array[p1];
		final T v2 = array[p2];
		final T v3 = array[p3];
		final T v4 = range >= 4 ? array[p4] : null;
		final T v5 = range >= 5 ? array[p5] : null;

		//	まず、先頭３つのソート
		if (comparator.compare(v1, v2) <= 0) {
			if (comparator.compare(v2, v3) <= 0) {
				// v1 <= v2 <= v3
				//array[p1] = v1;
				//array[p2] = v2;
				//array[p3] = v3;
			} else if (comparator.compare(v1, v3) <= 0) {
				// v1 <= v3 <= v2
				//array[p1] = v1;
				array[p2] = v3;
				array[p3] = v2;
			} else {
				// v3 <= v1 <= v2
				array[p1] = v3;
				array[p2] = v1;
				array[p3] = v2;
			}
		} else {
			if (comparator.compare(v1, v3) <= 0) {
				// v2 <= v1 <= v3
				array[p1] = v2;
				array[p2] = v1;
				//array[p3] = v3;
			} else if (comparator.compare(v2, v3) <= 0) {
				// v2 <= v3 <= v1
				array[p1] = v2;
				array[p2] = v3;
				array[p3] = v1;
			} else {
				// v3 <= v2 <= v1
				array[p1] = v3;
				//array[p2] = v2;
				array[p3] = v1;
			}
		}

		if (range >= 4) {
			// v4 ( = array[p4]) を挿入ソートっぽく指定位置に挿入
			if (comparator.compare(array[p2], v4) <= 0) {
				if (comparator.compare(array[p3], v4) <= 0) {
					// array[p3] <= v4
				} else {
					// array[p2] <= v4 < array[p3];
					array[p4] = array[p3];
					array[p3] = v4;
				}
			} else {
				if (comparator.compare(array[p1], v4) <= 0) {
					// array[p1] <= v4 < array[p2];
					array[p4] = array[p3];
					array[p3] = array[p2];
					array[p2] = v4;
				} else {
					// v4 < array[p1] <= array[p2];
					array[p4] = array[p3];
					array[p3] = array[p2];
					array[p2] = array[p1];
					array[p1] = v4;
				}
			}

			if (range >= 5) {
				// v5 ( = array[p5]) を挿入ソートっぽく指定位置に挿入
				if (comparator.compare(array[p3], v5) <= 0) {
					// array[p3] <= v5
					if (comparator.compare(array[p4], v5) <= 0) {
						// array[p3] <= array[4] <= v5
					} else {
						// array[p3] <= v5 < array[p4]
						array[p5] = array[p4];
						array[p4] = v5;
					}
				} else {
					// v5 < array[p3]
					if (comparator.compare(array[p2], v5) <= 0) {
						// array[p2] <= v5 < array[p3]
						array[p5] = array[p4];
						array[p4] = array[p3];
						array[p3] = v5;
					} else {
						// v5 < array[p2] <= array[p3]
						if (comparator.compare(array[p1], v5) <= 0) {
							// array[p1] <= v5 < array[p2] <= array[p3]
							array[p5] = array[p4];
							array[p4] = array[p3];
							array[p3] = array[p2];
							array[p2] = v5;
						} else {
							// v5 < array[p1] <= array[p2] <= array[p3]
							array[p5] = array[p4];
							array[p4] = array[p3];
							array[p3] = array[p2];
							array[p2] = array[p1];
							array[p1] = v5;
						}
					}
				}
			}
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
	 * @param works work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void sortImpl(final T[] arrayFrom, final T[] arrayTo, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		/*
		if (range <= 1) {
			arrayTo[from] = arrayFrom[from];
			return;
		} else if (range == 2) {
			if (comparator.compare(arrayFrom[from + 1], arrayFrom[from]) < 0) {
				arrayTo[from]     = arrayFrom[from + 1];
				arrayTo[from + 1] = arrayFrom[from];
			} else {
				arrayTo[from]     = arrayFrom[from];
				arrayTo[from + 1] = arrayFrom[from + 1];
			}
			return;
		} else if (range == 3) {
			System.arraycopy(arrayFrom, from, arrayTo, from, to - from);
			if (comparator.compare(arrayTo[from + 1], arrayTo[from]) < 0) {
				T work = arrayTo[from];
				arrayTo[from] = arrayTo[from + 1];
				arrayTo[from + 1] = work;
			}
			if (comparator.compare(arrayTo[from + 2], arrayTo[from + 1]) < 0) {
				T work = arrayTo[from + 1];
				arrayTo[from + 1] = arrayTo[from + 2];
				arrayTo[from + 2] = work;
				if (comparator.compare(arrayTo[from + 1], arrayTo[from]) < 0) {
					work = arrayTo[from];
					arrayTo[from] = arrayTo[from + 1];
					arrayTo[from + 1] = work;
				}
			}
			return;
		}
		*/
		if (range <= 5) {
			sort5(arrayFrom, from, to, comparator);
			//BinInsertionSort.sortImpl(array, from, to, comparator);
			System.arraycopy(arrayFrom, from, arrayTo, from, range);
			return;
		}

		int mid = from + (to - from) / 2;	//	center position (boundary of range1 and range 2) / 中央位置（範囲１と範囲２の境界）
		sortImpl(arrayTo, arrayFrom, from, mid, comparator);	//	sort of range1(from - center) / 範囲１（最小位置～中間位置）のソート
		sortImpl(arrayTo, arrayFrom, mid,  to,  comparator);	//	sort of range2(center - to) / 範囲２（中間位置～最大位置）のソート

		int idx = from;		//	index of processing / 現在処理中の位置（範囲１と範囲２の小さい方をこの位置へ配置（移動）する）
		int idx1 = from;	//	index of range1 / 範囲１のインデックス
		int idx2 = mid;		//	index of range2 / 範囲２のインデックス

		// merge of workarea and range2, packing to array
		// ワーク領域（範囲１のコピー）と範囲２をマージしてソート対象(array)の先頭から詰めていく
		idx = idx1;	//	現在処理中の位置を設定
		T value1 = arrayFrom[idx1];
		T value2 = arrayFrom[idx2];
		for (;;) {
			if (comparator.compare(value1, value2) <= 0) {		// virtual code : (value2 < value1) == false
				arrayTo[idx++] = value1;
				idx1++;
				if (idx1 >= mid)
					break;
				value1 = arrayFrom[idx1];
			} else {
				arrayTo[idx++] = value2;
				idx2++;
				if (idx2 >= to)
					break;
				value2 = arrayFrom[idx2];
			}
		}

		// Pack the remaining work area to be sorted
		// 残ったワーク領域をソート対象へ詰める
		if (idx1 < mid)
			System.arraycopy(arrayFrom, idx1, arrayTo, idx, mid - idx1);
		if (idx2 < to)
			System.arraycopy(arrayFrom, idx2, arrayTo, idx, to - idx2);
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
	public static final <T> void sortImpl(final T[] array, final int from, final int to, Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] workArray = (T[])new Object[array.length];

		System.arraycopy(array, from, workArray, from, to - from);
		sortImpl(workArray, array, from, to, comparator);
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
		return "Improved Merge Sort";
	}
}
