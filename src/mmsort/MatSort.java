/*
 * MatSort
 * Saving memory version merge sort (saving memory version MasSort)
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class MatSort implements ISortAlgorithm {
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
			if (comparator.compare(array[idx], key) < 0) {
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
		int curIdx = fromIdx + (toIdx - fromIdx) / 2;
		while (fromIdx < toIdx) {
			if (comparator.compare(key, array[curIdx]) <= 0) {	// virtual code : (array[curIdx] < key) == false
				toIdx = curIdx;
				curIdx = fromIdx + (toIdx - fromIdx) / 2;
				continue;
			} else {
				fromIdx = curIdx + 1;
				curIdx = fromIdx + (toIdx - fromIdx) / 2;
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
		if (workSize == 0)
			workSize = (range + 9) / 10;	// round up 切り上げ
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
		MasSort.masSort(works, array, 0, workSize, fromIdx, comparator);

		//	最終ブロックの一つ手前のブロックをソートしてマージ…をすべてのブロックをマージするまで繰り返す。
		while (fromIdx > from) {
			//	ひとつ前のブロックをソート (fromIdx ～ midIdx)
			final int midIdx = fromIdx;
			fromIdx = fromIdx - workSize;
			if (fromIdx < from)
				fromIdx = from;
			System.arraycopy(array, fromIdx, works, 0, midIdx - fromIdx);
			//masSort(works, array, 0, midIdx - fromIdx, fromIdx, comparator);
			MasSort.masSort(array, works, fromIdx, midIdx, 0, comparator);

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

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		matSort(array, from, to, comparator, 0);
	}

	public boolean isStable()
	{
		return true;
	}

	public String getName()
	{
		return "MatSort";
	}
}
