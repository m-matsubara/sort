/*
 * Many Pivot Sort
 *
 * メニー・ピボット・ソート
 * 事前にピボット値をたくさん確定することで高速化を図った改良版クイックソート
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class ManyPivotSort implements ISortAlgorithm {
	private static final int PIVOTS_SIZE = 127;							//	ピボットリストのサイズ。大きすぎなければ何でもよいが、2のベぎ乗 - 1が無駄がなくてよい。
	private static final int ALGORITHM_THRESHOLD = 10000;				//	クイックソートに切り替えるサイズ
	/**
	 * Many pivot sort
	 *
	 * メニー・ピボット・ソート
	 *
	 * internal method (Added argument the pivot array)
	 * 内部的に呼び出される。ピボットの配列（ピボット候補）を引数にもつ
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param pivots array of pivot / ピボットの配列
	 * @param fromPivots from index of pivots / 使用対象となる pivots 配列の添え字の最小値
	 * @param toPivots to index of pivots (last element of exclusive) / 使用対象となる pivots 配列の添え字の最大値 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	protected static final <T> void mpSort(final T[] array, final int from, final int to, final T[] pivots, final int fromPivots, final int toPivots, final Comparator<? super T> comparator)
	{
		final int pivotIdx = fromPivots + ((toPivots - fromPivots) >> 1);	//	using index from pivots (center position) / pivots配列の中で、今回使うべき要素の添え字
		final T pivot = pivots[pivotIdx];									//	pivot value / ピボット値

		int curFrom = from;		//	min index / 現在処理中位置の小さい方の位置
		int curTo = to - 1;		//	max index / 現在処理中位置の大きい方の位置

		while (true) {
			while (comparator.compare(array[curFrom++], pivot) < 0);
			while (comparator.compare(pivot, array[curTo--]) < 0);
			curFrom--;
			curTo++;
			if (curFrom > curTo)
				break;
			final T work = array[curFrom];
			array[curFrom++] = array[curTo];
			array[curTo--] = work;
		}

		if (toPivots - fromPivots <= 3) {	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
			mpSort(array, from, curTo + 1, comparator);
			mpSort(array, curFrom, to, comparator);
		} else {
			mpSort(array, from, curTo + 1, pivots, fromPivots, pivotIdx, comparator);
			mpSort(array, curFrom, to, pivots, pivotIdx + 1, toPivots, comparator);
		}
	}

	/**
	 * Many pivot sort
	 *
	 * メニー・ピボット・ソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void mpSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	sort range / ソート範囲サイズ

		//	ソート対象配列サイズが一定以下のときは特別扱い
		if (range < ALGORITHM_THRESHOLD) {
			// しきい値以下ではクイックソート（５つのメディアン）に切り替える。
			QuickSortM5.quickSortMedian5(array, from, to, comparator);
			return;
		}

		int pivotsSize = PIVOTS_SIZE;	//	ソート要素数によって変えてみたり…。

		@SuppressWarnings("unchecked")
		final T[] pivots = (T[])new Object[pivotsSize];		//	pivot candidates / ピボット候補の配列

		// Selection of the pivot values (Binary insertion sort ish processing).
		// ピボット（複数）の選出
		for (int i = 0; i < pivots.length; i++) {
			pivots[i] = array[(int)(from + (long)range * i / pivots.length + range / 2 / pivots.length)];
		}
		// sort of pivot candidates / ピボット値のみをソート
		BinInsertionSort.binInsertionSort(pivots, 0, pivots.length, comparator);
		//QuickSortM3.quickSortMedian3(pivots, 0, pivots.length, comparator);
		// sort of array / ソート対象本体のソート
		mpSort(array, from, to, pivots, 0, pivots.length, comparator);
	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		mpSort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "Many Pivot Sort";
	}
}
