/*
 * mmsSort (Half work memory edition)
 *
 * Stable Dual-pivot Quicksort (and Merge sort)
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2016 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class MmsSortH implements ISortAlgorithm {
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
	 * マージソート（分割した２つの領域はmmsSortを使う）
	 * つまりは最上位段だけマージソートで、再起はmmsSort(Dual-pivot Stable Quicksort)となる。
	 * 作業用一時領域はソート対象の範囲サイズ / 2（切り上げ）が必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param workArray work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void sortImpl(final T[] array, final int from, final int to, final T[] workArray, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが5以下のときは特別扱い
		if (range <= 5) {
			sort5(array, from, to, comparator);
			//BinInsertionSort.sortImpl(array, from, to, comparator);
			return;
		}

		int mid = from + (to - from) / 2;	//	中央位置（範囲１と範囲２の境界）
		MmsSort.sortImpl(array, from, mid, workArray, 40, comparator);	//	範囲１（最小位置～中間位置）のソート
		MmsSort.sortImpl(array, mid, to, workArray, 40, comparator);	//	範囲２（中間位置～最大位置）のソート
		int idx = from;		//	現在処理中の位置（範囲１と範囲２の小さい方をこの位置へ配置（移動）する）
		int idx1 = from;	//	範囲１の次の値のインデックス
		int idx2 = mid;		//	範囲２の次の値のインデックス

		if (comparator.compare(array[mid - 1], array[mid]) <= 0)
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
	}


	/**
	 * Merge sort
	 * マージソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void sortImpl(T[] array, int from, int to, Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] workArray = (T[])new Object[(to - from + 1) / 2];
		//final T[] workArray = (T[])new Object[(to - from)];

		sortImpl(array, from, to, workArray, comparator);
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
		return "mmsSort (Half memory)";
	}
}
