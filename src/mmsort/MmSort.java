/*
 * mmSort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2016 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class MmSort implements ISortAlgorithm {
	// Insersion Sortなどに切り替える要素数
	private static final int ALGORITHM_THRESHOLD = 20;

	/**
	 * mmSort
	 *
	 * 基本的には「 ５つのメディアン」だが、５つの中央値を選択したときに、小さい値２つを配列の先頭側に、大きい２つを配列の最後側に
	 * 退避し、パーティション操作から除外することで高速化を図ったアルゴリズム
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void mmSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ

		//	ソート対象配列サイズが一定数以下のときは特別扱い
		if (range < ALGORITHM_THRESHOLD) {
			InsertionSort.insertionSort(array, from, to, comparator);
			//BinInsertionSort.binInsertionSort(array, from, to, comparator);
			return;
		}

		final int p1 = from;
		final int p5 = to - 1;
		final int p3 = p1 + ((p5 - p1) >>> 1);
		final int p2 = p1 + ((p3 - p1) >>> 1);
		final int p4 = p3 + ((p5 - p3) >>> 1);

		//sort5 メソッドの呼び出しを手動でインライン展開
		//sort5(array, p1, p2, p3, p4, p5, comparator);
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
/* 4番目 と 5番目の位置関係は重要ではない(v3が中央に来さえすればよい)
				if (comparator.compare(array[p4], v5) <= 0) {
					// array[p3] <= array[4] <= v5
				} else {
					// array[p3] <= v5 < array[p4]
					array[p5] = array[p4];
					array[p4] = v5;
				}
*/
			} else {
				// v5 < array[p3]
				if (comparator.compare(array[p2], v5) <= 0) {
					// array[p2] <= v5 < array[p3]
					array[p5] = array[p4];
					array[p4] = array[p3];
					array[p3] = v5;
				} else {
					// v5 < array[p2] <= array[p3]
/* 1番目 と 2番目の位置関係は重要ではない(v3が中央に来さえすればよい)
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
*/
					array[p5] = array[p4];
					array[p4] = array[p3];
					array[p3] = array[p2];
					array[p2] = v5;
				}
			}
		}


		/*
		 * この時点で、
		 *   array[from](array[p1])には、5つの候補のうち、中央値以下の値が入っている。
		 *   array[p3]には、中央値が入っている。
		 *   array[to - 1](array[p5])には、5つの候補のうち、中央値以上の値が入っている。
		 * そこで、
		 *   array[from + 1]とarray[p2]の値を入れ替え
		 *   array[from + 2]とarray[p3]の値を入れ替え
		 *   array[to - 2]とarray[p4]の値を入れ替え
		 * とすることで、パーティション操作のの範囲を狭くすることができる。
		 **/

		final T pivot = array[p3];	//	ピボット値

		{
			final T work = array[from + 1];
			array[from + 1] = array[p2];
			array[p2] = work;
		}

		//work = array[from + 2];
		//array[from + 2] = array[p3];
		//array[p3] = work;
		array[p3] = array[from + 2];
		array[from + 2] = pivot;

		{
			final T work = array[to - 2];
			array[to - 2] = array[p4];
			array[p4] = work;
		}

		//	パーティション操作
		int curFrom = from + 3;			//	min index / 現在処理中位置の小さい方の位置
		int curTo = to - 1 - 2;			//	max index / 現在処理中位置の大きい方の位置
		while (true) {
			while (comparator.compare(array[curFrom++], pivot) < 0);
			while (comparator.compare(pivot, array[curTo--]) < 0);
			curFrom--;
			curTo++;
			if (curFrom >= curTo)
				break;
			final T work = array[curFrom];
			array[curFrom++] = array[curTo];
			array[curTo--] = work;
		};

		//	ピボット値をパーティションの間に入れ替える（再起の処理の対象外にできる）
		array[from + 2] = array[curTo];
		array[curTo] = pivot;

		//	小さいパーティション・大きいパーティションそれぞれで再起
		mmSort(array, from, curTo, comparator);
		mmSort(array, curFrom, to, comparator);
	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		mmSort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "mmSort";
	}
}
