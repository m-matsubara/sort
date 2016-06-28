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
	private static final int STATE_123 = 0;
	private static final int STATE_132 = 1;
	private static final int STATE_213 = 2;
	private static final int STATE_231 = 3;
	private static final int STATE_312 = 4;
	private static final int STATE_321 = 5;

	private static final int STATE_12 = 6;
	private static final int STATE_13 = 7;
	private static final int STATE_21 = 8;
	private static final int STATE_23 = 9;
	private static final int STATE_31 = 10;
	private static final int STATE_32 = 11;

	private static final int STATE_1 = 12;
	private static final int STATE_2 = 13;
	private static final int STATE_3 = 14;

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
		int state;
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
	public static final <T> void masSort(final T[] array, final int from, final int to, final T[] workArray, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[from + 1], array[from]) < 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[from + 1], array[from]) < 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			if (comparator.compare(array[from + 2], array[from + 1]) < 0) {
				T work = array[from + 1];
				array[from + 1] = array[from + 2];
				array[from + 2] = work;
				if (comparator.compare(array[from + 1], array[from]) < 0) {
					work = array[from];
					array[from] = array[from + 1];
					array[from + 1] = work;
				}
			}
			return;
		}

		final int gap = range / 3;
		final int pos1 = from;
		final int pos2 = pos1 + gap;
		final int pos3 = pos2 + gap;
		masSort(array, pos3, to,   workArray, comparator);
		masSort(array, pos2, pos3, workArray, comparator);
		masSort(array, from, pos2, workArray, comparator);

		// ソート済み配列の場合の高速化
		if (comparator.compare(array[pos2 - 1], array[pos2]) <= 0 && comparator.compare(array[pos3 - 1], array[pos3]) <= 0)
			return;

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
	public static final <T> void masSort(T[] array, int from, int to, Comparator<? super T> comparator)
	{
		final int range = to - from;
		@SuppressWarnings("unchecked")
		final T[] workArray = (T[])new Object[range / 3 * 2];

		masSort(array, from, to, workArray, comparator);
	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		masSort(array, from, to, comparator);
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
