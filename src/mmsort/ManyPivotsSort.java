/*
 * Many Pivots Sort 事前にピボット値をたくさん確定することで高速化を図った改良版クイックソート
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class ManyPivotsSort {
	protected static final int PIVOTS_SIZE = 31;							//	ピボットリストのサイズ。大きすぎなければ何でもよいが、2のベぎ乗 - 1が無駄がなくてよい。
	/**
	 * コムソート
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void combSort(final T[] array, final int min, final int max, final Comparator<? super T> comparator)
	{
		final int range = max - min;
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

		int gap = range;
		boolean done = false;
		while (!done || gap > 1) {
			gap = gap * 10 / 13;
			if (gap == 9 || gap == 10)
				gap = 11;
			else if (gap == 0)
				gap = 1;
			done = true;
			for (int i = min; i + gap < max; i++) {
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
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void quickSort(final T[] array, final int min, final int max, final Comparator<? super T> comparator)
	{
		final int range = max - min;
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

		final T pivot = array[min + range / 2];

		int curMin = min;
		int curMax = max - 1;

		do {
			while (comparator.compare(array[curMin], pivot) < 0) {
				curMin++;
			}
			while (comparator.compare(array[curMax], pivot) > 0) {
				curMax--;
			}
			if (curMin <= curMax) {
				final T work = array[curMin];
				array[curMin] = array[curMax];
				array[curMax] = work;
				curMin++;
				curMax--;
			}
		} while (curMin <= curMax);

		if (min < curMax + 1) {
			quickSort(array, min, curMax + 1, comparator);
		}

		if (curMin < max - 1) {
			quickSort(array, curMin, max, comparator);
		}
	}


	/**
	 * クイックソート（３つのメディアン）
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void quickSortMedian3(final T[] array, final int min, final int max, final Comparator<? super T> comparator)
	{
		final int range = max - min;
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
		T pivot;
//		pivot = array[min + range / 2];

		T pivot1 = array[min];
		T pivot2 = array[min + range / 2];
		T pivot3 = array[max - 1];

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

		int curMin = min;
		int curMax = max - 1;

		do {
			while (comparator.compare(array[curMin], pivot) < 0) {
				curMin++;
			}
			while (comparator.compare(array[curMax], pivot) > 0) {
				curMax--;
			}
			if (curMin <= curMax) {
				final T work = array[curMin];
				array[curMin] = array[curMax];
				array[curMax] = work;
				curMin++;
				curMax--;
			}
		} while (curMin <= curMax);

		if (min < curMax + 1) {
			quickSort(array, min, curMax + 1, comparator);
		}

		if (curMin < max - 1) {
			quickSort(array, curMin, max, comparator);
		}
	}


	/**
	 * メニー・ピボット・ソート
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param pivots ピボットの配列
	 * @param minPivots 使用対象となる pivots 配列の添え字の最小値
	 * @param maxPivots 使用対象となる pivots 配列の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void mpSort(final T[] array, final int min, final int max, final T[] pivots, final int minPivots, final int maxPivots, final Comparator<? super T> comparator)
	{
		final int range = max - min;
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
		final T pivot = pivots[pivotIdx];						//

		int curMin = min;
		int curMax = max - 1;

		do {
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
				curMin++;
				curMax--;
			} else {
				break;
			}
		} while (true);

		if (min < curMax + 1) {
			if (minPivots >= pivotIdx - 3)	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
				mpSort(array, min, curMax + 1, comparator);
			else
				mpSort(array, min, curMax + 1, pivots, minPivots, pivotIdx, comparator);
		}

		if (curMin < max - 1) {
			if (pivotIdx + 1 >= maxPivots - 3)	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
				mpSort(array, curMin, max, comparator);
			else
				mpSort(array, curMin, max, pivots, pivotIdx + 1, maxPivots, comparator);
		}
	}

	/**
	 * メニー・ピボット・ソート
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void mpSort(final T[] array, final int min, final int max, final Comparator<? super T> comparator)
	{
		final int range = max - min;
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

		if (range < 3000) {
			quickSortMedian3(array, min, max, comparator);
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
		final T[] pivots = (T[])new Object[pivotsSize];

		//	ピボット（複数）の選出
		for (int i = 0; i < pivots.length; i++) {
			pivots[i] = array[(int)(min + (long)range * i / pivots.length + range / 2 / pivots.length)];
		}
		//	ピボット値のみをソート
		combSort(pivots, 0, pivots.length, comparator);
		//	ソート対象本体のソート
		//mp3wSort(array, min, max, pivots, 0, pivots.length, comparator);
		mpSort(array, min, max, pivots, 0, pivots.length, comparator);
	}

}
