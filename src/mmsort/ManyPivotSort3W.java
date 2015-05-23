/*
 * Many Pivot Sort
 *
 * メニー・ピボット・ソート
 * 事前にピボット値をたくさん確定することで高速化を図った改良版クイックソート
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class ManyPivotSort3W implements ISortAlgorithm {
	protected static final int PIVOTS_SIZE = 31;							//	ピボットリストのサイズ。大きすぎなければ何でもよいが、2のベぎ乗 - 1が無駄がなくてよい。
	/**
	 * メニー・ピボット・ソート(3 Way Edition)
	 *
	 * 内部的に呼び出される。ピボットの配列（ピボット候補）を引数にもつ
	 *
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param pivots ピボットの配列
	 * @param fromPivots 使用対象となる pivots 配列の添え字の最小値
	 * @param toPivots 使用対象となる pivots 配列の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void mpSort(final T[] array, final int min, final int max, final T[] pivots, final int minPivots, final int maxPivots, final Comparator<? super T> comparator)
	{
		final int range = max - min;		//	ソート範囲サイズ

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				final T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				final T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			if (comparator.compare(array[min + 1], array[min + 2]) > 0) {
				final T work = array[min + 1];
				array[min + 1] = array[min + 2];
				array[min + 2] = work;
				if (comparator.compare(array[min], array[min + 1]) > 0) {
					final T work2 = array[min];
					array[min] = array[min + 1];
					array[min + 1] = work2;
				}
			}
			return;
		}
/*
		if (range < 50) {
			combSort(array, min, max, comparator);
			return;
		}
*/

		final int pivotIdx = (minPivots + maxPivots) / 2;		//	pivots配列の中で、今回使うべき要素の添え字
		final T pivot = pivots[pivotIdx];						//	ピボット値

		int curMin = min;			//	現在処理中位置の小さい方の位置
		int curMax = max - 1;		//	現在処理中位置の大きい方の位置
		int eqMin = curMin;
		int eqMax = curMax;

		do {
			//	このあたりは割と普通のクイックソートのまま。
			int comp1;
			while ((comp1 = comparator.compare(array[curMin], pivot)) < 0) {
				curMin++;
			}
			int comp2;
			while ((comp2 = comparator.compare(array[curMax], pivot)) > 0) {
				curMax--;
			}
			if (curMin <= curMax) {
				if (comp1 != comp2) {	//	実質的には array[curMin]とarray[curMax]の位置の両方の値がピボット値と同じでない場合という意味
					final T work = array[curMin];
					array[curMin] = array[curMax];
					array[curMax] = work;
				}
				if (comp1 == 0) {
					final T work = array[curMax];
					array[curMax] = array[eqMax];
					array[eqMax] = work;
					eqMax--;
				}
				if (comp2 == 0) {
					final T work = array[curMin];
					array[curMin] = array[eqMin];
					array[eqMin] = work;
					eqMin++;
				}
				curMin++;
				curMax--;
			} else {
				break;
			}
		} while (true);
		if (curMin != eqMin) {
			while (eqMin > min) {
				curMin--;
				eqMin--;
				final T work = array[curMin];
				array[curMin] = array[eqMin];
				array[eqMin] = work;
			}
		}
		if (curMax != eqMax) {
			while (eqMax + 1 < max) {
				eqMax++;
				curMax++;
				final T work = array[curMax];
				array[curMax] = array[eqMax];
				array[eqMax] = work;
			}
		}

		if (min < curMin) {
			if (minPivots >= pivotIdx - 3)	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
				mpSort(array, min, curMin, comparator);
			else
				mpSort(array, min, curMin, pivots, minPivots, pivotIdx, comparator);
		}

		if (curMax < max - 1) {
			if (pivotIdx + 1 >= maxPivots - 3)	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
				mpSort(array, curMax + 1, max, comparator);
			else
				mpSort(array, curMax + 1, max, pivots, pivotIdx + 1, maxPivots, comparator);
		}
	}

	/**
	 * メニー・ピボット・ソート
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void mpSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ

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
/*
		if (range < 50) {
			combSort(array, from, to, comparator);
			return;
		}
*/

		if (range < 3000) {
			QuickSortM3.quickSortMedian3(array, from, to, comparator);
			return;
		}

		int pivotsSize = 127;
/*
		if (range >= 1000000)
			pivotsSize = 2048 - 1;
		else if (range >= 500000)
			pivotsSize = 1024 - 1;
		else if (range >= 250000)
			pivotsSize = 512 - 1;
		else if (range >= 120000)
			pivotsSize = 256 - 1;
		else if (range >= 60000)
			pivotsSize = 128 - 1;
		else if (range >= 30000)
			pivotsSize = 64 - 1;
		else
			pivotsSize = 32 - 1;
*/
		@SuppressWarnings("unchecked")
		final T[] pivots = (T[])new Object[pivotsSize];		//	ピボット候補の配列

		//	ピボット（複数）の選出
		for (int i = 0; i < pivots.length; i++) {
			pivots[i] = array[(int)(from + (long)range * i / pivots.length + range / 2 / pivots.length)];
		}
		//	ピボット値のみをソート
		CombSort.combSort(pivots, 0, pivots.length, comparator);
		//	ソート対象本体のソート
		mpSort(array, from, to, pivots, 0, pivots.length, comparator);
	}

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		mpSort(array, from, to, comparator);
	}

	public boolean isStable()
	{
		return false;
	}

	public String getName()
	{
		return "Many Pivot Sort";
	}
}
