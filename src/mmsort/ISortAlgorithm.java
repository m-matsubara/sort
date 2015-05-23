/*
 * ISortable interface
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

/**
 * Sort algorithm interface (for mmsort.SortTest class)
 *
 * ソートアルゴリズムを実装するインターフェース(mmsort.SortTestクラス用)
 *
 * @author matsubara
 *
 */
public interface ISortAlgorithm {
	/**
	 * Sort method
	 *
	 * ソートメソッド
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator);

	/**
	 * Sort algorithm is stable ?
	 *
	 * ソートアルゴリズムは安定アルゴリズムか？
	 *
	 * @return
	 */
	public boolean isStable();

	/**
	 * Sort algorithm name
	 *
	 * ソートアルゴリズムの名称
	 *
	 * @return
	 */
	public String getName();
}
