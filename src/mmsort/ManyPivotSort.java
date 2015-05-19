/*
 * Many Pivots Sort 事前にピボット値をたくさん確定することで高速化を図った改良版クイックソート
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class ManyPivotSort {
	protected static final int PIVOTS_SIZE = 31;							//	ピボットリストのサイズ。大きすぎなければ何でもよいが、2のベぎ乗 - 1が無駄がなくてよい。
	/**
	 * コムソート
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void combSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;	//	ソート範囲サイズ

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

		int gap = range;
		boolean done = false;
		while (!done || gap > 1) {
			gap = gap * 10 / 13;
			if (gap == 9 || gap == 10)
				gap = 11;
			else if (gap == 0)
				gap = 1;
			done = true;
			for (int i = from; i + gap < to; i++) {
				if (comparator.compare(array[i + gap], array[i]) < 0) {
					final T work = array[i];
					array[i] = array[i + gap];
					array[i + gap] = work;
					done = false;
				}
			}
		}
	}


	/**
	 * クイックソート
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void quickSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
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

		final T pivot = array[from + range / 2];		//	ピボット値（ソート対象の中央位置）

		int curFrom = from;			//	現在処理中位置の小さい方の位置
		int curTo = to - 1;			//	現在処理中位置の大きい方の位置

		do {
			while (comparator.compare(array[curFrom], pivot) < 0) {
				curFrom++;
			}
			while (comparator.compare(array[curTo], pivot) > 0) {
				curTo--;
			}
			if (curFrom <= curTo) {
				final T work = array[curFrom];
				array[curFrom] = array[curTo];
				array[curTo] = work;
				curFrom++;
				curTo--;
			}
		} while (curFrom <= curTo);

		if (from < curTo + 1) {
			quickSort(array, from, curTo + 1, comparator);
		}

		if (curFrom < to - 1) {
			quickSort(array, curFrom, to, comparator);
		}
	}


	/**
	 * クイックソート（３つのメディアン）
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void quickSortMedian3(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
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
		T pivot;							//	ピボット値
//		pivot = array[from + range / 2];

		T pivot1 = array[from];				//	ピボット候補１
		T pivot2 = array[from + range / 2];	//	ピボット候補２
		T pivot3 = array[to - 1];			//	ピボット候補３

		//	３つのピボット候補から中央値を取得し、ピボット値とする
		if (comparator.compare(pivot1, pivot2) < 0) {
			if (comparator.compare(pivot2, pivot3) < 0) {
				pivot = pivot2;		//	pivot1 < pivot2 < pivot3
			} else if (comparator.compare(pivot1, pivot3) < 0) {
				pivot = pivot3;		//	pivot1 < pivot3 < pivot2
			} else {
				pivot = pivot1;		//	pivot3 < pivot1 < pivot2
			}
		} else {
			if (comparator.compare(pivot1, pivot3) < 0) {
				pivot = pivot1;		//	pivot2 < pivot1 < pivot3
			} else if (comparator.compare(pivot2, pivot3) < 0) {
				pivot = pivot3;		//	pivot2 < pivot3 < pivot1
			} else {
				pivot = pivot2;		//	pivot3 < pivot2 < pivot1
			}
		}

		int curFrom = from;			//	現在処理中位置の小さい方の位置
		int curTo = to - 1;			//	現在処理中位置の大きい方の位置

		do {
			while (comparator.compare(array[curFrom], pivot) < 0) {
				curFrom++;
			}
			while (comparator.compare(array[curTo], pivot) > 0) {
				curTo--;
			}
			if (curFrom <= curTo) {
				final T work = array[curFrom];
				array[curFrom] = array[curTo];
				array[curTo] = work;
				curFrom++;
				curTo--;
			}
		} while (curFrom <= curTo);

		if (from < curTo + 1) {
			quickSort(array, from, curTo + 1, comparator);
		}

		if (curFrom < to - 1) {
			quickSort(array, curFrom, to, comparator);
		}
	}


	/**
	 * メニー・ピボット・ソート
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
	public static final <T> void mpSort(final T[] array, final int from, final int to, final T[] pivots, final int fromPivots, final int toPivots, final Comparator<? super T> comparator)
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

		final int pivotIdx = (fromPivots + toPivots) / 2;		//	pivots配列の中で、今回使うべき要素の添え字
		final T pivot = pivots[pivotIdx];						//	ピボット値

		int curFrom = from;			//	現在処理中位置の小さい方の位置
		int curTo = to - 1;		//	現在処理中位置の大きい方の位置

		do {
			//	このあたりは割と普通のクイックソートのまま。
			int comp1;
			while ((comp1 = comparator.compare(array[curFrom], pivot)) < 0) {
				curFrom++;
			}
			int comp2;
			while ((comp2 = comparator.compare(array[curTo], pivot)) > 0) {
				curTo--;
			}
			if (curFrom <= curTo) {
				if (comp1 != comp2) {	//	実質的には array[curFrom]とarray[curTo]の位置の両方の値がピボット値と同じでない場合という意味
					final T work = array[curFrom];
					array[curFrom] = array[curTo];
					array[curTo] = work;
				}
				curFrom++;
				curTo--;
			} else {
				break;
			}
		} while (true);

		if (from < curTo + 1) {
			if (fromPivots >= pivotIdx - 3)	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
				mpSort(array, from, curTo + 1, comparator);
			else
				mpSort(array, from, curTo + 1, pivots, fromPivots, pivotIdx, comparator);
		}

		if (curFrom < to - 1) {
			if (pivotIdx + 1 >= toPivots - 3)	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
				mpSort(array, curFrom, to, comparator);
			else
				mpSort(array, curFrom, to, pivots, pivotIdx + 1, toPivots, comparator);
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
			quickSortMedian3(array, from, to, comparator);
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
		combSort(pivots, 0, pivots.length, comparator);
		//	ソート対象本体のソート
		mpSort(array, from, to, pivots, 0, pivots.length, comparator);
	}

}
