/*
 * MasSort (Version 2)
 *
 * 2016/06
 * 以前のバージョンのMasSortは配列を32個ほどの区画にわけマージしていたが、
 * コードが複雑になりすぎて速度が思ったほど上がらなかったため、３つの区画に分け、
 * ３つの区画の大小関係を state のような整数型変数１つで管理する方式に変更した。
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
	/**
	 * 最大サイズ５のソート
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
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
	 * ２つのレーンのマージ処理
	 * @param array マージ先
	 * @param pos1 array配列のレーン1の開始位置
	 * @param pos2 array配列のレーン2の開始位置
	 * @param pos3 array配列のレーン3の開始位置
	 * @param to ソート対象の終了位置（含まない位置）
	 * @param workArray work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 * @param state レーン先頭要素の並び
	 * @param idx arrayへのインデックス
	 * @param p1to レーン1の終了位置 (+1)
	 * @param p2to レーン2の終了位置 (+1)
	 * @param p3to レーン3の終了位置 (+1)
	 */
	public static final <T> void merge2(final T[] array, int pos1, int pos2, int pos3, final int to, final T[] workArray, final Comparator<? super T> comparator, int state, int idx, int p1to, int p2to, int p3to) {
		TwoLane:
		for (; idx < to; idx++) {
			// 以下のif文のネストは、本来なら switch case で処理するべきだが、if のネストのほうが速かったので、このような書き方にしている。
			if (state < 0x21) {
				if (state == 0x12) {
					// state = 0x12
					array[idx] = workArray[pos1++];
					if (pos1 >= p1to) {
						state = 0x2;
						break TwoLane;
					} else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else
						state = 0x21;
				} else {
					// state = 0x13
					array[idx] = workArray[pos1++];
					if (pos1 >= p1to) {
						state = 0x3;
						break TwoLane;
					} else if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						; // モード変更なし
					else
						state = 0x31;
				}
			} else if (state < 0x31) {
				if (state == 0x21) {
					// state = 0x21
					array[idx] = workArray[pos2++];
					if (pos2 >= p2to) {
						state = 0x1;
						break TwoLane;
					} else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else
						state = 0x12;
				} else {
					// state = 0x23
					array[idx] = workArray[pos2++];
					if (pos2 >= p2to) {
						state = 0x3;
						break TwoLane;
					} else if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						; // モード変更なし
					else
						state = 0x32;
				}
			} else {
				if (state == 0x31) {
					// state = 0x31
					array[idx] = array[pos3++];
					if (pos3 >= p3to) {
						state = 0x1;
						break TwoLane;
					} else if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else
						state = 0x13;
				} else {
					// state = 0x32
					array[idx] = array[pos3++];
					if (pos3 >= p3to) {
						state = 0x2;
						break TwoLane;
					} else if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else
						state = 0x23;
				}
			}
		}
		idx++;

		if (state == 0x1) {
			System.arraycopy(workArray, pos1, array, idx, p1to - pos1);
		} else if (state == 0x2) {
			System.arraycopy(workArray, pos2, array, idx, p2to - pos2);
		}
	}

	/**
	 * ３つのレーンのマージ処理
	 * @param array マージ先
	 * @param pos1 array配列のレーン1の開始位置
	 * @param pos2 array配列のレーン2の開始位置
	 * @param pos3 array配列のレーン3の開始位置
	 * @param to ソート対象の終了位置（含まない位置）
	 * @param workArray work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void merge3(final T[] array, int pos1, int pos2, int pos3, final int to, final T[] workArray, final Comparator<? super T> comparator) {
		int state;
		if (comparator.compare(array[pos1], array[pos2]) <= 0) {
			// array[p1] <= array[p2]
			if (comparator.compare(array[pos2], array[pos3]) <= 0) {
				// array[p1] <= array[p2] <= array[p3]
				state = 0x123;
			} else if (comparator.compare(array[pos1], array[pos3]) <= 0) {
				// array[p1] <= array[p3] <= array[p2]
				state = 0x132;
			} else {
				// array[p3] < array[p1] <= array[p2]
				state = 0x312;
			}
		} else {
			// array[p2] < array[p1]
			if (comparator.compare(array[pos1], array[pos3]) <= 0) {
				// array[p2] < array[p1] <= array[p3]
				state = 0x213;
			} else if (comparator.compare(array[pos2], array[pos3]) <= 0) {
				// array[p2] <= array[p3] < array[p1]
				state = 0x231;
			} else {
				// array[p3] < array[p2] < array[p1]
				state = 0x321;
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
			if (state < 0x200) {
				// state = 0x1??
				array[idx] = workArray[pos1++];
				if (pos1 >= p1to) {
					break ThreeLane;
				}
				if (state == 0x123) {
					// state = 0x123
					if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						state = 0x213;
					else
						state = 0x231;
				} else {
					// state = 0x132
					if (comparator.compare(workArray[pos1], array[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						state = 0x312;
					else
						state = 0x321;
				}
			} else if (state < 0x300) {
				// state = 0x2??
				array[idx] = workArray[pos2++];
				if (pos2 >= p2to) {
					break ThreeLane;
				}
				if (state == 0x213) {
					// state = 0x213
					if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						state = 0x123;
					else
						state = 0x132;
				} else {
					// state = 0x231
					if (comparator.compare(workArray[pos2], array[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						state = 0x321;
					else
						state = 0x312;
				}
			} else {
				// state = 0x3??
				array[idx] = array[pos3++];
				if (pos3 >= p3to) {
					break ThreeLane;
				}
				if (state == 0x312) {
					// state = 0x312
					if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						state = 0x132;
					else
						state = 0x123;
				} else {
					// state = 0x321
					if (comparator.compare(array[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos3], workArray[pos1]) < 0)
						state = 0x231;
					else
						state = 0x213;
				}
			}
		}
		idx++;
		state &= 0xff;

		merge2(array, pos1, pos2, pos3, to, workArray, comparator, state, idx, p1to, p2to, p3to);	//	マージ処理を分割したほうが、初回実行が速くなる。
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
		if (comparator.compare(array[pos2 - 1], array[pos2]) <= 0 && comparator.compare(array[pos3 - 1], array[pos3]) <= 0)
			return;

		// マージ処理は外出しにしたほうが初回実行時に速い(再起で小さい範囲を処理している間にJITコンパイラに処理されて、結果的に高速化できる)
		merge3(array, pos1, pos2, pos3, to, workArray, comparator);
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