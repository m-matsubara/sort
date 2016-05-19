/*
 * Staged Sort
 *
 * 二段燃焼ソート
 * Many pivot sortの改良版
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class StagedSort implements ISortAlgorithm {
	protected static final int PIVOTS_SIZE = 127;							//	ピボットリストのサイズ。大きすぎなければ何でもよいが、2のベぎ乗 - 1が無駄がなくてよい。
	protected static final int SWITCH_SIZE = 3000;							//	Quick Sort (Median of 3) に切り替えるサイズ
	protected static final int AAA = 10;

	/**
	 * 前方優先バイナリサーチ
	 * 先頭から範囲を２倍ずつ拡張しながら、ある程度位置を絞り込む。その後絞り込んだ範囲内でバイナリサーチ
	 * @param key 検索値
	 * @param array 検索対象
	 * @param from 検索対象の添え字の最小値
	 * @param to 検索対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 * @return keyの値と等しいか、より大きい最初の要素の添え字
	 */
	public static final <T> int searchFowardingBinSearch(final T key, final T[] array, int from, int to, Comparator<? super T> comparator)
	{
		//	先頭から1個目, 2個目, 4個目, 8個目…として、対象位置を大まかに絞り込む
		int skipSize = 1;
		int idx = from;
		while (idx < to) {
			if (comparator.compare(array[idx], key) < 0) {
				from = idx + 1;
				idx += skipSize;
				skipSize *= 2;
			} else  {
				to = idx;
				break;
			}
		}

		//	大まかに絞り込んだ範囲内で２分検索する
		int fromIdx = from;
		int toIdx = to;
		int curIdx = fromIdx + (toIdx - fromIdx) / 2;
		while (fromIdx < toIdx) {
			if (comparator.compare(key, array[curIdx]) <= 0) {	// virtual code : (array[curIdx] < key) == false
				toIdx = curIdx;
				curIdx = fromIdx + (toIdx - fromIdx) / 2;
				continue;
			} else {
				fromIdx = curIdx + 1;
				curIdx = fromIdx + (toIdx - fromIdx) / 2;
				continue;
			}
		}
		return curIdx;
	}


	/**
	 * Staged Sort
	 *
	 * 二段燃焼ソート
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
	protected static final <T> void stagedSort(final T[] array, final int from, final int to, final int fromPivots, final int toPivots, final Comparator<? super T> comparator)
	{
		final int pivotIdx = fromPivots + (toPivots - fromPivots) / 2;		//	using index from pivots (center position) / pivots配列の中で、今回使うべき要素の添え字
		final T pivot = array[pivotIdx];									//	pivot value / ピボット値

		int curFrom = from;		//	min index / 現在処理中位置の小さい方の位置
		int curTo = to - 1;		//	max index / 現在処理中位置の大きい方の位置

		while (true) {
			while (comparator.compare(array[curFrom], pivot) < 0)
				curFrom++;
			while (comparator.compare(pivot, array[curTo]) < 0)
				curTo--;
			if (curTo <= curFrom)
				break;
			final T work = array[curFrom];
			array[curFrom] = array[curTo];
			array[curTo] = work;
			curFrom++;
			curTo--;
		}

		if (from < curTo) {
			if (fromPivots >= pivotIdx - 3)
				QuickSortM5.quickSortMedian5(array, from, to, comparator);
			else
				stagedSort(array, from, curTo + 1, fromPivots, pivotIdx, comparator);
		}

		if (curFrom < to - 1) {
			if (pivotIdx + 1 >= toPivots - 3)
				QuickSortM5.quickSortMedian5(array, from, to, comparator);
			else
				stagedSort(array, curFrom, to, pivotIdx + 1, toPivots, comparator);
		}
	}

	/**
	 * Staged Sort
	 *
	 * 二段燃焼ソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	@SuppressWarnings("unchecked")
	public static final <T> void stagedSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator, T[] pivots)
	{
		final int range = to - from;		//	sort range / ソート範囲サイズ

		//	ソート対象配列サイズが一定以下のときは特別扱い
		if (range < SWITCH_SIZE) {
			// しきい値以下ではクイックソート（5つのメディアン）に切り替える。
			QuickSortM5.quickSortMedian5(array, from, to, comparator);
			return;
		}

		int pivotsSize = range / AAA;

		if (pivots == null)
			pivots = (T[])new Object[pivotsSize];		//	pivot candidates / ピボット候補の配列

		// Selection of the pivot values (Binary insertion sort ish processing).
		// ピボット（複数）の選出

		for (int i = 1; i < pivots.length; i++) {
			T work = array[from + i * AAA];
			array[from + i * AAA] = array[from + i];
			array[from + i] = work;
		}

		//QuickSortM5.quickSortMedian5(array, from, from + pivotsSize, comparator);
		stagedSort(array, from, from + pivotsSize, comparator, pivots);

		stagedSort(array, from + pivotsSize, to, 0, pivotsSize, comparator);

		for (int i = 0; i < pivotsSize; i++)
			pivots[i] = array[from + i];

		int idx = from;
		int idx1 = 0;
		int idx2 = from + pivotsSize;
		while (idx1 < pivotsSize && idx2 < to)  {
			final T value1 = pivots[idx1];

			final int toIdx = searchFowardingBinSearch(value1, array, idx2, to, comparator);
			if (toIdx != idx2) {
				// getting multi values from back block, stored to sorted area (Because back block is large)
				// 後方のブロックから値をいくつか取り出して、ソート済み領域に格納（後ろのブロックが大きいため、後ろのブロックから連続して値が採用される可能性が高い）
				System.arraycopy(array, idx2, array, idx, toIdx - idx2);
				idx += (toIdx - idx2);
				idx2 = toIdx;
			}
			if (toIdx < to) {
				// getting single value from forward block, stored to sorted area
				// 前方のブロックから値を１つ取り出して、ソート済み領域に格納
				array[idx] = value1;
				idx1++;
				idx++;
			}
		}

		// remaining temporary data to array
		// 残った一時領域のデータをソート対象へ詰める
		while (idx1 < pivotsSize)  {
			array[idx] = pivots[idx1];
			idx++;
			idx1++;
		}

	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		stagedSort(array, from, to, comparator, null);
	}

	@Override
	public boolean isStable()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "Staged Sort";
	}
}
