/*
 * Mas4Sort
 *
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2017 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class Mas4Sort implements ISortAlgorithm {

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
	 * Merge two sublists / ２つのサブリストのマージ処理
	 * @param array      Merge destination / マージ先
	 * @param pos1       Index of Sublists1 / array配列のサブリスト1の開始位置
	 * @param pos2       Index of Sublists2 / array配列のサブリスト2の開始位置
	 * @param pos3       Index of Sublists3 / array配列のサブリスト3の開始位置
	 * @param pos4       Index of Sublists4 / array配列のサブリスト4の開始位置
	 * @param to         List end / ソート対象の終了位置（含まない位置）
	 * @param workArray  work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 * @param state      サブリスト先頭要素の並び
	 * @param idx        Index of array / arrayへのインデックス
	 * @param p1to       Index of last Sublists1 element (exclusive) / サブリスト1の終了位置 (+1)
	 * @param p2to       Index of last Sublists2 element (exclusive) / サブリスト2の終了位置 (+1)
	 * @param p3to       Index of last Sublists3 element (exclusive) / サブリスト3の終了位置 (+1)
	 * @param p4to       Index of last Sublists4 element (exclusive) / サブリスト4の終了位置 (+1)
	 */
	public static final <T> void merge2(final T[] array, int pos1, int pos2, int pos3, int pos4, final int to, final T[] workArray, final Comparator<? super T> comparator, int state, int idx, int p1to, int p2to, int p3to, int p4to) {
		for (; idx < to; idx++) {
			// 以下のif文のネストは、本来なら switch case で処理するべきだが、if のネストのほうが速かったので、このような書き方にしている。
			if (state < 0x20) {
				array[idx] = workArray[pos1++];
				if (pos1 >= p1to)
					break;
				if (state == 0x12) {
					if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else
						state = 0x21;
				} else if (state == 0x13) {
					if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0)
						; // モード変更なし
					else
						state = 0x31;
				} else if (state == 0x14) {
					if (comparator.compare(workArray[pos1], array[pos4]) <= 0)
						; // モード変更なし
					else
						state = 0x41;
				}
			} else if (state < 0x30) {
				array[idx] = workArray[pos2++];
				if (pos2 >= p2to)
					break;
				if (state == 0x21) {
					if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else
						state = 0x12;
				} else if (state == 0x23) {
					if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0)
						; // モード変更なし
					else
						state = 0x32;
				} else if (state == 0x24) {
					if (comparator.compare(workArray[pos2], array[pos4]) <= 0)
						; // モード変更なし
					else
						state = 0x42;
				}
			} else if (state < 0x40) {
				array[idx] = workArray[pos3++];
				if (pos3 >= p3to)
					break;
				if (state == 0x31) {
					if (comparator.compare(workArray[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else
						state = 0x13;
				} else if (state == 0x32) {
					if (comparator.compare(workArray[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else
						state = 0x23;
				} else if (state == 0x34) {
					if (comparator.compare(workArray[pos3], array[pos4]) <= 0)
						; // モード変更なし
					else
						state = 0x43;
				}
			} else {
				array[idx] = array[pos4++];
				if (pos4 >= p4to)
					break;
				if (state == 0x41) {
					if (comparator.compare(array[pos4], workArray[pos1]) < 0)
						; // モード変更なし
					else
						state = 0x14;
				} else if (state == 0x42) {
					if (comparator.compare(array[pos4], workArray[pos2]) < 0)
						; // モード変更なし
					else
						state = 0x24;
				} else if (state == 0x43) {
					if (comparator.compare(array[pos4], workArray[pos3]) < 0)
						; // モード変更なし
					else
						state = 0x34;
				}
			}
		}
		idx++;
		state &= 0xf;

		if (state == 0x1) {
			System.arraycopy(workArray, pos1, array, idx, p1to - pos1);
		} else if (state == 0x2) {
			System.arraycopy(workArray, pos2, array, idx, p2to - pos2);
		} else if (state == 0x3) {
			System.arraycopy(workArray, pos3, array, idx, p3to - pos3);
		}
	}

	/**
	 * Merge three sublists / ３つのサブリストのマージ処理
	 * @param array      Merge destination / マージ先
	 * @param pos1       Index of Sublists1 / array配列のサブリスト1の開始位置
	 * @param pos2       Index of Sublists2 / array配列のサブリスト2の開始位置
	 * @param pos3       Index of Sublists3 / array配列のサブリスト3の開始位置
	 * @param pos4       Index of Sublists4 / array配列のサブリスト4の開始位置
	 * @param to         List end / ソート対象の終了位置（含まない位置）
	 * @param workArray  work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 * @param state      サブリスト先頭要素の並び
	 * @param idx        Index of array / arrayへのインデックス
	 * @param p1to       Index of last Sublists1 element (exclusive) / サブリスト1の終了位置 (+1)
	 * @param p2to       Index of last Sublists2 element (exclusive) / サブリスト2の終了位置 (+1)
	 * @param p3to       Index of last Sublists3 element (exclusive) / サブリスト3の終了位置 (+1)
	 * @param p4to       Index of last Sublists4 element (exclusive) / サブリスト4の終了位置 (+1)
	 */
	public static final <T> void merge3(final T[] array, int pos1, int pos2, int pos3, int pos4, final int to, final T[] workArray, final Comparator<? super T> comparator, int state, int idx, int p1to, int p2to, int p3to, int p4to) {
		for (; idx < to; idx++) {
			// 以下のif文のネストは、本来なら switch case で処理するべきだが、if のネストのほうが速かったので、このような書き方にしている。
			if (state < 0x200) {
				array[idx] = workArray[pos1++];
				if (pos1 >= p1to)
					break;
				switch (state) {
				case 0x123:
					if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0)
						state = 0x213;
					else
						state = 0x231;
					break;
				case 0x124:
					if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], array[pos4]) <= 0)
						state = 0x214;
					else
						state = 0x241;
					break;
				case 0x132:
					if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						state = 0x312;
					else
						state = 0x321;
					break;
				case 0x134:
					if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], array[pos4]) <= 0)
						state = 0x314;
					else
						state = 0x341;
					break;
				case 0x142:
					if (comparator.compare(workArray[pos1], array[pos4]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0)
						state = 0x412;
					else
						state = 0x421;
					break;
				case 0x143:
					if (comparator.compare(workArray[pos1], array[pos4]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0)
						state = 0x413;
					else
						state = 0x431;
				}
			} else if (state < 0x300) {
				array[idx] = workArray[pos2++];
				if (pos2 >= p2to)
					break;
				switch (state) {
				case 0x213:
					if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0)
						state = 0x123;
					else
						state = 0x132;
					break;
				case 0x214:
					if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], array[pos4]) <= 0)
						state = 0x124;
					else
						state = 0x142;
					break;
				case 0x231:
					if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						state = 0x321;
					else
						state = 0x312;
					break;
				case 0x234:
					if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], array[pos4]) <= 0)
						state = 0x324;
					else
						state = 0x342;
					break;
				case 0x241:
					if (comparator.compare(workArray[pos2], array[pos4]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], workArray[pos1]) < 0)
						state = 0x421;
					else
						state = 0x412;
					break;
				case 0x243:
					if (comparator.compare(workArray[pos2], array[pos4]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0)
						state = 0x423;
					else
						state = 0x432;
				}
			} else if (state < 0x400) {
				array[idx] = workArray[pos3++];
				if (pos3 >= p3to)
					break;
				switch (state) {
				case 0x312:
					if (comparator.compare(workArray[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos3], workArray[pos2]) < 0)
						state = 0x132;
					else
						state = 0x123;
					break;
				case 0x314:
					if (comparator.compare(workArray[pos3], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos3], array[pos4]) <= 0)
						state = 0x134;
					else
						state = 0x143;
					break;
				case 0x321:
					if (comparator.compare(workArray[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos3], workArray[pos1]) < 0)
						state = 0x231;
					else
						state = 0x213;
					break;
				case 0x324:
					if (comparator.compare(workArray[pos3], workArray[pos2]) < 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos3], array[pos4]) <= 0)
						state = 0x234;
					else
						state = 0x243;
					break;
				case 0x341:
					if (comparator.compare(workArray[pos3], array[pos4]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos3], workArray[pos1]) < 0)
						state = 0x431;
					else
						state = 0x413;
					break;
				case 0x342:
					if (comparator.compare(workArray[pos3], array[pos4]) <= 0)
						; // モード変更なし
					else if (comparator.compare(workArray[pos3], workArray[pos2]) < 0)
						state = 0x432;
					else
						state = 0x423;
				}
			} else  {
				array[idx] = array[pos4++];
				if (pos4 >= p4to)
					break;
				switch (state) {
				case 0x412:
					if (comparator.compare(array[pos4], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos4], workArray[pos2]) < 0)
						state = 0x142;
					else
						state = 0x124;
					break;
				case 0x413:
					if (comparator.compare(array[pos4], workArray[pos1]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos4], workArray[pos3]) < 0)
						state = 0x143;
					else
						state = 0x134;
					break;
				case 0x421:
					if (comparator.compare(array[pos4], workArray[pos2]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos4], workArray[pos1]) < 0)
						state = 0x241;
					else
						state = 0x214;
					break;
				case 0x423:
					if (comparator.compare(array[pos4], workArray[pos2]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos4], workArray[pos3]) < 0)
						state = 0x243;
					else
						state = 0x234;
					break;
				case 0x431:
					if (comparator.compare(array[pos4], workArray[pos3]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos4], workArray[pos1]) < 0)
						state = 0x341;
					else
						state = 0x314;
					break;
				case 0x432:
					if (comparator.compare(array[pos4], workArray[pos3]) < 0)
						; // モード変更なし
					else if (comparator.compare(array[pos4], workArray[pos2]) < 0)
						state = 0x342;
					else
						state = 0x324;
				}
			}
		}
		idx++;
		state &= 0xff;

		merge2(array, pos1, pos2, pos3, pos4, to, workArray, comparator, state, idx, p1to, p2to, p3to, p4to);	//	マージ処理を分割したほうが、初回実行が速くなる。
	}

	/**
	 * Merge four sublists / ４つのサブリストのマージ処理
	 * @param array      Merge destination / マージ先
	 * @param pos1       Index of Sublists1 / array配列のサブリスト1の開始位置
	 * @param pos2       Index of Sublists2 / array配列のサブリスト2の開始位置
	 * @param pos3       Index of Sublists3 / array配列のサブリスト3の開始位置
	 * @param pos4       Index of Sublists4 / array配列のサブリスト4の開始位置
	 * @param to         List end / ソート対象の終了位置（含まない位置）
	 * @param workArray  work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void merge4(final T[] array, int pos1, int pos2, int pos3, int pos4, final int to, final T[] workArray, final Comparator<? super T> comparator) {
		int state = 0x1234;
		//sort4(array, pos1, pos2, pos3, pos4, comparator);	yet //これだめ
		if (comparator.compare(array[pos1], array[pos2]) <= 0) {
			// array[p1] <= array[p2]
			if (comparator.compare(array[pos2], array[pos3]) <= 0) {
				// array[p1] <= array[p2] <= array[p3]
				if (comparator.compare(array[pos2], array[pos4]) <= 0) {
					if (comparator.compare(array[pos3], array[pos4]) <= 0)
						state = 0x1234;
					else
						state = 0x1243;
				} else {
					if (comparator.compare(array[pos1], array[pos4]) <= 0)
						state = 0x1423;
					else
						state = 0x4123;
				}
			} else if (comparator.compare(array[pos1], array[pos3]) <= 0) {
				// array[p1] <= array[p3] <= array[p2]
				if (comparator.compare(array[pos3], array[pos4]) <= 0) {
					if (comparator.compare(array[pos2], array[pos4]) <= 0)
						state = 0x1324;
					else
						state = 0x1342;
				} else {
					if (comparator.compare(array[pos1], array[pos4]) <= 0)
						state = 0x1432;
					else
						state = 0x4132;
				}
			} else {
				// array[p3] < array[p1] <= array[p2]
				if (comparator.compare(array[pos1], array[pos4]) <= 0) {
					if (comparator.compare(array[pos2], array[pos4]) <= 0)
						state = 0x3124;
					else
						state = 0x3142;
				} else {
					if (comparator.compare(array[pos3], array[pos4]) <= 0)
						state = 0x3412;
					else
						state = 0x4312;
				}
			}
		} else {
			// array[p2] < array[p1]
			if (comparator.compare(array[pos1], array[pos3]) <= 0) {
				// array[p2] < array[p1] <= array[p3]
				if (comparator.compare(array[pos1], array[pos4]) <= 0) {
					if (comparator.compare(array[pos3], array[pos4]) <= 0)
						state = 0x2134;
					else
						state = 0x2143;
				} else {
					if (comparator.compare(array[pos2], array[pos4]) <= 0)
						state = 0x2413;
					else
						state = 0x4213;
				}
			} else if (comparator.compare(array[pos2], array[pos3]) <= 0) {
				// array[p2] <= array[p3] < array[p1]
				if (comparator.compare(array[pos3], array[pos4]) <= 0) {
					if (comparator.compare(array[pos1], array[pos4]) <= 0)
						state = 0x2314;
					else
						state = 0x2341;
				} else {
					if (comparator.compare(array[pos2], array[pos4]) <= 0)
						state = 0x2431;
					else
						state = 0x4231;
				}
			} else {
				// array[p3] < array[p2] < array[p1]
				if (comparator.compare(array[pos2], array[pos4]) <= 0) {
					if (comparator.compare(array[pos1], array[pos4]) <= 0)
						state = 0x3214;
					else
						state = 0x3241;
				} else {
					if (comparator.compare(array[pos3], array[pos4]) <= 0)
						state = 0x3421;
					else
						state = 0x4321;
				}
			}
		}

		System.arraycopy(array, pos1, workArray, 0, pos4 - pos1);
		int idx = pos1;
		final int p1to = pos2 - idx;
		final int p2to = pos3 - idx;
		final int p3to = pos4 - idx;
		final int p4to = to;
		pos1 = 0;
		pos2 -= idx;
		pos3 -= idx;
		for (; idx < to; idx++) {
			if (state < 0x2000) {
				array[idx] = workArray[pos1++];
				if (pos1 >= p1to)
					break;
				switch (state) {
				case 0x1234:
					if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0) {
						if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0) {
						} else  {
							state = 0x2134;
						}
					} else {
						if (comparator.compare(workArray[pos1], array[pos4]) <= 0) {
							state = 0x2314;
						} else  {
							state = 0x2341;
						}
					}
					break;
				case 0x1243:
					if (comparator.compare(workArray[pos1], array[pos4]) <= 0) {
						if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0) {
						} else  {
							state = 0x2143;
						}
					} else {
						if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0) {
							state = 0x2413;
						} else  {
							state = 0x2431;
						}
					}
					break;
				case 0x1324:
					if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0) {
						if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0) {
						} else  {
							state = 0x3124;
						}
					} else {
						if (comparator.compare(workArray[pos1], array[pos4]) <= 0) {
							state = 0x3214;
						} else  {
							state = 0x3241;
						}
					}
					break;
				case 0x1342:
					if (comparator.compare(workArray[pos1], array[pos4]) <= 0) {
						if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0) {
						} else  {
							state = 0x3142;
						}
					} else {
						if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0) {
							state = 0x3412;
						} else  {
							state = 0x3421;
						}
					}
					break;
				case 0x1423:
					if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0) {
						if (comparator.compare(workArray[pos1], array[pos4]) <= 0) {
						} else  {
							state = 0x4123;
						}
					} else {
						if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0) {
							state = 0x4213;
						} else  {
							state = 0x4231;
						}
					}
					break;
				case 0x1432:
					if (comparator.compare(workArray[pos1], workArray[pos3]) <= 0) {
						if (comparator.compare(workArray[pos1], array[pos4]) <= 0) {
						} else  {
							state = 0x4132;
						}
					} else {
						if (comparator.compare(workArray[pos1], workArray[pos2]) <= 0) {
							state = 0x4312;
						} else  {
							state = 0x4321;
						}
					}
				}
			} else if (state < 0x3000) {
				array[idx] = workArray[pos2++];
				if (pos2 >= p2to)
					break;
				switch (state) {
				case 0x2134:
					if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0) {
						if (comparator.compare(workArray[pos2], workArray[pos1]) < 0) {
						} else  {
							state = 0x1234;
						}
					} else {
						if (comparator.compare(workArray[pos2], array[pos4]) <= 0) {
							state = 0x1324;
						} else  {
							state = 0x1342;
						}
					}
					break;
				case 0x2143:
					if (comparator.compare(workArray[pos2], array[pos4]) <= 0) {
						if (comparator.compare(workArray[pos2], workArray[pos1]) < 0) {
						} else  {
							state = 0x1243;
						}
					} else {
						if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0) {
							state = 0x1423;
						} else  {
							state = 0x1432;
						}
					}
					break;
				case 0x2314:
					if (comparator.compare(workArray[pos2], workArray[pos1]) < 0) {
						if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0) {
						} else  {
							state = 0x3214;
						}
					} else {
						if (comparator.compare(workArray[pos2], array[pos4]) <= 0) {
							state = 0x3124;
						} else  {
							state = 0x3142;
						}
					}
					break;
				case 0x2341:
					if (comparator.compare(workArray[pos2], array[pos4]) <= 0) {
						if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0) {
						} else  {
							state = 0x3241;
						}
					} else {
						if (comparator.compare(workArray[pos2], workArray[pos1]) < 0) {
							state = 0x3421;
						} else  {
							state = 0x3412;
						}
					}
					break;
				case 0x2413:
					if (comparator.compare(workArray[pos2], workArray[pos1]) < 0) {
						if (comparator.compare(workArray[pos2], array[pos4]) <= 0) {
						} else  {
							state = 0x4213;
						}
					} else {
						if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0) {
							state = 0x4123;
						} else  {
							state = 0x4132;
						}
					}
					break;
				case 0x2431:
					if (comparator.compare(workArray[pos2], workArray[pos3]) <= 0) {
						if (comparator.compare(workArray[pos2], array[pos4]) <= 0) {
						} else  {
							state = 0x4231;
						}
					} else {
						if (comparator.compare(workArray[pos2], workArray[pos1]) < 0) {
							state = 0x4321;
						} else  {
							state = 0x4312;
						}
					}
				}
			} else if (state < 0x4000) {
				array[idx] = workArray[pos3++];
				if (pos3 >= p3to)
					break;
				switch (state) {
				case 0x3124:
					if (comparator.compare(workArray[pos3], workArray[pos2]) < 0) {
						if (comparator.compare(workArray[pos3], workArray[pos1]) < 0) {
						} else  {
							state = 0x1324;
						}
					} else {
						if (comparator.compare(workArray[pos3], array[pos4]) <= 0) {
							state = 0x1234;
						} else  {
							state = 0x1243;
						}
					}
					break;
				case 0x3142:
					if (comparator.compare(workArray[pos3], array[pos4]) <= 0) {
						if (comparator.compare(workArray[pos3], workArray[pos1]) < 0) {
						} else  {
							state = 0x1342;
						}
					} else {
						if (comparator.compare(workArray[pos3], workArray[pos2]) < 0) {
							state = 0x1432;
						} else  {
							state = 0x1423;
						}
					}
					break;
				case 0x3214:
					if (comparator.compare(workArray[pos3], workArray[pos1]) < 0) {
						if (comparator.compare(workArray[pos3], workArray[pos2]) < 0) {
						} else  {
							state = 0x2314;
						}
					} else {
						if (comparator.compare(workArray[pos3], array[pos4]) <= 0) {
							state = 0x2134;
						} else  {
							state = 0x2143;
						}
					}
					break;
				case 0x3241:
					if (comparator.compare(workArray[pos3], array[pos4]) <= 0) {
						if (comparator.compare(workArray[pos3], workArray[pos2]) < 0) {
						} else  {
							state = 0x2341;
						}
					} else {
						if (comparator.compare(workArray[pos3], workArray[pos1]) < 0) {
							state = 0x2431;
						} else  {
							state = 0x2413;
						}
					}
					break;
				case 0x3412:
					if (comparator.compare(workArray[pos3], workArray[pos1]) < 0) {
						if (comparator.compare(workArray[pos3], array[pos4]) <= 0) {
						} else  {
							state = 0x4312;
						}
					} else {
						if (comparator.compare(workArray[pos3], workArray[pos2]) < 0) {
							state = 0x4132;
						} else  {
							state = 0x4123;
						}
					}
					break;
				case 0x3421:
					if (comparator.compare(workArray[pos3], workArray[pos2]) < 0) {
						if (comparator.compare(workArray[pos3], array[pos4]) <= 0) {
						} else  {
							state = 0x4321;
						}
					} else {
						if (comparator.compare(workArray[pos3], workArray[pos1]) < 0) {
							state = 0x4231;
						} else  {
							state = 0x4213;
						}
					}
				}
			} else {
				array[idx] = array[pos4++];
				if (pos4 >= p4to)
					break;
				switch (state) {
				case 0x4123:
					if (comparator.compare(array[pos4], workArray[pos2]) < 0) {
						if (comparator.compare(array[pos4], workArray[pos1]) < 0) {
						} else  {
							state = 0x1423;
						}
					} else {
						if (comparator.compare(array[pos4], workArray[pos3]) < 0) {
							state = 0x1243;
						} else  {
							state = 0x1234;
						}
					}
					break;
				case 0x4132:
					if (comparator.compare(array[pos4], workArray[pos3]) < 0) {
						if (comparator.compare(array[pos4], workArray[pos1]) < 0) {
						} else  {
							state = 0x1432;
						}
					} else {
						if (comparator.compare(array[pos4], workArray[pos2]) < 0) {
							state = 0x1342;
						} else  {
							state = 0x1324;
						}
					}
					break;
				case 0x4213:
					if (comparator.compare(array[pos4], workArray[pos1]) < 0) {
						if (comparator.compare(array[pos4], workArray[pos2]) < 0) {
						} else  {
							state = 0x2413;
						}
					} else {
						if (comparator.compare(array[pos4], workArray[pos3]) < 0) {
							state = 0x2143;
						} else  {
							state = 0x2134;
						}
					}
					break;
				case 0x4231:
					if (comparator.compare(array[pos4], workArray[pos3]) < 0) {
						if (comparator.compare(array[pos4], workArray[pos2]) < 0) {
						} else  {
							state = 0x2431;
						}
					} else {
						if (comparator.compare(array[pos4], workArray[pos1]) < 0) {
							state = 0x2341;
						} else  {
							state = 0x2314;
						}
					}
					break;
				case 0x4321:
					if (comparator.compare(array[pos4], workArray[pos2]) < 0) {
						if (comparator.compare(array[pos4], workArray[pos3]) < 0) {
						} else  {
							state = 0x3421;
						}
					} else {
						if (comparator.compare(array[pos4], workArray[pos1]) < 0) {
							state = 0x3241;
						} else  {
							state = 0x3214;
						}
					}
					break;
				case 0x4312:
					if (comparator.compare(array[pos4], workArray[pos1]) < 0) {
						if (comparator.compare(array[pos4], workArray[pos3]) < 0) {
						} else  {
							state = 0x3412;
						}
					} else {
						if (comparator.compare(array[pos4], workArray[pos2]) < 0) {
							state = 0x3142;
						} else  {
							state = 0x3124;
						}
					}
				}
			}
		}
		idx++;
		state &= 0xfff;

		merge3(array, pos1, pos2, pos3, pos4, to, workArray, comparator, state, idx, p1to, p2to, p3to, p4to);	//	マージ処理を分割したほうが、初回実行が速くなる。
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
		final int gap = range / 4;
		final int pos1 = from;
		final int pos2 = pos1 + gap;
		final int pos3 = pos2 + gap;
		final int pos4 = pos3 + gap;
		sortImpl(array, pos4, to,   workArray, comparator);
		sortImpl(array, pos3, pos4, workArray, comparator);
		sortImpl(array, pos2, pos3, workArray, comparator);
		sortImpl(array, from, pos2, workArray, comparator);

		// ソート済み配列の場合の高速化
		if (comparator.compare(array[pos2 - 1], array[pos2]) <= 0 && comparator.compare(array[pos3 - 1], array[pos3]) <= 0 && comparator.compare(array[pos4 - 1], array[pos4]) <= 0)
			return;

		// マージ処理は外出しにしたほうが初回実行時に速い(再起で小さい範囲を処理している間にJITコンパイラに処理されて、結果的に高速化できる)
		merge4(array, pos1, pos2, pos3, pos4, to, workArray, comparator);
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
		final T[] workArray = (T[])new Object[range / 4 * 3];

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
		return "Mas4Sort";
	}
}