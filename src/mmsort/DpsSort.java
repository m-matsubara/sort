/*
 * dbpSort
 *
 * Dual-pivot Stable Quicksort
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2016 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class DpsSort implements ISortAlgorithm {
	// Insersion Sortなどに切り替える要素数
	public static final int ALGORITHM_THRESHOLD = 20;

	/**
	 * dpsSort
	 *
	 * Dual-pivot Stable Quicksort
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
	public static final <T> void dpsSort(final T[] array, final int from, final int to, final T[] workArray, final int depthRemainder, final Comparator<? super T> comparator)
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

		// 以下、「÷7」の近似値
		//final int gap = range / 7;
		final int gap = (range >> 3) + (range >> 6) + 1;

		// ピボット候補値の添え字
		final int p3 = from + (range >> 1);
		final int p2 = p3 - gap;
		final int p1 = p2 - gap;
		final int p4 = p3 + gap;
		final int p5 = p4 + gap;

		// ピボット候補値（ソートして、2番目と4番目をピボット値として用いる）
		T v1 = array[p1];
		T v2 = array[p2];
		T v3 = array[p3];
		T v4 = array[p4];
		T v5 = array[p5];
		//	まず、先頭３つのソート
		if (comparator.compare(v1, v2) <= 0) {
			if (comparator.compare(v2, v3) <= 0) {
				// v1 <= v2 <= v3
			} else if (comparator.compare(v1, v3) <= 0) {
				// v1 <= v3 <= v2
				final T temp = v2; v2 = v3; v3 = temp;
			} else {
				// v3 <= v1 <= v2
				final T temp = v1; v1 = v3; v3 = v2; v2 = temp;
			}
		} else {
			if (comparator.compare(v1, v3) <= 0) {
				// v2 <= v1 <= v3
				final T temp = v1; v1 = v2; v2 = temp;
			} else if (comparator.compare(v2, v3) <= 0) {
				// v2 <= v3 <= v1
				final T temp = v1; v1 = v2; v2 = v3; v3 = temp;
			} else {
				// v3 <= v2 <= v1
				final T temp = v1; v1 = v3; v3 = temp;
			}
		}

		// v4  を挿入ソートっぽく指定位置に挿入
		if (comparator.compare(v2, v4) <= 0) {
			if (comparator.compare(v3, v4) <= 0) {
				// v3 <= v4
			} else {
				// v2 <= v4 < v3;
				final T temp = v4; v4 = v3; v3 = temp;
			}
		} else {
			if (comparator.compare(v1, v4) <= 0) {
				// v1 <= v4 < v2;
				final T temp = v4; v4 = v3; v3 = v2; v2 = temp;
			} else {
				// v4 < v1 <= v2;
				final T temp = v4; v4 = v3; v3 = v2; v2 = v1; v1 = temp;
			}
		}

		// v5  を挿入ソートっぽく指定位置に挿入
		// v2とv4だけ正しければよい。
		if (comparator.compare(v3, v5) <= 0) {
			// v3 <= v5
			if (comparator.compare(v4, v5) <= 0) {
				// v3 <= v4 <= v5
			} else {
				// v3 <= v5 < v4
				////final T temp = v5; v5 = v4; v4 = temp;
				v4 = v5;	// v2とv4だけ正しければよい。
			}
		} else {
			// v5 < v3
			if (comparator.compare(v2, v5) <= 0) {
				// v2 <= v5 < v3
				////final T temp = v5; v5 = v4; v4 = v3; v3 = temp;
				v4 = v3;	// v2とv4だけ正しければよい。
			} else {
				// v5 < v2 <= v3
				if (comparator.compare(v1, v5) <= 0) {
					// v1 <= v5 < v2 <= v3
					////final T temp = v5; v5 = v4; v4 = v3; v3 = v2; v2 = temp;
					v4 = v3; v2 = v5;	// v2とv4だけ正しければよい。
				} else {
					// v5 < v1 <= v2 <= v3
					////final T temp = v5; v5 = v4; v4 = v3; v3 = v2; v2 = v1; v1 = temp;
					v4 = v3; v2 = v1;	// v2とv4だけ正しければよい。
				}
			}
		}

		if (comparator.compare(v2, v4) != 0) {
			// v2 とv4は異なる値
			// dual pivot quick sort ベースの処理
			final T pivot1 = v2;	//	ピボット１
			final T pivot2 = v4;	//	ピボット２
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
			// ピボット１より大きく、ピボット２より小さいオブジェクトを works から array へ書き戻し
			//for (int idx = 0; idx < idx2W; idx++) {
			//	array[idxTo++] = works[idx];
			//}
			System.arraycopy(workArray, 0, array, idxTo, idx2W);
			idxTo += idx2W;

			// ピボット２以上のオブジェクトを works から array へ書き戻し
			for (int idx = range - 1; idx > idx3W; idx--) {
				array[idxTo++] = workArray[idx];
			}
			// ピボット２以上のオブジェクトを先にソート（直前に配列コピーを行っており、CPUキャッシュにヒットしやすいため）
			dpsSort(array, idx1A + idx2W, to,            workArray, depthRemainder - 1, comparator);
			// ピボット１より大きく、ピボット２より小さいオブジェクトを次にソート（まだCPUキャッシュに残っているかも？と期待して）
			dpsSort(array, idx1A,         idx1A + idx2W, workArray, depthRemainder - 1, comparator);
			// ピボット１以下のオブジェクトは最後にソート（CPUキャッシュに残っている可能性が一番低いので…。）
			dpsSort(array, from,          idx1A,         workArray, depthRemainder - 1, comparator);
		} else {
			// pivot1 とpivot2が同じ値
			// 3 way partition ベースの処理
			final T pivot = v2;	//	ピボット値
			int idx1A = from;		// value < pivot の要素へのインデックス(arrayへの配置用)
			int idx2W = 0;			// value == pivot の要素へのインデックス(workArrayへの配置用)
			int idx3W = range - 1;	// pivot < value へのインデックス(workArrayへの配置用)

			// 先頭から後方に向かってパーティション操作を行う。（一般的なクイックソートのように前後からではない）
			//   ピボット値より大きい値は配列の前方に詰めていく
			//   ピボット値より小さい値は作業領域の後方に詰めていく
			//   ピボット値と同じキー値の値は、作業領域の前方に詰めていく
			for (int idx = from; idx < to; idx++) {
				final T value = array[idx];
				final int compareVal = comparator.compare(value, pivot);
				if (compareVal < 0) {
					array[idx1A++] = value;
				} else if (compareVal > 0) {
					workArray[idx3W--] = value;
				} else {
					workArray[idx2W++] = value;
				}
			}

			int idxTo = idx1A;
			// ピボット値と同じキーのオブジェクトを works から array へ書き戻し
			//for (int idx = 0; idx < idx2W; idx++) {
			//	array[idxTo++] = works[idx];
			//}
			System.arraycopy(workArray, 0, array, idxTo, idx2W);
			idxTo += idx2W;

			// ピボット値よりも大きいオブジェクトを works から array へ書き戻し
			for (int idx = range - 1; idx > idx3W; idx--) {
				array[idxTo++] = workArray[idx];
			}

			// ピボット値より大きいオブジェクトを先にソート（直前に配列コピーを行っており、CPUキャッシュにヒットしやすいため）
			dpsSort(array, idx1A + idx2W, to,            workArray, depthRemainder - 1, comparator);
			// ピボット値より小さいオブジェクトをあとにソート（CPUキャッシュヒット率がたぶん低い）
			dpsSort(array, from,          idx1A,         workArray, depthRemainder - 1, comparator);
		}
	}

	public static final <T> void dpsSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
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
		dpsSort(array, from, to, workArray, depthRemainder, comparator);
		//InsertionSort.insertionSort(array, from, to, comparator);
	}


	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		dpsSort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "dpsSort";
	}
}
