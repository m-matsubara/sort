/*
 * mmsSort
 *
 * Dual-pivot Stable Quicksort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2016 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class MmsSort implements ISortAlgorithm {
	// Insersion Sortなどに切り替える要素数
	private static final int ALGORITHM_THRESHOLD = 20;

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
	 * mmsSort
	 *
	 * Stable Dual-pivot Quicksort
	 *
	 * Dual-pibot Quicksort を安定ソートにしたソートアルゴリズム
	 * 対象配列と同じサイズのワークエリアを使用する。
	 * 3 way partition Quicksort も併用
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param workArray work array / 作業用配列
	 * @param depthRemainder The remaining number of times of the depth of the call / 呼び出しの深さの残り回数
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void sortImpl(final T[] array, final int from, final int to, final T[] workArray, final int depthRemainder, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ

		// ソート対象配列サイズが一定数以下のときは特別扱い
		if (range <= 10) {
			//sort5(array, from, to, comparator);
			InsertionSort.sortImpl(array, from, to, comparator);
			//BinInsertionSort.sortImpl(array, from, to, comparator);
			return;
		}
/*
		if (range < ALGORITHM_THRESHOLD) {
			InsertionSort.sortImpl(array, from, to, comparator);
			//BinInsertionSort.sortImpl(array, from, to, comparator);
			return;
		}
*/
		// 呼び出し深さが限度を超えたら別（MergeSortベース）のアルゴリズムに切り替え
		if (depthRemainder < 0) {
			MatSort.sortImpl(array, from, to, comparator, workArray, (range + 9) / 10);
			//MergeSort.sortImpl(array, from, to, workArray, comparator);
			return;
		}

		T pivot1;	//	ピボット１
		T pivot2;	//	ピボット２
		if (range >= 300) {
			// ピボット候補値の添え字の差分
			final int gap = range / 12;
			// ピボット候補値の添え字
			final int center = from + (range >> 1);
			workArray[0]  = array[center - gap * 5];
			workArray[1]  = array[center - (gap << 2)];
			workArray[2]  = array[center - gap * 3];
			workArray[3]  = array[center - (gap << 1)];
			workArray[4]  = array[center - gap];
			workArray[5]  = array[center];
			workArray[6]  = array[center + gap];
			workArray[7]  = array[center + (gap << 1)];
			workArray[8]  = array[center + gap * 3];
			workArray[9]  = array[center + (gap << 2)];
			workArray[10] = array[center + gap * 5];
			InsertionSort.sortImpl(workArray, 0, 11, comparator);

			// 3等分ではなく、2:1:1位で分割されるようにpivot1, pivot2を選択する。（そのほうが比較回数が少なくなり、速度も速くなる）
			pivot1 = workArray[5];
			pivot2 = workArray[8];
		} else if (range >= 150) {
			// ピボット候補値の添え字の差分
			final int gap = range / 12;
			// ピボット候補値の添え字
			final int center = from + (range >> 1);
			workArray[0] = array[center - gap * 3];
			workArray[1] = array[center - (gap << 1)];
			workArray[2] = array[center - gap];
			workArray[3] = array[center];
			workArray[4] = array[center + gap];
			workArray[5] = array[center + (gap << 1)];
			workArray[6] = array[center + gap * 3];
			InsertionSort.sortImpl(workArray, 0, 7, comparator);

			// 3等分ではなく、2:1:1位で分割されるようにpivot1, pivot2を選択する。（そのほうが比較回数が少なくなり、速度も速くなる）
			pivot1 = workArray[3];
			pivot2 = workArray[5];
		} else {
			// ピボット候補値の添え字の差分
			final int gap = range / 6;
			// ピボット候補値の添え字
			final int p2 = from + (range >> 1);
			final int p3 = p2 + gap;
			final int p4 = p3 + gap;
			final int p1 = p2 - gap;
			final int p0 = p1 - gap;
			workArray[0] = array[p0];
			workArray[1] = array[p1];
			workArray[2] = array[p2];
			workArray[3] = array[p3];
			workArray[4] = array[p4];
			InsertionSort.sortImpl(workArray, 0, 5, comparator);

			// 3等分ではなく、2:1:1位で分割されるようにpivot1, pivot2を選択する。（そのほうが比較回数が少なくなり、速度も速くなる）
			pivot1 = workArray[2];
			pivot2 = workArray[3];
		}

		if (comparator.compare(pivot1, pivot2) != 0) {
			// pivot1 ≠ pivot2 のケース
			// dual pivot quick sort ベースの処理
			int idx1A = from;		//	value <= pivot1 の要素へのインデックス(arrayへの配置用)
			int idx2W = 0;			//	pivot1 < value < pivot2の要素へのインデックス(workArrayへの配置用)
			int idx3W = range - 1;	//	pivot2 <= value へのインデックス(workArrayへの配置用)

			// 先頭から後方に向かってパーティション操作を行う。（一般的なクイックソートのように前後からではない）
			//   ピボット１以下の値は配列の前方に詰めていく
			//   ピボット２以上の値は作業領域の後方に詰めていく
			//   ピボット１より大きくピボット２より小さい値は、作業領域の前方に詰めていく
			for (int idx = from; idx < to; idx++) {
				final T value = array[idx];
				if (comparator.compare(value, pivot1) <= 0) {
					array[idx1A++] = value;		// TODO ここで余計な代入が発生するケースがあるが、下手に最適化すると逆に遅くなってしまう…。
				} else if (comparator.compare(value, pivot2) >= 0) {
					workArray[idx3W--] = value;
				} else {
					workArray[idx2W++] = value;
				}
			}

			int idxTo = idx1A;
			// ピボット１より大きく、ピボット２より小さいオブジェクト (pivot1 < value < pivot2) を workArray から array へ書き戻し
			System.arraycopy(workArray, 0, array, idxTo, idx2W);
			idxTo += idx2W;
			// ピボット１より大きく、ピボット２より小さいオブジェクト(pivot1 < value < pivot2)をソート
			sortImpl(array, idx1A, idx1A + idx2W, workArray, depthRemainder - 1, comparator);

			// ピボット２以上のオブジェクト(pivot2 ≦ value)を workArray から array へ書き戻し
			for (int idx = range - 1; idx > idx3W; idx--) {
				array[idxTo++] = workArray[idx];
			}
			// ピボット２以上のオブジェクト(pivot2 ≦ value)をソート
			sortImpl(array, idx1A + idx2W, to, workArray, depthRemainder - 1, comparator);

			// ピボット１以下のオブジェクト(value ≦ pivot2)は最後にソート（CPUキャッシュに残っている可能性が低いので…。）
			sortImpl(array, from, idx1A, workArray, depthRemainder - 1, comparator);
		} else {
			// pivot1 ＝ pivot2 のケース
			// 3 way partition ベースの処理
			int idx1A = from;		// value < pivot の要素へのインデックス(arrayへの配置用)
			int idx2W = 0;			// value == pivot の要素へのインデックス(workArrayへの配置用)
			int idx3W = range - 1;	// pivot < value へのインデックス(workArrayへの配置用)

			// 先頭から後方に向かってパーティション操作を行う。（一般的なクイックソートのように前後からではない）
			//   ピボット値より大きい値は配列の前方に詰めていく
			//   ピボット値より小さい値は作業領域の後方に詰めていく
			//   ピボット値と同じキー値の値は、作業領域の前方に詰めていく
			for (int idx = from; idx < to; idx++) {
				final T value = array[idx];
				final int compareVal = comparator.compare(value, pivot1);
				if (compareVal < 0) {
					array[idx1A++] = value;
				} else if (compareVal > 0) {
					workArray[idx3W--] = value;
				} else {
					workArray[idx2W++] = value;
				}
			}

			int idxTo = idx1A;
			// ピボット値と同じキーのオブジェクト(value = pivot1)を workArray から array へ書き戻し
			System.arraycopy(workArray, 0, array, idxTo, idx2W);
			idxTo += idx2W;

			// ピボット値よりも大きいオブジェクト(pivot1 < value)を workArray から array へ書き戻し
			for (int idx = range - 1; idx > idx3W; idx--) {
				array[idxTo++] = workArray[idx];
			}

			// ピボット値より大きいオブジェクト(pivot1 < value)を先にソート（直前に配列コピーを行っており、CPUキャッシュにヒットしやすいため）
			sortImpl(array, idx1A + idx2W, to,    workArray, depthRemainder - 1, comparator);
			// ピボット値より小さいオブジェクト(value < pivot1)をあとにソート（CPUキャッシュヒット率がたぶん低い）
			sortImpl(array, from,          idx1A, workArray, depthRemainder - 1, comparator);
		}
	}

	public static final <T> void sortImpl(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		// 要素数
		final int range = to - from;

		// 作業用配列
		@SuppressWarnings("unchecked")
		final T[] workArray = (T[])new Object[range];

		// 呼び出し深さの許容値
		// 経験的にDual-pivot quicksortで最大呼び出し深さは、「log3(配列サイズ/他のアルゴリズムに切り替えるサイズ) * 2.2 」程度に近似できると考えられる。（乱数データの場合）
		// さらに1.2倍して、2を足すことで、余裕を持たせている。
		// …本当はもっと雑な計算式でもよい。
		final int depthRemainder = (int)(Math.log(range / ALGORITHM_THRESHOLD) / Math.log(3.0) * 2.2 * 1.2 + 2);

		// ソート本体呼び出し
		sortImpl(array, from, to, workArray, depthRemainder, comparator);
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
		return "mmsSort (dual pivot stable Sort)";
	}
}
