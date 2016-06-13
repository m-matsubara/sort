/*
 * No7Sort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2016 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class No7Sort implements ISortAlgorithm {
	private static final int MODE_123 = 0;
	private static final int MODE_132 = 1;
	private static final int MODE_213 = 2;
	private static final int MODE_231 = 3;
	private static final int MODE_312 = 4;
	private static final int MODE_321 = 5;
	private static final int MODE_12 = 6;
	private static final int MODE_13 = 7;
	private static final int MODE_21 = 8;
	private static final int MODE_23 = 9;
	private static final int MODE_31 = 10;
	private static final int MODE_32 = 11;
	private static final int MODE_1 = 12;
	private static final int MODE_2 = 13;
	private static final int MODE_3 = 14;

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
		int mode;
		if (comparator.compare(array[pos1], array[pos2]) <= 0) {
			// array[p1] <= array[p2]
			if (comparator.compare(array[pos2], array[pos3]) <= 0) {
				// array[p1] <= array[p2] <= array[p3]
				mode = MODE_123;
			} else if (comparator.compare(array[pos1], array[pos3]) <= 0) {
				// array[p1] <= array[p3] <= array[p2]
				mode = MODE_132;
			} else {
				// array[p3] < array[p1] <= array[p2]
				mode = MODE_312;
			}
		} else {
			// array[p2] < array[p1]
			if (comparator.compare(array[pos1], array[pos3]) <= 0) {
				// array[p2] < array[p1] <= array[p3]
				mode = MODE_213;
			} else if (comparator.compare(array[pos2], array[pos3]) <= 0) {
				// array[p2] <= array[p3] < array[p1]
				mode = MODE_231;
			} else {
				// array[p3] < array[p2] < array[p1]
				mode = MODE_321;
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
			if (mode < MODE_213 ) {
				// 0-1
				if (mode == MODE_123) {	// 0 MODE_123
					array[idx] = workArray[pos1++];
					if (pos1 >= p1to) {
						mode = MODE_23;
						idx++;
						break ThreeLane;
					} else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						mode = MODE_213;
					else
						mode = MODE_231;
				} else { 				// 1 MODE_132
					array[idx] = workArray[pos1++];
					if (pos1 >= p1to) {
						mode = MODE_32;
						idx++;
						break ThreeLane;
					} else if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						mode = MODE_312;
					else
						mode = MODE_321;
				}
			} else if (mode < MODE_312) {
				if (mode == MODE_213) {	// 2 MODE_213
					array[idx] = workArray[pos2++];
					if (pos2 >= p2to) {
						mode = MODE_13;
						idx++;
						break ThreeLane;
					} else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						mode = MODE_123;
					else
						mode = MODE_132;
				} else { // MODE_231	// 3 MODE_231
					array[idx] = workArray[pos2++];
					if (pos2 >= p2to) {
						mode = MODE_31;
						idx++;
						break ThreeLane;
					} else if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						mode = MODE_321;
					else
						mode = MODE_312;
				}
			} else {
				if (mode == MODE_312) {	// 4 MODE_312
					array[idx] = array[pos3++];
					if (pos3 >= p3to) {
						mode = MODE_12;
						idx++;
						break ThreeLane;
					} else if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						mode = MODE_132;
					else
						mode = MODE_123;
				} else { 				// 5 MODE_321
					array[idx] = array[pos3++];
					if (pos3 >= p3to) {
						mode = MODE_21;
						idx++;
						break ThreeLane;
					} else if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						mode = MODE_231;
					else
						mode = MODE_213;
				}
			}
		}

		TwoLane:
		for (; idx < to; idx++) {
			// 以下のif文のネストは、本来なら switch case で処理するべきだが、if のネストのほうが速かったので、このような書き方にしている。
			if (mode < MODE_21) {
				if (mode == MODE_12) {	// 6 MODE_12
					array[idx] = workArray[pos1++];
					if (pos1 >= p1to) {
						mode = MODE_2;
						idx++;
						break TwoLane;
					} else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else
						mode = MODE_21;
				} else { 				// 7 MODE_13
					array[idx] = workArray[pos1++];
					if (pos1 >= p1to) {
						mode = MODE_3;
						idx++;
						break TwoLane;
					} else if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						; // モード変更なし
					else
						mode = MODE_31;
				}
			} else if (mode < MODE_31) {
				if (mode == MODE_21) {	// 8 MODE_21
					array[idx] = workArray[pos2++];
					if (pos2 >= p2to) {
						mode = MODE_1;
						idx++;
						break TwoLane;
					} else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else
						mode = MODE_12;
				} else { 				// 9 MODE_23
					array[idx] = workArray[pos2++];
					if (pos2 >= p2to) {
						mode = MODE_3;
						idx++;
						break TwoLane;
					} else if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						; // ���[�h�ύX�Ȃ�
					else
						mode = MODE_32;
				}
			} else {
				if (mode == MODE_31) {	// 10 MODE_31
					array[idx] = array[pos3++];
					if (pos3 >= p3to) {
						mode = MODE_1;
						idx++;
						break TwoLane;
					} else if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else
						mode = MODE_13;
				} else { 				// 11 MODE_32
					array[idx] = array[pos3++];
					if (pos3 >= p3to) {
						mode = MODE_2;
						idx++;
						break TwoLane;
					} else if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else
						mode = MODE_23;
				}
			}
		}
		if (mode == MODE_1) {
			System.arraycopy(workArray, pos1, array, idx, p1to - pos1);
		} else if (mode == MODE_2) {
			System.arraycopy(workArray, pos2, array, idx, p2to - pos2);
		}
	}


	/**
	 * No7Sort
	 *
	 * 作業用一時領域はソート対象の範囲サイズの2/3(切り捨て)程度必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param workArray work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void no7Sort(final T[] array, final int from, final int to, final T[] workArray, final Comparator<? super T> comparator)
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
		no7Sort(array, from, pos2, workArray, comparator);
		no7Sort(array, pos2, pos3, workArray, comparator);
		no7Sort(array, pos3, to,   workArray, comparator);

		// ソート済み配列の場合の高速化
		if (comparator.compare(array[pos2 - 1], array[pos2]) <= 0 && comparator.compare(array[pos3 - 1], array[pos3]) <= 0)
			return;

		// マージ処理は外出しにしたほうが初回実行時に速い(再起で小さい範囲を処理している間にJITコンパイラに処理されて、結果的に高速化できる)
		merge(array, pos1, pos2, pos3, to, workArray, comparator);
	}


	/**
	 * No7Sort
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void no7Sort(T[] array, int from, int to, Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] workArray = (T[])new Object[(to - from) / 3 * 2];

		no7Sort(array, from, to, workArray, comparator);
	}

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		no7Sort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "No7Sort";
	}
}
