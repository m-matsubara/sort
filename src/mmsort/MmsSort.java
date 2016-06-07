/*
 * mmsSort
 *
 * Stable Dual-pivot Quicksort
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
	public static final <T> void mmsSort(final T[] array, final int from, final int to, final T[] workArray, final int depthRemainder, final Comparator<? super T> comparator)
	{
		final int range = to - from;		//	ソート範囲サイズ

		// ソート対象配列サイズが一定数以下のときは特別扱い
		if (range < ALGORITHM_THRESHOLD) {
			InsertionSort.insertionSort(array, from, to, comparator);
			//BinInsertionSort.binInsertionSort(array, from, to, comparator);
			return;
		}

		// 呼び出し深さが限度を超えたら別（MergeSortベース）のアルゴリズムに切り替え
		if (depthRemainder < 0) {
			MatSort.matSort(array, from, to, comparator, workArray, (range + 9) / 10);
			//MergeSort.mergeSort(array, from, to, workArray, comparator);
			return;
		}

		T pivot1;	//	ピボット１
		T pivot2;	//	ピボット２
		if (range >= 150) {
			// ピボット候補値の添え字の差分
			final int gap = range / 12;
			// ピボット候補値の添え字
			final int center = from + (range >> 1);
			final int p3 = center - gap;
			final int p2 = p3 - gap;
			final int p1 = p2 - gap;
			final int p0 = p1 - gap;
			final int p4 = center + gap;
			final int p5 = p4 + gap;
			final int p6 = p5 + gap;
			final int p7 = p6 + gap;
			workArray[0] = array[p0];
			workArray[1] = array[p1];
			workArray[2] = array[p2];
			workArray[3] = array[p3];
			workArray[4] = array[p4];
			workArray[5] = array[p5];
			workArray[6] = array[p6];
			workArray[7] = array[p7];
			BinInsertionSort.binInsertionSort(workArray, 0, 8, comparator);

			pivot1 = workArray[2];
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
			BinInsertionSort.binInsertionSort(workArray, 0, 5, comparator);

			pivot1 = workArray[1];
			pivot2 = workArray[3];
		}

		if (comparator.compare(pivot1, pivot2) != 0) {
			// pivot1 ≠ pivot2 のケース
			// dual pivot quick sort ベースの処理
			int idx1A = from;		//	value <= pivot1 の要素へのインデックス(arraysへの配置用)
			int idx2W = 0;			//	pivot1 < value < pivot2の要素へのインデックス(worksへの配置用)
			int idx3W = range - 1;	//	pivot2 <= value へのインデックス(worksへの配置用)

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
			// ピボット１より大きく、ピボット２より小さいオブジェクト (pivot1 < value < pivot2) を works から array へ書き戻し
			//for (int idx = 0; idx < idx2W; idx++) {
			//	array[idxTo++] = works[idx];
			//}
			System.arraycopy(workArray, 0, array, idxTo, idx2W);
			idxTo += idx2W;
			// ピボット１より大きく、ピボット２より小さいオブジェクト(pivot1 < value < pivot2)をソート
			mmsSort(array, idx1A, idx1A + idx2W, workArray, depthRemainder - 1, comparator);

			// ピボット２以上のオブジェクト(pivot2 ≦ value)を works から array へ書き戻し
			for (int idx = range - 1; idx > idx3W; idx--) {
				array[idxTo++] = workArray[idx];
			}
			// ピボット２以上のオブジェクト(pivot2 ≦ value)をソート
			mmsSort(array, idx1A + idx2W, to, workArray, depthRemainder - 1, comparator);

			// ピボット１以下のオブジェクト(value ≦ pivot2)は最後にソート（CPUキャッシュに残っている可能性が低いので…。）
			mmsSort(array, from, idx1A, workArray, depthRemainder - 1, comparator);
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
			// ピボット値と同じキーのオブジェクト(value = pivot1)を works から array へ書き戻し
			//for (int idx = 0; idx < idx2W; idx++) {
			//	array[idxTo++] = works[idx];
			//}
			System.arraycopy(workArray, 0, array, idxTo, idx2W);
			idxTo += idx2W;

			// ピボット値よりも大きいオブジェクト(pivot1 < value)を works から array へ書き戻し
			for (int idx = range - 1; idx > idx3W; idx--) {
				array[idxTo++] = workArray[idx];
			}

			// ピボット値より大きいオブジェクト(pivot1 < value)を先にソート（直前に配列コピーを行っており、CPUキャッシュにヒットしやすいため）
			mmsSort(array, idx1A + idx2W, to,    workArray, depthRemainder - 1, comparator);
			// ピボット値より小さいオブジェクト(value < pivot1)をあとにソート（CPUキャッシュヒット率がたぶん低い）
			mmsSort(array, from,          idx1A, workArray, depthRemainder - 1, comparator);
		}
	}

	public static final <T> void mmsSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		// 要素数
		final int range = to - from;

		// 作業用配列
		@SuppressWarnings("unchecked")
		final T[] workArray = (T[])new Object[range];

		// 呼び出し深さの許容値 (log2(range))
		// 経験的にDual-pivot quicksortで最大呼び出し深さは、「log3(配列サイズ/他のアルゴリズムに切り替えるサイズ) * 2.2 」程度に近似できると考えられる。（乱数データの場合）
		// さらに1.2倍して、2を足すことで、余裕を持たせている。
		// …本当はもっと雑な計算式でもよい。
		final int depthRemainder = (int)(Math.log(range / ALGORITHM_THRESHOLD) / Math.log(3.0) * 2.2 * 1.2 + 2);

		// ソート本体呼び出し
		mmsSort(array, from, to, workArray, depthRemainder, comparator);
	}


	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		mmsSort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "mmsSort";
	}
}
