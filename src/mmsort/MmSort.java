/*
 * MasSort メモリ転送回数を減らした高速化版マージソート（ソート対象配列と同サイズの作業領域が必要）
 * MatSort 作業領域サイズを抑えることのできる改良版マージソート
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class MmSort<T> {
	/**
	 * Insertion sort
	 * 挿入ソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void insertSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			if (comparator.compare(array[from + 1], array[from + 2]) > 0) {
				T work = array[from + 1];
				array[from + 1] = array[from + 2];
				array[from + 2] = work;
			if (comparator.compare(array[from], array[from + 1]) > 0) {
					work = array[from];
					array[from] = array[from + 1];
					array[from + 1] = work;
			}
			}
			return;
		}
		for (int i = from; i < to; i++) {
			final T key = array[i];
			int j = i - 1;
			while (j >= from && comparator.compare(array[j], key) > 0) {
				array[j + 1] = array[j];
				j--;
			}
			array[j + 1] = key;
		}
	}

	/**
	 * 挿入ソート（挿入位置を二分検索によって探索する）
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static <T> void insertBinSort(T[] array, int from, int to, Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			if (comparator.compare(array[from + 1], array[from + 2]) > 0) {
				T work = array[from + 1];
				array[from + 1] = array[from + 2];
				array[from + 2] = work;
				if (comparator.compare(array[from], array[from + 1]) > 0) {
					work = array[from];
					array[from] = array[from + 1];
					array[from + 1] = work;
				}
			}
			return;
		}
		//	前から既に整列済みの部分を省く・逆順の列が続く場合はその部分を反転
		int startIdx = from + 1;
		if (comparator.compare(array[startIdx - 1], array[startIdx]) <= 0) {
			for (startIdx++; startIdx < to; startIdx++) {
				if (comparator.compare(array[startIdx - 1], array[startIdx]) > 0) {
					break;
				}
			}
		} else {
			for (startIdx++; startIdx < to; startIdx++) {
				if (comparator.compare(array[startIdx - 1], array[startIdx]) <= 0) {
					break;
				}
			}
			int fromIdx = from;
			int toIdx = startIdx - 1;
			while (fromIdx < toIdx) {
				T temp = array[fromIdx];
				array[fromIdx] = array[toIdx];
				array[toIdx] = temp;
				fromIdx++;
				toIdx--;
			}
		}

		for (int i = startIdx; i < to; i++) {
			T key = array[i];
			int fromIdx = from;
			int toIdx = i;
			int curIdx = (fromIdx + toIdx) / 2;
			while (fromIdx < toIdx) {
				final int compVal = comparator.compare(key, array[curIdx]);
				if (compVal < 0) {
					toIdx = curIdx;
				} else {
					fromIdx = curIdx + 1;
				}
				curIdx = (fromIdx + toIdx) / 2;
			}
			for (int j = i - 1; j >= curIdx; j--) {
				array[j + 1] = array[j];
			}
			array[curIdx] = key;
		}
}


	/**
	 * マージソート
	 * 作業用一時領域はソート対象の範囲サイズ / 2（切り上げ）が必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param works work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void mergeSort(final T[] array, final int from, final int to, final T[] works, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			if (comparator.compare(array[from + 1], array[from + 2]) > 0) {
				T work = array[from + 1];
				array[from + 1] = array[from + 2];
				array[from + 2] = work;
				if (comparator.compare(array[from], array[from + 1]) > 0) {
					work = array[from];
					array[from] = array[from + 1];
					array[from + 1] = work;
				}
			}
			return;
		} else if (range < 200) {
			insertBinSort(array, from, to, comparator);
			return;
		}

		int mid = (from + to) / 2;	//	中央位置（範囲１と範囲２の境界）
		mergeSort(array, from, mid, works, comparator);	//	範囲１（最小位置～中間位置）のソート
		mergeSort(array, mid, to, works, comparator);	//	範囲２（中間位置～最大位置）のソート

		int idx = from;		//	現在処理中の位置（範囲１と範囲２の小さい方をこの位置へ配置（移動）する）
		int idx1 = from;		//	範囲１の次の値のインデックス
		int idx2 = mid;		//	範囲２の次の値のインデックス

		if (comparator.compare(array[mid - 1], array[mid]) < 0)
			return;			//	範囲１の値はすべて範囲２の値より小さかった（再配置なし）…ソート済みの配列に対する高速化


		//	範囲１の値が小さい間繰り返す（再配置なし）
/*		//	（この処理無い方が速い…ショック）
		while (idx1 < mid)  {
			if (comparator.compare(array[idx1], array[idx2]) > 0) {
				break;
			}
			idx1++;
		}
*/

		//	範囲１をワーク配列にコピー
		if (mid - idx1 > 0) {
			System.arraycopy(array, idx1, works, idx1 - from, mid - idx1);
		}

		//	ワーク領域（範囲１のコピー）と範囲２をマージしてソート対象(array)の先頭から詰めていく
		idx = idx1;	//	現在処理中の位置を設定
		while (idx1 < mid && idx2 < to)  {
			final T value1 = works[idx1 - from];
			final T value2 = array[idx2];
			if (comparator.compare(value1, value2) <= 0) {
				array[idx] = value1;
				idx1++;
			} else {
				array[idx] = value2;
				idx2++;
			}
			idx++;
		}

		//	残ったワーク領域をソート対象へ詰める
		while (idx1 < mid)  {
			array[idx] = works[idx1 - from];
			idx++;
			idx1++;
		}

