/*
 * No6 Sort
 *
 * Stable Dual-pivot Quicksort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2016 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class No6Sort implements ISortAlgorithm {

	/**
	 * 配列中の任意の５か所をソートする
	 * （３番目の要素からピボット値を得るために使用）
	 * @param array sort target / ソート対象
	 * @param p1 index 1 / 添え字1
	 * @param p2 index 2 / 添え字2
	 * @param p3 index 3 / 添え字3
	 * @param p4 index 4 / 添え字4
	 * @param p5 index 5 / 添え字5
	 * @param comparator
	 */
	@SuppressWarnings("unused")
	private static final <T> void sort5(final T[] array, final int p1, final int p2, final int p3, final int p4, final int p5, final Comparator<? super T> comparator)
	{
		final T v1 = array[p1];
		final T v2 = array[p2];
		final T v3 = array[p3];
		final T v4 = array[p4];
		final T v5 = array[p5];

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

	/**
	 * No6 Sort
	 *
	 * 基本的には「 ５つのメディアン」だが、５つの中央値を選択したときに、小さい値２つを配列の先頭側に、大きい２つを配列の最後側に
	 * 退避し、パーティション操作から除外することで高速化を図ったアルゴリズム
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param works work array / 作業用配列
	 * @param depthRemain The remaining number of times of the depth of the call / 呼び出しの深さの残り回数
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void no6Sort(final T[] array, final int from, final int to, final T[] works, final int depthRemain, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ

		//	ソート対象配列サイズが一定数以下のときは特別扱い
		if (range < 20) {
			//InsertionSort.insertionSort(array, from, to, comparator);
			BinInsertionSort.binInsertionSort(array, from, to, comparator);
			return;
		}

		if (depthRemain < 0) {
			MergeSort.mergeSort(array, from, to, works, comparator);
			return;
		}

		final int gap = range / 6;
		final int p1 = from + gap;
		final int p2 = p1 + gap;
		final int p3 = from + range / 2;
		final int p4 = p3 + gap;
		final int p5 = to - gap;
		//sort5(array, p1, p2, p3, p4, p5, comparator);
		T v1 = array[p1];
		T v2 = array[p2];
		T v3 = array[p3];
		T v4 = array[p4];
		T v5 = array[p5];
		T temp;
		//	まず、先頭３つのソート
		if (comparator.compare(v1, v2) <= 0) {
			if (comparator.compare(v2, v3) <= 0) {
				// v1 <= v2 <= v3
			} else if (comparator.compare(v1, v3) <= 0) {
				// v1 <= v3 <= v2
				temp = v2;
				v2 = v3;
				v3 = temp;
			} else {
				// v3 <= v1 <= v2
				temp = v1;
				v1 = v3;
				v3 = v2;
				v2 = temp;
			}
		} else {
			if (comparator.compare(v1, v3) <= 0) {
				// v2 <= v1 <= v3
				temp = v1;
				v1 = v2;
				v2 = temp;
			} else if (comparator.compare(v2, v3) <= 0) {
				// v2 <= v3 <= v1
				temp = v1;
				v1 = v2;
				v2 = v3;
				v3 = temp;
			} else {
				// v3 <= v2 <= v1
				temp = v1;
				v1 = v3;
				v3 = temp;
			}
		}

		// v4  を挿入ソートっぽく指定位置に挿入
		if (comparator.compare(v2, v4) <= 0) {
			if (comparator.compare(v3, v4) <= 0) {
				// v3 <= v4
			} else {
				// v2 <= v4 < v3;
				temp = v4;
				v4 = v3;
				v3 = temp;
			}
		} else {
			if (comparator.compare(v1, v4) <= 0) {
				// v1 <= v4 < v2;
				temp = v4;
				v4 = v3;
				v3 = v2;
				v2 = temp;
			} else {
				// v4 < v1 <= v2;
				temp = v4;
				v4 = v3;
				v3 = v2;
				v2 = v1;
				v1 = temp;
			}
		}

		// v5  を挿入ソートっぽく指定位置に挿入
		if (comparator.compare(v3, v5) <= 0) {
			// v3 <= v5
			if (comparator.compare(v4, v5) <= 0) {
				// v3 <= v4 <= v5
			} else {
				// v3 <= v5 < v4
				temp = v5;
				v5 = v4;
				v4 = temp;
			}
		} else {
			// v5 < v3
			if (comparator.compare(v2, v5) <= 0) {
				// v2 <= v5 < v3
				temp = v5;
				v5 = v4;
				v4 = v3;
				v3 = temp;
			} else {
				// v5 < v2 <= v3
				if (comparator.compare(v1, v5) <= 0) {
					// v1 <= v5 < v2 <= v3
					temp = v5;
					v5 = v4;
					v4 = v3;
					v3 = v2;
					v2 = temp;
				} else {
					// v5 < v1 <= v2 <= v3
					temp = v5;
					v5 = v4;
					v4 = v3;
					v3 = v2;
					v2 = v1;
					v1 = temp;
				}
			}
		}

		final T pivot1 = v2;	//	ピボット値1
		final T pivot2 = v4;	//	ピボット値2

		if (comparator.compare(pivot1, pivot2) != 0) {
			// pivot1 とpivot2は異なる値
			// dual pivot quick sort ベース
			int idx1A = from;		//	value <= pivot1 の要素へのインデックス(arraysへの配置用)
			int idx2W = 0;			//	pivot1 < value < pivot2の要素へのインデックス(worksへの配置用)
			int idx3W = range - 1;	//	pivot2 <= value へのインデックス(worksへの配置用)

			int idx = from;
			for (; idx < to; idx++) {
				T value = array[idx];
				if (comparator.compare(value, pivot1) <= 0) {
					array[idx1A++] = value;
				} else if (comparator.compare(value, pivot2) < 0) {
					works[idx2W++] = value;
				} else {
					works[idx3W--] = value;
				}
			}
			int idxTo = idx1A;
			for (idx = 0; idx < idx2W; idx++) {
				array[idxTo++] = works[idx];
			}
			for (idx = range - 1; idx > idx3W; idx--) {
				array[idxTo++] = works[idx];
			}

			no6Sort(array, from,          idx1A,         works, depthRemain - 1, comparator);
			no6Sort(array, idx1A,         idx1A + idx2W, works, depthRemain - 1, comparator);
			no6Sort(array, idx1A + idx2W, to,            works, depthRemain - 1, comparator);
		} else {
			// pivot1 とpivot2が同じ値(pivot1のみ使用)
			// 3 way partition ベース
			int idx1A = from;		// value < pivot1 の要素へのインデックス(arraysへの配置用)
			int idx2W = 0;			// value == pivot1 の要素へのインデックス(worksへの配置用)
			int idx3W = range - 1;	// pivot1 < value へのインデックス(worksへの配置用)
			int idx = from;

			for (; idx < to; idx++) {
				T value = array[idx];
				int compareVal = comparator.compare(value, pivot1);
				if (compareVal < 0) {
					array[idx1A++] = value;
				} else if (compareVal > 0) {
					works[idx3W--] = value;
				} else {
					works[idx2W++] = value;
				}
			}
			// works配列から array 配列へ書き戻し
			int idxTo = idx1A;
			for (idx = 0; idx < idx2W; idx++) {
				array[idxTo++] = works[idx];
			}
			for (idx = range - 1; idx > idx3W; idx--) {
				array[idxTo++] = works[idx];
			}

			no6Sort(array, from,          idx1A,         works, depthRemain - 1, comparator);
			no6Sort(array, idx1A + idx2W, to,            works, depthRemain - 1, comparator);
		}
	}

	public static final <T> void no6Sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		// 要素数
		int range = to - from;

		// 作業用配列
		@SuppressWarnings("unchecked")
		T[] works = (T[])new Object[range];

		// 呼び出し深さの許容値 (log2(range))
		// 安全クイックソートやIntroSortでは 1.5や2.0などの係数をかけるが、このソートではDual-pivot quicksortをベースとしているため、
		// 係数をかけなくともlog2(range)で十分な値となる。
		int depthRemain = (int)(Math.log(range) / Math.log(2.0));

		// ソート本体呼び出し
		no6Sort(array, from, to, works, depthRemain, comparator);
	}


	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		no6Sort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "No6 Sort";
	}
}
