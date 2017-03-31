/*
 * MasSort (Version 2)
 *
 * 2016/06
 * 以前のバージョンのMasSortは配列を32個ほどの区画にわけマージしていたが、
 * コードが複雑になりすぎて速度が思ったほど上がらなかったため、３つの区画に分け、
 * ３つの区画の大小関係を STATE_XXX のような整数型変数１つで管理する方式に
 * 変更した。
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2016 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class MasSort implements ISortAlgorithm {
	private static final byte STATE_123 = 0;
	private static final byte STATE_132 = 1;
	private static final byte STATE_213 = 2;
	private static final byte STATE_231 = 3;
	private static final byte STATE_312 = 4;
	private static final byte STATE_321 = 5;

	private static final byte STATE_12 = 6;
	private static final byte STATE_13 = 7;
	private static final byte STATE_21 = 8;
	private static final byte STATE_23 = 9;
	private static final byte STATE_31 = 10;
	private static final byte STATE_32 = 11;

	private static final byte STATE_1 = 12;
	private static final byte STATE_2 = 13;
	private static final byte STATE_3 = 14;

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
	 * マージ処理
	 * @param array マージ先
	 * @param pos1 array配列の第１レーンの開始位置
	 * @param pos2 array配列の第２レーンの開始位置
	 * @param pos3 array配列の第３レーンの開始位置
	 * @param to ソート対象の終了位置（含まない位置）
	 * @param workArray work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void merge(final T[] array, int pos1, int pos2, int pos3, final int to, final T[] workArray, final Comparator<? super T> comparator) {
		byte state;	// state は int より byte のほうが僅かに速い？
		if (comparator.compare(array[pos1], array[pos2]) <= 0) {
			// array[p1] <= array[p2]
			if (comparator.compare(array[pos2], array[pos3]) <= 0) {
				// array[p1] <= array[p2] <= array[p3]
				state = STATE_123;
			} else if (comparator.compare(array[pos1], array[pos3]) <= 0) {
				// array[p1] <= array[p3] <= array[p2]
				state = STATE_132;
			} else {
				// array[p3] < array[p1] <= array[p2]
				state = STATE_312;
			}
		} else {
			// array[p2] < array[p1]
			if (comparator.compare(array[pos1], array[pos3]) <= 0) {
				// array[p2] < array[p1] <= array[p3]
				state = STATE_213;
			} else if (comparator.compare(array[pos2], array[pos3]) <= 0) {
				// array[p2] <= array[p3] < array[p1]
				state = STATE_231;
			} else {
				// array[p3] < array[p2] < array[p1]
				state = STATE_321;
			}
		}

		System.arraycopy(array, pos1, workArray, 0, pos3 - pos1);
		int idx = pos1;
		final int p1to = pos2 - idx;
		final int p2to = pos3 - idx;
		final int p3to = to;
		pos1 = 0;
		pos2 -= idx;

		ThreeLane:
		for (; idx < to; idx++) {
			// 以下のif文のネストは、本来なら switch case で処理するべきだが、if のネストのほうが速かったので、このような書き方にしている。
			if (state < STATE_213) {
				// 0-1
				array[idx] = workArray[pos1++];
				if (state == STATE_123) {	// 0 STATE_123
					if (pos1 >= p1to) {
						state = STATE_23;
						break ThreeLane;
					} else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						state = STATE_213;
					else
						state = STATE_231;
				} else { 				// 1 STATE_132
					if (pos1 >= p1to) {
						state = STATE_32;
						break ThreeLane;
					} else if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						state = STATE_312;
					else
						state = STATE_321;
				}
			} else if (state < STATE_312) {
				// 2-3
				array[idx] = workArray[pos2++];
				if (state == STATE_213) {	// 2 STATE_213
					if (pos2 >= p2to) {
						state = STATE_13;
						break ThreeLane;
					} else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						state = STATE_123;
					else
						state = STATE_132;
				} else { // STATE_231	// 3 STATE_231
					if (pos2 >= p2to) {
						state = STATE_31;
						break ThreeLane;
					} else if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						state = STATE_321;
					else
						state = STATE_312;
				}
			} else {
				// 4-5
				array[idx] = array[pos3++];
				if (state == STATE_312) {	// 4 STATE_312
					if (pos3 >= p3to) {
						state = STATE_12;
						break ThreeLane;
					} else if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						state = STATE_132;
					else
						state = STATE_123;
				} else { 				// 5 STATE_321
					if (pos3 >= p3to) {
						state = STATE_21;
						break ThreeLane;
					} else if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						state = STATE_231;
					else
						state = STATE_213;
				}
			}
		}
		idx++;

		TwoLane:
		for (; idx < to; idx++) {
			// 以下のif文のネストは、本来なら switch case で処理するべきだが、if のネストのほうが速かったので、このような書き方にしている。
			if (state < STATE_21) {
				if (state == STATE_12) {	// 6 STATE_12
					array[idx] = workArray[pos1++];
					if (pos1 >= p1to) {
						state = STATE_2;
						break TwoLane;
					} else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else
						state = STATE_21;
				} else { 				// 7 STATE_13
					array[idx] = workArray[pos1++];
					if (pos1 >= p1to) {
						state = STATE_3;
						break TwoLane;
					} else if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						; // モード変更なし
					else
						state = STATE_31;
				}
			} else if (state < STATE_31) {
				if (state == STATE_21) {	// 8 STATE_21
					array[idx] = workArray[pos2++];
					if (pos2 >= p2to) {
						state = STATE_1;
						break TwoLane;
					} else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else
						state = STATE_12;
				} else { 				// 9 STATE_23
					array[idx] = workArray[pos2++];
					if (pos2 >= p2to) {
						state = STATE_3;
						break TwoLane;
					} else if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						; // モード変更なし
					else
						state = STATE_32;
				}
			} else {
				if (state == STATE_31) {	// 10 STATE_31
					array[idx] = array[pos3++];
					if (pos3 >= p3to) {
						state = STATE_1;
						break TwoLane;
					} else if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else
						state = STATE_13;
				} else { 				// 11 STATE_32
					array[idx] = array[pos3++];
					if (pos3 >= p3to) {
						state = STATE_2;
						break TwoLane;
					} else if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else
						state = STATE_23;
				}
			}
		}
		idx++;

		if (state == STATE_1) {
			System.arraycopy(workArray, pos1, array, idx, p1to - pos1);
		} else if (state == STATE_2) {
			System.arraycopy(workArray, pos2, array, idx, p2to - pos2);
		}
	}


	/**
	 * MasSort
	 *
	 * 作業用一時領域はソート対象の範囲サイズの2/3(切り捨て)程度必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param workArray work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void sortImpl(final T[] array, final int from, final int to, final T[] workArray, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		// 比較キーが整数程度なら、insertionSortの方が速いが、複雑な比較関数の場合は、比較回数が少なくなる、binInsertionSortの方が有利

		if (range <= 5) {
			sort5(array, from, to, comparator);
			//BinInsertionSort.sortImpl(array, from, to, comparator);
			return;
		}
/*
		if (range < 100) {
			//sort5(array, from, to, comparator);
			//InsertionSort.sortImpl(array, from, to, comparator);
			BinInsertionSort.sortImpl(array, from, to, comparator);
			//MergeSort.sortImpl(array, from, to, workArray, comparator);
			return;
		}
*/
		final int gap = range / 3;
		final int pos1 = from;
		final int pos2 = pos1 + gap;
		final int pos3 = pos2 + gap;
		sortImpl(array, pos3, to,   workArray, comparator);
		sortImpl(array, pos2, pos3, workArray, comparator);
		sortImpl(array, from, pos2, workArray, comparator);

		// ソート済み配列の場合の高速化
		//if (comparator.compare(array[pos2 - 1], array[pos2]) <= 0 && comparator.compare(array[pos3 - 1], array[pos3]) <= 0)
		//	return;

		// マージ処理は外出しにしたほうが初回実行時に速い(再起で小さい範囲を処理している間にJITコンパイラに処理されて、結果的に高速化できる)
		merge(array, pos1, pos2, pos3, to, workArray, comparator);
	}


	/**
	 * MasSort
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void sortImpl(T[] array, int from, int to, Comparator<? super T> comparator)
	{
		final int range = to - from;
		@SuppressWarnings("unchecked")
		final T[] workArray = (T[])new Object[range / 3 * 2];

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
		return "MasSort";
	}
}
