/*
 * Many Pivot Sort (3 Way partition edition)
 *
 * メニー・ピボット・ソート(3 Way partition版)
 * 事前にピボット値をたくさん確定することで高速化を図った改良版クイックソート
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class ManyPivotSort3W implements ISortAlgorithm {
	protected static final int PIVOTS_SIZE = 31;							// pivot list size / ピボットリストのサイズ。大きすぎなければ何でもよいが、2のベぎ乗 - 1が無駄がなくてよい。
	protected static final int SWITCH_SIZE = 50;							// size of switching to other algorithms / 他のアルゴリズムに切り替えるサイズ
	/**
	 * Many pivot sort (3 Way partition)
	 *
	 * メニー・ピボット・ソート (3 Way partition)
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
	public static final <T> void mpSort(final T[] array, final int from, final int to, final T[] pivots, final int fromPivots, final int toPivots, final Comparator<? super T> comparator)
	{
		final int pivotIdx = fromPivots + (toPivots - fromPivots) / 2;		//	using index from pivots (center position) / pivots配列の中で、今回使うべき要素の添え字
		final T pivot = pivots[pivotIdx];									//	pivot value / ピボット値

		int curFrom = from;		//	min index / 現在処理中位置の小さい方の位置
		int curTo = to - 1;		//	max index / 現在処理中位置の大きい方の位置

		// A little faster
		// 少し高速化
		//
		// +----------------+-----------------------------------------+----------------+
		// | value == pivot |                    ?                    | value == pivot |
		// +----------------+-----------------------------------------+----------------+
		// ^                ^                                         ^                 ^
		// |                |                                         |                 |
		// from          curFrom                                   curTo                to
		while (curFrom < curTo && comparator.compare(array[curFrom], pivot) == 0)
			curFrom++;
		while (curFrom < curTo && comparator.compare(pivot, array[curTo]) == 0)
			curTo--;
		if (curFrom >= curTo)
			return;

		int eqFrom = curFrom;
		int eqTo = curTo;
		while (true) {
			// Separation
			// 分割
			//
			// +----------------+---------------+---------+---------------+----------------+
			// | value == pivot | value < pivot |    ?    | value > pivot | value == pivot |
			// +----------------+---------------+---------+---------------+----------------+
			// ^                ^               ^         ^               ^                 ^
			// |                |               |         |               |                 |
			// from           eqFrom         curFrom   curTo            eqTo                to

			int comp1;		// array[curFrom] と pivot の比較値
			while ((comp1 = comparator.compare(array[curFrom], pivot)) < 0)
				curFrom++;
			int comp2;		// array[curTo] と pivot の比較値
			while ((comp2 = comparator.compare(pivot, array[curTo])) < 0)
				curTo--;
			if (curFrom > curTo)
				break;

			T work = array[curFrom];
			array[curFrom] = array[curTo];
			array[curTo] = work;

			// Elements that were in the array[curFrom] at the time of comparison is present in the array[curTo] by swap.
			// array [curTo] is replaced with the array [eqTo], to collect the value equal to the pivot value in the rear.
			// 比較時に array[curFrom] にあった要素はスワップによって array[curTo] に存在している
			// array[curTo] と array[eqTo] を入れ替えて、ピボット値と等しい値を後方にいったん集める
			if (comp1 == 0) {
				work = array[curTo];
				array[curTo] = array[eqTo];
				array[eqTo] = work;
				eqTo--;
			}
			// Elements that were in the array[curTo] at the time of comparison is present in the array[curFrom] by swap.
			// array [curFrom] is replaced with the array [eqFrom], to collect the value equal to the pivot value in the rear.
			// 比較時に array[curTo] にあった要素はスワップによって array[curFrom] に存在している
			// array[curFrom] と array[eqFrom] を入れ替えて、ピボット値と等しい値を後方にいったん集める
			if (comp2 == 0) {
				work = array[curFrom];
				array[curFrom] = array[eqFrom];
				array[eqFrom] = work;
				eqFrom++;
			}

			curFrom++;
			curTo--;
		}

		// Bring pivot value and a value equal to the center
		// ピボット値と等しい値を中央に持ってくる
		//
		// Before
		// +----------------+-----------------+-----------------------+----------------+
		// | value == pivot | value < pivot   |         value > pivot | value == pivot |
		// +----------------+-----------------+-----------------------+----------------+
		// ^        |       ^                 ^                       ^        |        ^
		// |        |       |                 |                       |        |        |
		// from     |     eqFrom      curFrom & curTo               eqTo       |        to
		//          |                                                          |
		//          +---------------+                 +------------------------+
		//                          |                 |
		// After                    v                 v
		// +-----------------+----------------+----------------+-----------------------+
		// | value < pivot   | value == pivot | value == pivot |         value > pivot |
		// +-----------------+----------------+----------------+-----------------------+
		// ^                 ^                                ^                         ^
		// |                 |                                |                         |
		// from            eqFrom                           eqTo                        to
		if (curFrom != eqFrom) {
			while (eqFrom > from) {
				curFrom--;
				eqFrom--;
				final T work = array[curFrom];
				array[curFrom] = array[eqFrom];
				array[eqFrom] = work;
			}
		}
		if (curTo != eqTo) {
			while (eqTo + 1 < to) {
				eqTo++;
				curTo++;
				final T work = array[curTo];
				array[curTo] = array[eqTo];
				array[eqTo] = work;
			}
		}

/*
		for (int i = from; i < curFrom; i++) {
			if (comparator.compare(pivot, array[i]) <= 0)
				throw new RuntimeException("aaa");
		}
		for (int i = curFrom; i < curTo + 1; i++) {
			if (comparator.compare(pivot, array[i]) != 0)
				throw new RuntimeException("bbb");
		}
		for (int i = curTo + 1; i < to; i++) {
			if (comparator.compare(pivot, array[i]) >= 0)
				throw new RuntimeException("ccc");
		}
*/
		if (from < curFrom - 1) {
			if (fromPivots >= pivotIdx - 3)	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
				mpSort(array, from, curFrom, comparator);
			else
				mpSort(array, from, curFrom, pivots, fromPivots, pivotIdx, comparator);
		}

		if (curTo < to - 1) {
			if (pivotIdx + 1 >= toPivots - 3)	//	pivotsの残りが３つを切ったらpivotsを作り直す。（最後まで使い切らないのは、最後の１個は範囲内の中間値に近いとは言えないので）
				mpSort(array, curTo + 1, to, comparator);
			else
				mpSort(array, curTo + 1, to, pivots, pivotIdx + 1, toPivots, comparator);
		}
	}

	/**
	 * Many pivot sort (3 Way partition)
	 *
	 * メニー・ピボット・ソート (3 Way partition)
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void mpSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	sort range / ソート範囲サイズ

		if (range < SWITCH_SIZE) {
			// Please replace with your favorite sorting algorithm...

			// BinInsertionSort.binInsertionSort(array, from, to, comparator);
			QuickSortM3.quickSortMedian3(array, from, to, comparator);
			return;
		}

		int pivotsSize = PIVOTS_SIZE;
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

		// Selection of the pivot values (Binary insertion sort ish processing).
		// Remove duplicate values.
		// 複数のピボット値を選出(バイナリ挿入ソートっぽい処理)
		// 重複値は取り除く
		T item = array[(int)(from + range / 2 / pivots.length)];
		pivots[0] = item;
		int pivotCount = 1;
		for (int i = 1; i < pivots.length; i++) {
			// Get element at appropriate intervals from array
			// 配列より適当な間隔で要素を取得
			item = array[(int)(from + (long)range * i / pivots.length + range / 2 / pivots.length)];
			int fromIdx = 0;
			int toIdx = pivotCount;
			// Binary search, insert if non duplicate
			// 二分探索して重複がなければ挿入
			while (true) {
				int curIdx = fromIdx + (toIdx - fromIdx) / 2;
				if (fromIdx >= toIdx) {
					// Insertion position determination
					// 挿入位置確定
					for (int j = pivotCount; j > curIdx; j--)
						pivots[j] = pivots[j - 1];
					pivots[curIdx] = item;
					pivotCount++;
					break;
				}
				int comp = comparator.compare(item, pivots[curIdx]);
				if (comp == 0) {
					// Equal and already Selection pivot value, and exits the process, and proceeds to the next element
					// すでに選出したピボット値と等しければ処理を抜けて次の要素へ進む
					break;
				}
				if (comp < 0)
					toIdx = curIdx;
				else
					fromIdx = curIdx + 1;
			}
		}

		//	ソート対象本体のソート
		mpSort(array, from, to, pivots, 0, pivotCount, comparator);
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
		return "Many Pivot Sort (3 Way)";
	}
}