//		if (idx != idx2)
//			throw new RuntimeException("Position error");
	}


	/**
	 * Merge sort
	 * マージソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void mergeSort(T[] array, int from, int to, Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] works = (T[])new Object[(to - from) / 2];

		mergeSort(array, from, to, works, comparator);
	}


	/**
	 * Binary search (head position of the same value)
	 * 二分探索（同じ値がるときは先頭位置）
	 * @param key search key / 探索するキー
	 * @param array search target / 探索対象
	 * @param from index of first element / 探索対象の開始位置
	 * @param to index of last element (exclusive) / 探索対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 * @return search result (element index) / 探索結果（添え字）
	 */
	public static final <T> int binSearchH(final T key, final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		int fromIdx = from;
		int toIdx = to;
		int curIdx = (fromIdx + toIdx) / 2;
		while (fromIdx < toIdx) {
			final int compVal = comparator.compare(key, array[curIdx]);
			if (compVal <= 0) {
				toIdx = curIdx;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			} else {
				fromIdx = curIdx + 1;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			}
		}
		return curIdx;
	}

	/**
	 * Binary search (tail position of the same value)
	 * 二分探索（同じ値がるときは最終位置の後ろ）
	 * @param key search key / 探索するキー
	 * @param array search target / 探索対象
	 * @param from index of first element / 探索対象の開始位置
	 * @param to index of last element (exclusive) / 探索対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 * @return search result (element index) / 探索結果（添え字）
	 */
	public static final <T> int binSearchT(final T key, final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		int fromIdx = from;
		int toIdx = to;
		int curIdx = (fromIdx + toIdx) / 2;
		while (fromIdx < toIdx) {
			final int compVal = comparator.compare(key, array[curIdx]);
			if (compVal < 0) {
				toIdx = curIdx;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			} else {
				fromIdx = curIdx + 1;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			}
		}
		return curIdx;
	}

	/**
	 * rotate
	 * ローテーション
	 * @param array rotate target / ローテーション対象
	 * @param start index of rotate first element / ローテーションの開始位置
	 * @param range1 size of rotation range 1 / ローテーション範囲1のサイズ
	 * @param range2 size of rotation range 2 /  ローテーション範囲2のサイズ
	 */
	public static final <T> void rotateRange(final T[] array, final int start, final int range1, final int range2) {
		int pos = start;
		T posObject = array[pos];
		int startPos = pos;
		int range = range1 + range2;
		for (int idx = 0; idx < range; idx++) {
			int destPos;
			if (pos < start + range1)
				destPos = pos + range2;
			else
				destPos = pos - range1;
			T value = posObject;
			posObject = array[destPos];
			array[destPos] = value;
			pos = destPos;
			if (pos == startPos) {
				pos++;
				posObject = array[pos];
				startPos = pos;
			}
		}
	}


	/**
	 * In-place Merge
	 * インプレース・マージ
	 * @param array merge target / マージ対象
	 * @param from index of range1 first element / マージ対象の開始位置
	 * @param mid index of range2 first element / マージ対象２の開始位置
	 * @param to index of last element (exclusive) / マージ対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void ipMerge(final T[] array, final int from, final int mid, final int to, final Comparator<? super T> comparator)
	{
		if ((mid - from == 0) || (to - mid == 0))
			return;
		if (comparator.compare(array[mid - 1], array[mid]) < 0)
			return;
		if ((mid - from == 1) && (to - mid == 1)) {
			if (comparator.compare(array[from], array[mid]) > 0) {
				T work = array[from];
				array[from] = array[mid];
				array[mid] = work;
			}
			return;
		}

		if (mid - from == 1) {
			T key = array[from];
			int pos = binSearchH(key, array, from + 1, to, comparator);
			if (pos != from + 1)
				rotateRange(array, from, 1, pos - from - 1);
			return;
		}
		if (to - mid == 1) {
			T key = array[to - 1];
			int pos = binSearchT(key, array, from, to - 1, comparator);
			if (pos != to - 1)
				rotateRange(array, pos, to - 1 - pos, 1);
			return;
		}

		int idxRotate1 = from + (mid - from) / 2;
		T pivot = array[idxRotate1];
		int idxRotate2 = binSearchH(pivot, array, mid, to, comparator);
		int range1 = mid - idxRotate1;
		int range2 = idxRotate2 - mid;
		rotateRange(array, idxRotate1, range1, range2);
		ipMerge(array, from, idxRotate1, idxRotate1 + range2, comparator);
		ipMerge(array, idxRotate1 + range2, idxRotate2, to, comparator);
	}


	/**
	 * In-place Merge sort
	 * インプレース・マージソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void ipMergeSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[from], array[from + 1]) > 0) {
				T work = array[from];
				array[from] = array[from + 1];
				array[from + 1] = work;
			}
			if (comparator.compare(array[from + 1], array[from + 2]) > 0) {
				T work = array[from + 1];
				array[from + 1] = array[from + 2];
				array[from + 2] = work;
				if (comparator.compare(array[from], array[from + 1]) > 0) {
					work = array[from];
					array[from] = array[from + 1];
					array[from + 1] = work;
				}
			}
			return;
		} else if (range < 200) {
			insertBinSort(array, from, to, comparator);
			return;
		}

		int mid = (from + to) / 2;	//	中央位置（範囲１と範囲２の境界）
		ipMergeSort(array, from, mid, comparator);	//	範囲１（最小位置～中間位置）のソート
		ipMergeSort(array, mid, to, comparator);	//	範囲２（中間位置～最大位置）のソート

		ipMerge(array, from, mid, to, comparator);
	}


	/**
	 * Improved Merge sort
	 * (Outputs directly to sort the results in the work area, to return while merge from the work area to the original sequence)
	 * Temporary area for work need the same size as the sort object
	 *
	 * 改良版マージソート
	 * （ソート結果を作業領域に直接出力し、作業領域から元の配列にマージしながら戻す）
	 * 作業用一時領域はソート対象と同じサイズが必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param works work area / 作業用一時領域
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void improvedMergeSort(final T[] arrayFrom, final T[] arrayTo, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			arrayTo[from] = arrayFrom[from];
			return;
		} else if (range == 2) {
			if (comparator.compare(arrayFrom[from], arrayFrom[from + 1]) > 0) {
				arrayTo[from]     = arrayFrom[from + 1];
				arrayTo[from + 1] = arrayFrom[from];
			} else {
				arrayTo[from]     = arrayFrom[from];
				arrayTo[from + 1] = arrayFrom[from + 1];
			}
			return;
		} else if (range == 3) {
			System.arraycopy(arrayFrom, from, arrayTo, from, to - from);
			if (comparator.compare(arrayTo[from], arrayTo[from + 1]) > 0) {
				T work = arrayTo[from];
				arrayTo[from] = arrayTo[from + 1];
				arrayTo[from + 1] = work;
			}
			if (comparator.compare(arrayTo[from + 1], arrayTo[from + 2]) > 0) {
				T work = arrayTo[from + 1];
				arrayTo[from + 1] = arrayTo[from + 2];
				arrayTo[from + 2] = work;
				if (comparator.compare(arrayTo[from], arrayTo[from + 1]) > 0) {
					work = arrayTo[from];
					arrayTo[from] = arrayTo[from + 1];
					arrayTo[from + 1] = work;
				}
			}
			return;
		} else if (range < 200) {
			System.arraycopy(arrayFrom, from, arrayTo, from, to - from);
			insertBinSort(arrayTo, from, to, comparator);
			return;
		}

		int mid = (from + to) / 2;	//	中央位置（範囲１と範囲２の境界）
		improvedMergeSort(arrayTo, arrayFrom, from, mid, comparator);	//	範囲１（最小位置～中間位置）のソート
		improvedMergeSort(arrayTo, arrayFrom, mid, to, comparator);	//	範囲２（中間位置～最大位置）のソート

		int idx = from;		//	現在処理中の位置（範囲１と範囲２の小さい方をこの位置へ配置（移動）する）
		int idx1 = from;	//	範囲１の次の値のインデックス
		int idx2 = mid;		//	範囲２の次の値のインデックス

		//	ワーク領域（範囲１のコピー）と範囲２をマージしてソート対象(array)の先頭から詰めていく
		idx = idx1;	//	現在処理中の位置を設定
		while (idx1 < mid && idx2 < to)  {
			final T value1 = arrayFrom[idx1];
			final T value2 = arrayFrom[idx2];
			if (comparator.compare(value1, value2) <= 0) {
				arrayTo[idx] = value1;
				idx1++;
			} else {
				arrayTo[idx] = value2;
				idx2++;
			}
			idx++;
		}

		//	残ったワーク領域をソート対象へ詰める
		while (idx1 < mid)  {
			arrayTo[idx] = arrayFrom[idx1];
			idx++;
			idx1++;
		}
		while (idx2 < to)  {
			arrayTo[idx] = arrayFrom[idx2];
			idx++;
			idx2++;
		}
	}


	/**
	 * Improved Merge sort
	 * (Outputs directly to sort the results in the work area, to return while merge from the work area to the original sequence)
	 * Temporary area for work need the same size as the sort object
	 *
	 * 改良版マージソート
	 * （ソート結果を作業領域に直接出力し、作業領域から元の配列にマージしながら戻す）
	 * 作業用一時領域はソート対象と同じサイズが必要
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void improvedMergeSort(final T[] array, final int from, final int to, Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] works = (T[])new Object[array.length];

		System.arraycopy(array, from, works, from, to - from);
		improvedMergeSort(works, array, from, to, comparator);
	}


	/**
	 * MasSort
	 *
	 * Faster version of the merge sort.
	 * Merging by dividing the sort object in the many blocks.
	 * Compared to normal merge sort, it is possible to reduce the memory transfer count,
	 * it is considered particularly advantageous in slow memory access environment.
	 *
	 * 高速化版マージソート
	 * ソート対象を多くのブロックに分割してマージする。
	 * 通常のマージソートに比べて、メモリ転送回数を減らすことができるため、メモリアクセスの遅い環境で特に有利と考えられる。
	 * @param arrayFrom sort source / ソート元
	 * @param arrayTo sort result / ソート結果
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param destIdx sort result destination position /  ソート済み内容配置先インデックス
	 * @param comparator comparator of array element / 比較器
	 */
	public static final <T> void masSort(final T[] arrayFrom, final T[] arrayTo, final int from, final int to, final int destIdx, final Comparator<? super T> comparator)
	{
		final int range = to - from;			//	ソート範囲サイズ

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			arrayTo[destIdx] = arrayFrom[from];
			return;
		} else if (range == 2) {
			if (comparator.compare(arrayFrom[from], arrayFrom[from + 1]) > 0) {
				arrayTo[destIdx]     = arrayFrom[from + 1];
				arrayTo[destIdx + 1] = arrayFrom[from];
			} else {
				arrayTo[destIdx]     = arrayFrom[from];
				arrayTo[destIdx + 1] = arrayFrom[from + 1];
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(arrayFrom[from], arrayFrom[from + 1]) > 0) {
				T work = arrayFrom[from];
				arrayFrom[from] = arrayFrom[from + 1];
				arrayFrom[from + 1] = work;
			}
			if (comparator.compare(arrayFrom[from + 1], arrayFrom[from + 2]) > 0) {
				T work = arrayFrom[from + 1];
				arrayFrom[from + 1] = arrayFrom[from + 2];
				arrayFrom[from + 2] = work;
				if (comparator.compare(arrayFrom[from], arrayFrom[from + 1]) > 0) {
					work = arrayFrom[from];
					arrayFrom[from] = arrayFrom[from + 1];
					arrayFrom[from + 1] = work;
				}
			}
			System.arraycopy(arrayFrom, from, arrayTo, destIdx, to - from);
			return;
		} else if (range < 200) {
			insertBinSort(arrayFrom, from, to, comparator);
			System.arraycopy(arrayFrom, from, arrayTo, destIdx, to - from);
			return;
		}

		int blockCount = 32;							//	通常のマージソートでは２つのブロックに分けるが、masSortではより多くのブロックに分けてマージする。
		int[] blockStart = new int[blockCount];			//	各ブロックの先頭位置（arrayへの添え字）を保持
		int[] blockEnd = new int[blockCount];			//	各ブロックの最終位置 + 1（arrayへの添え字）を保持
		int[] fromOrderBlocks = new int[blockCount];	//	各ブロックの先頭要素を比較し、小さい値を持つブロック№を保持(0 ～ blockCount-1)
		@SuppressWarnings("unchecked")
		T[]   blockStartItemCache = (T[])new Object[blockCount];	//	各ブロックの先頭要素をキャッシュ(CPUのキャッシュヒット率が上がるかなと思って)

		//	ソート対象をブロックに分割・ブロックごとにソート
		for (int blockIdx = 0; blockIdx < blockCount; blockIdx++) {
			blockStart[blockIdx] = (int)((long)range * blockIdx / blockCount);
			blockEnd[blockIdx] = (int)((long)range * (blockIdx + 1) / blockCount);
			masSort(arrayTo, arrayFrom, destIdx + blockStart[blockIdx], destIdx + blockEnd[blockIdx], from + blockStart[blockIdx], comparator);
			fromOrderBlocks[blockIdx] = blockIdx;
		}


		//	各ブロックの先頭要素をキャッシュ(CPUのキャッシュヒット率が上がるかなと思って)
		for (int blockIdx = 0; blockIdx < blockCount; blockIdx++) {
			blockStartItemCache[blockIdx] = arrayFrom[from + blockStart[blockIdx]];
		}

		//	各ブロックの先頭で比較して、小さい値を持つブロック順にブロックをソート → fromOrderBlocks
		//	（二分探索を使った挿入ソート）
		for (int fromOrderBlocksIdx = 1; fromOrderBlocksIdx < blockCount; fromOrderBlocksIdx++) {
			int blockIdx = fromOrderBlocks[fromOrderBlocksIdx];	//	ブロックインデックス（このブロックの最小値は他のブロックの最小値と比較して何番目か調べる）
			//T key = works[blockStart[blockIdx]];
			T key = blockStartItemCache[blockIdx];				//	キー値
			int fromIdx = 0;										//	探索範囲の最小インデックス
			int toIdx = fromOrderBlocksIdx;						//	探索範囲の最大インデックス + 1
			int curIdx = (fromIdx + toIdx) / 2;					//	探索範囲の中央インデックス
			//	二分探索処理
			while (fromIdx < toIdx) {
				int curBlockIdx = fromOrderBlocks[curIdx];
				//int compVal = comparator.compare(key, works[blockStart[curBlockIdx]]);
				int compVal = comparator.compare(key, blockStartItemCache[curBlockIdx]);
				if (compVal == 0) {		//	キー値が同じであるなら、ブロック番号の小さい方を先にする。（安定のため）
					if (blockIdx < curBlockIdx)
						compVal = -1;
					else
						compVal = 1;
				}
				if (compVal < 0) {
					toIdx = curIdx;
				} else {
					fromIdx = curIdx + 1;
				}
				curIdx = (fromIdx + toIdx) / 2;
			}
			//	挿入処理
			for (int j = fromOrderBlocksIdx - 1; j >= curIdx; j--) {
				fromOrderBlocks[j + 1] = fromOrderBlocks[j];
			}
			fromOrderBlocks[curIdx] = fromOrderBlocksIdx;
		}

		//	先頭要素でソートしたブロックごとにブロックの末尾が次のブロックの先頭より小さければ、ブロック全体を転送することで高速化
		//	(ソート済み・逆順ソート済みで効果)
		int idx = 0;

		int blockTransIdx;
		for (blockTransIdx = 0; blockTransIdx < blockCount - 1; blockTransIdx++) {
			int blockIdx = fromOrderBlocks[blockTransIdx];		//	比較の基準のブロック
			int blockIdx2 = fromOrderBlocks[blockTransIdx + 1];	//	比較の基準の次のブロック

			//	「比較の基準のブロックの末尾」と「比較の基準の次のブロックの先頭」を比較
			int compVal = comparator.compare(arrayFrom[from + blockEnd[blockIdx] - 1], arrayFrom[from + blockStart[blockIdx2]]);
			if (compVal == 0) {
				if (blockIdx < blockIdx2)
					compVal = -1;
				else
					compVal = 1;
			}

			//	「比較の基準のブロックの末尾」が「比較の基準の次のブロックの先頭」より大きければ高速化ロジックを抜ける
			if (compVal > 0) {
				break;
			}
			//	比較の基準のブロックは後続のすべてのブロックより小さいのでブロックごと転送してしまう。
			int rangeBlock = blockEnd[blockIdx] - blockStart[blockIdx];
			System.arraycopy(arrayFrom, from + blockStart[blockIdx], arrayTo, destIdx + idx, rangeBlock);
			idx += rangeBlock;
		}
		//	最後のブロックのみ残った場合は、比較のブロックがないのでこのブロックも転送する。
		if (blockTransIdx == blockCount - 1) {
			int blockIdx = fromOrderBlocks[blockTransIdx];
			int rangeBlock = blockEnd[blockIdx] - blockStart[blockIdx];
			System.arraycopy(arrayFrom, from + blockStart[blockIdx], arrayTo, destIdx + idx, rangeBlock);
			idx += rangeBlock;
			blockTransIdx++;
		}
		//	ブロック転送したブロックを fromOrderBlocks の配列から除去
		for (int fromBlockIdx = blockTransIdx; fromBlockIdx < blockCount; fromBlockIdx++) {
			fromOrderBlocks[fromBlockIdx - blockTransIdx] = fromOrderBlocks[fromBlockIdx];
		}
		blockCount -= blockTransIdx;

		//	全ブロックをマージ
		//	各ブロックをキューのように扱っていると考えると理解しやすいかも
		for (/*idx = 0*/; idx < range; idx++) {
			//	最小の値を持つブロック（fromOrderBlocks[0]）の先頭要素を取り出し、ソート対象へ先頭から詰めていく
			int blockIdx = fromOrderBlocks[0];
			int valueIdx = from + blockStart[blockIdx];
			T value = arrayFrom[valueIdx];
			arrayTo[destIdx + idx] = value;
			//	取り出したブロックの先頭位置をインクリメント
			blockStart[blockIdx]++;

			if (blockStart[fromOrderBlocks[0]] >= blockEnd[fromOrderBlocks[0]]) {
				//	ブロックのすべての値がなくなったら、fromOrderBlocksの配列からブロック№を取り除く
				for (int i = 1; i < blockCount; i++)
					fromOrderBlocks[i - 1] = fromOrderBlocks[i];
				blockCount--;
			}
			else {
				//	fromOrderBlocks[0] のブロックは先頭の値を取り出したので次の先頭は大きな値になる。二分検索でfromOrderBlocksのどの位置にすればよいか決定し位置を修正する
				//	（部分的な二分探索を使った挿入ソート…先頭ブロックの最小値が変わったのでそこだけ適切な順序に並べ替え）
				blockStartItemCache[blockIdx] = arrayFrom[valueIdx + 1];
				int fromBlockIdx = fromOrderBlocks[0];			//	fromOrderBlocks[0]の最小値は取り出したので新しい最小値が他のブロックの最小値と比較して何番目か調べる
				//T key = works[blockStart[fromBlockIdx]];
				T key = blockStartItemCache[fromBlockIdx];
				int fromIdx = 1;									//	探索範囲の最小インデックス
				int toIdx = blockCount;						//	探索範囲の最大インデックス
				int curIdx = (fromIdx + toIdx) / 2;				//	探索範囲の中央インデックス
				//	二分探索処理
				while (fromIdx < toIdx) {
					int curBlockIdx = fromOrderBlocks[curIdx];
					//int compVal = comparator.compare(key, works[blockStart[curBlockIdx]]);
					int compVal = comparator.compare(key, blockStartItemCache[curBlockIdx]);
					if (compVal == 0) {							//	キー値が同じであるなら、ブロック番号の小さい方を先にする。（安定のため）
						if (fromBlockIdx < curBlockIdx)
							compVal = -1;
						else
							compVal = 1;
					}
					if (compVal < 0) {
						toIdx = curIdx;
						curIdx = (fromIdx + toIdx) / 2;
					} else {
						fromIdx = curIdx + 1;
					}
					curIdx = (fromIdx + toIdx) / 2;
				}
				//	挿入処理
				for (int j = 1; j < curIdx; j++) {
					fromOrderBlocks[j - 1] = fromOrderBlocks[j];
				}
				fromOrderBlocks[curIdx - 1] = fromBlockIdx;
			}
		}
	}


	/**
	 * MasSort
	 *
	 * Faster version of the merge sort.
	 * 高速化版マージソート
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 */
	public static <T> void masSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		final int range = to - from;
		@SuppressWarnings("unchecked")
		final T[] works = (T[])new Object[range];
		System.arraycopy(array, from, works, 0, range);
		masSort(works, array, 0, range, from, comparator);
	}


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
			final int comp = comparator.compare(array[idx], key);
			if (comp < 0) {
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
		int curIdx = (fromIdx + toIdx) / 2;
		while (fromIdx < toIdx) {
			final int compVal = comparator.compare(key, array[curIdx]);
			if (compVal <= 0) {
				toIdx = curIdx;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			} else {
				fromIdx = curIdx + 1;
				curIdx = (fromIdx + toIdx) / 2;
				continue;
			}
		}
		return curIdx;
	}


	/**
	 * MatSort
	 *
	 * Saving memory version merge sort (saving memory version MasSort)
	 * WorkSize can specify the less value the number of elements to be sorted as the maximum value.
	 * Performance If the workSize to the same value as the number of elements to be sorted is maximized, it is substantially MasSort.
	 * When workSize a small value the performance becomes lower, if a 1, it is substantially insert sort.
	 * Since workSize If too small obviously affect the performance, rather than a fixed value, is better to specify the ratio of the number of elements, such as 1 / N it seems to be good.
	 * The logical workSize ought larger it is to improve performance, but was also seen 1/2 to fast become the case than normal MasSort be about 1/10.	 *
	 *
	 * 省メモリ版マージソート(省メモリ版 MasSort)
	 * workSizeはソート対象の要素数を最大値としてそれ以下の値を指定できる。
	 * workSizeをソート対象の要素数と同じ値にするならパフォーマンスは最大となり、実質的に MasSort となる。
	 * workSizeを小さな値にするとパフォーマンスはより低くなり、1であるなら、実質的にインサートソートとなる。
	 * workSizeは小さすぎる場合は明らかにパフォーマンスに影響するので、固定値ではなく、1/N といったような要素数との比で指定する方がよいと思われる。
	 * 論理的にはworkSizeはより大きな方がパフォーマンスが向上するはずだが、1/2～1/10程度でも通常の MasSort より速くなるケースも見受けられた。
	 *
	 * @param array sort target / ソート対象
	 * @param from index of first element / ソート対象の開始位置
	 * @param to index of last element (exclusive) / ソート対象の終了位置 + 1
	 * @param comparator comparator of array element / 比較器
	 * @param workSize work area size / 作業領域サイズ
	 */
	public static <T> void matSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator, int workSize)
	{
		int range = (to - from);
		if (range <= 1)
			return ;
		//	作業領域サイズの決定
		if (workSize > range)
			workSize = range;					//	作業領域サイズがソート範囲のサイズより大きい場合、ソート範囲のサイズにする。
		else if (workSize == range)
			;									//	作業領域サイズがソート範囲と等しい場合、masSort が１回呼ばれるだけになる。
		else if (workSize > (range + 1) / 2)
			workSize = (range + 1) / 2;			//	作業領域サイズがソート範囲の半分より大きい場合、ソート範囲の半分にする。
		else if (workSize < 1)
			workSize = 1;
		@SuppressWarnings("unchecked")
		final T[] works = (T[])new Object[workSize];

		//	最終ブロックをソート
		int fromIdx = to - workSize;
		System.arraycopy(array, fromIdx, works, 0, workSize);
		masSort(works, array, 0, workSize, fromIdx, comparator);

		//	最終ブロックの一つ手前のブロックをソートしてマージ…をすべてのブロックをマージするまで繰り返す。
		while (fromIdx > from) {
			//	ひとつ前のブロックをソート (fromIdx ～ midIdx)
			final int midIdx = fromIdx;
			fromIdx = fromIdx - workSize;
			if (fromIdx < from)
				fromIdx = from;
			System.arraycopy(array, fromIdx, works, 0, midIdx - fromIdx);
			//masSort(works, array, 0, midIdx - fromIdx, fromIdx, comparator);
			masSort(array, works, fromIdx, midIdx, 0, comparator);

			int idx1 = fromIdx;
			int idx2 = midIdx;

			if (comparator.compare(works[midIdx - fromIdx - 1], array[midIdx]) < 0) {
//				範囲１の値はすべて範囲２の値より小さかった
				System.arraycopy(works, 0, array, fromIdx, midIdx - fromIdx);
				continue;
			}

			//	範囲１をワーク配列にコピー
			//if (midIdx - idx1 > 0) {
			//	System.arraycopy(array, idx1, works, 0, midIdx - idx1);
			//}

			//	最後のブロックとその１つ手前のブロックをマージして新しい（大きな）ブロックを作る。これをブロックが１つになるまで繰り返す。
			int idx = idx1;	//	現在処理中の位置を設定
			while (idx1 < midIdx && idx2 < to)  {
				final T value1 = works[idx1 - fromIdx];
//				T value2 = array[idx2];

				final int toIdx = searchFowardingBinSearch(value1, array, idx2, to, comparator);
				if (toIdx != idx2) {
					//	後方のブロックから値をいくつか取り出して、ソート済み領域に格納（後ろのブロックが大きいため、後ろのブロックから連続して値が採用される可能性が高い）
					System.arraycopy(array, idx2, array, idx, toIdx - idx2);
					idx += (toIdx - idx2);
					idx2 = toIdx;
				}
				if (toIdx < to) {
					//	前方のブロックから値を１つ取り出して、ソート済み領域に格納
					array[idx] = value1;
					idx1++;
					idx++;
				}
			}

			//	残ったワーク領域をソート対象へ詰める
			while (idx1 < midIdx)  {
				array[idx] = works[idx1 - fromIdx];
				idx++;
				idx1++;
			}
		}
	}
}
