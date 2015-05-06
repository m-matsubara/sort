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
	 * 挿入ソート
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void insertSort(final T[] array, final int min, final int max, final Comparator<? super T> comparator)
	{
		final int range = max - min;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			if (comparator.compare(array[min + 1], array[min + 2]) > 0) {
				T work = array[min + 1];
				array[min + 1] = array[min + 2];
				array[min + 2] = work;
			if (comparator.compare(array[min], array[min + 1]) > 0) {
					work = array[min];
					array[min] = array[min + 1];
					array[min + 1] = work;
			}
			}
			return;
		}
		for (int i = min; i < max; i++) {
			final T key = array[i];
			int j = i - 1;
			while (j >= min && comparator.compare(array[j], key) > 0) {
				array[j + 1] = array[j];
				j--;
			}

			array[j + 1] = key;
		}
	}

	/**
	 * 挿入ソート（挿入位置を二分検索によって探索する）
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static <T> void insertBinSort(T[] array, int min, int max, Comparator<? super T> comparator)
	{
		final int range = max - min;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			if (comparator.compare(array[min + 1], array[min + 2]) > 0) {
				T work = array[min + 1];
				array[min + 1] = array[min + 2];
				array[min + 2] = work;
				if (comparator.compare(array[min], array[min + 1]) > 0) {
					work = array[min];
					array[min] = array[min + 1];
					array[min + 1] = work;
				}
			}
			return;
		}
		//	前から既に整列済みの部分を省く・逆順の列が続く場合はその部分を反転
		int startIdx = min + 1;
		if (comparator.compare(array[startIdx - 1], array[startIdx]) <= 0) {
			for (startIdx++; startIdx < max; startIdx++) {
				if (comparator.compare(array[startIdx - 1], array[startIdx]) > 0) {
					break;
				}
			}
		} else {
			for (startIdx++; startIdx < max; startIdx++) {
				if (comparator.compare(array[startIdx - 1], array[startIdx]) <= 0) {
					break;
				}
			}
			int minIdx = min;
			int maxIdx = startIdx - 1;
			while (minIdx < maxIdx) {
				T temp = array[minIdx];
				array[minIdx] = array[maxIdx];
				array[maxIdx] = temp;
				minIdx++;
				maxIdx--;
			}
		}

		for (int i = startIdx; i < max; i++) {
			T key = array[i];
			int minIdx = min;
			int maxIdx = i;
			int curIdx = (minIdx + maxIdx) / 2;
			while (minIdx < maxIdx) {
				final int compVal = comparator.compare(key, array[curIdx]);
				if (compVal < 0) {
					maxIdx = curIdx;
				} else {
					minIdx = curIdx + 1;
				}
				curIdx = (minIdx + maxIdx) / 2;
			}
			for (int j = i - 1; j >= curIdx; j--) {
				array[j + 1] = array[j];
			}
			array[curIdx] = key;
		}
/*
		for (int i = min; i < max - 1; i++) {
			if (comparator.compare(array[i], array[i + 1]) > 0) {
				throw new RuntimeException("aaa");
			}
		}
*/
}


	/**
	 * マージソート
	 * 作業用一時領域はソート対象の範囲サイズ / 2（切り上げ）が必要
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param works 作業用一時領域
	 * @param comparator 比較器
	 */
	public static final <T> void mergeSort(final T[] array, final int min, final int max, final T[] works, final Comparator<? super T> comparator)
	{
		final int range = max - min;

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			if (comparator.compare(array[min + 1], array[min + 2]) > 0) {
				T work = array[min + 1];
				array[min + 1] = array[min + 2];
				array[min + 2] = work;
				if (comparator.compare(array[min], array[min + 1]) > 0) {
					work = array[min];
					array[min] = array[min + 1];
					array[min + 1] = work;
				}
			}
			return;
		} else if (range < 200) {
			insertBinSort(array, min, max, comparator);
			return;
		}

		int mid = (min + max) / 2;	//	中央位置（範囲１と範囲２の境界）
		mergeSort(array, min, mid, works, comparator);	//	範囲１（最小位置～中間位置）のソート
		mergeSort(array, mid, max, works, comparator);	//	範囲２（中間位置～最大位置）のソート

		int idx = min;		//	現在処理中の位置（範囲１と範囲２の小さい方をこの位置へ配置（移動）する）
		int idx1 = min;		//	範囲１の次の値のインデックス
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
			System.arraycopy(array, idx1, works, idx1 - min, mid - idx1);
		}

		//	ワーク領域（範囲１のコピー）と範囲２をマージしてソート対象(array)の先頭から詰めていく
		idx = idx1;	//	現在処理中の位置を設定
		while (idx1 < mid && idx2 < max)  {
			final T value1 = works[idx1 - min];
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
			array[idx] = works[idx1 - min];
			idx++;
			idx1++;
		}

//		if (idx != idx2)
//			throw new RuntimeException("Position error");
	}


	/**
	 * マージソート
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static final <T> void mergeSort(T[] array, int min, int max, Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] works = (T[])new Object[(max - min) / 2];

		mergeSort(array, min, max, works, comparator);
	}


	/**
	 * MasSort
	 * 高速化版マージソート
	 * ソート対象を多くのブロックに分割してマージする。
	 * 通常のマージソートに比べて、メモリ転送回数を減らすことができるため、メモリアクセスの遅い環境で特に有利と考えられる。
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param works 作業用一時領域
	 * @param comparator 比較器
	 */
	public static final <T> void masSort(final T[] array, final int min, final int max, final T[] works, final Comparator<? super T> comparator)
	{
		final int range = max - min;			//	ソート範囲サイズ

		//	ソート対象配列サイズが３以下のときは特別扱い
		if (range <= 1) {
			return;
		} else if (range == 2) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			return;
		} else if (range == 3) {
			if (comparator.compare(array[min], array[min + 1]) > 0) {
				T work = array[min];
				array[min] = array[min + 1];
				array[min + 1] = work;
			}
			if (comparator.compare(array[min + 1], array[min + 2]) > 0) {
				T work = array[min + 1];
				array[min + 1] = array[min + 2];
				array[min + 2] = work;
				if (comparator.compare(array[min], array[min + 1]) > 0) {
					work = array[min];
					array[min] = array[min + 1];
					array[min + 1] = work;
				}
			}
			return;
		} else if (range < 200) {
			insertBinSort(array, min, max, comparator);
			return;
		}

		int blockCount = 32;						//	通常のマージソートでは２つのブロックに分けるが、masSortではより多くのブロックに分けてマージする。
		int[] blockStart = new int[blockCount];		//	各ブロックの先頭位置（arrayへの添え字）を保持
		int[] blockEnd = new int[blockCount];		//	各ブロックの最終位置 + 1（arrayへの添え字）を保持
		int[] minOrderBlocks = new int[blockCount];	//	各ブロックの先頭要素を比較し、小さい値を持つブロック№を保持(0 ～ blockCount-1)
		@SuppressWarnings("unchecked")
		T[]   blockStartItemCache = (T[])new Object[blockCount];	//	各ブロックの先頭要素をキャッシュ(CPUのキャッシュヒット率が上がるかなと思って)

		//	ソート対象をブロックに分割・ブロックごとにソート
		for (int blockIdx = 0; blockIdx < blockCount; blockIdx++) {
			blockStart[blockIdx] = (int)((long)range * blockIdx / blockCount);
			blockEnd[blockIdx] = (int)((long)range * (blockIdx + 1) / blockCount);
			masSort(array, min + blockStart[blockIdx], min + blockEnd[blockIdx], works, comparator);
			minOrderBlocks[blockIdx] = blockIdx;
		}

		//	ワーク領域へコピー
		System.arraycopy(array, min, works, 0, range);	//	正確には最後のブロックはworkにコピーしなくてもよいのだが、かえって制御が複雑になるので全部コピーする。

		//	各ブロックの先頭要素をキャッシュ(CPUのキャッシュヒット率が上がるかなと思って)
		for (int blockIdx = 0; blockIdx < blockCount; blockIdx++) {
			blockStartItemCache[blockIdx] = works[blockStart[blockIdx]];
		}

		//	各ブロックの先頭で比較して、小さい値を持つブロック順にブロックをソート → minOrderBlocks
		//	（二分探索を使った挿入ソート）
		for (int minOrderBlocksIdx = 1; minOrderBlocksIdx < blockCount; minOrderBlocksIdx++) {
			int blockIdx = minOrderBlocks[minOrderBlocksIdx];	//	ブロックインデックス（このブロックの最小値は他のブロックの最小値と比較して何番目か調べる）
			//T key = works[blockStart[blockIdx]];
			T key = blockStartItemCache[blockIdx];				//	キー値
			int minIdx = 0;										//	探索範囲の最小インデックス
			int maxIdx = minOrderBlocksIdx;						//	探索範囲の最大インデックス + 1
			int curIdx = (minIdx + maxIdx) / 2;					//	探索範囲の中央インデックス
			//	二分探索処理
			while (minIdx < maxIdx) {
				int curBlockIdx = minOrderBlocks[curIdx];
				//int compVal = comparator.compare(key, works[blockStart[curBlockIdx]]);
				int compVal = comparator.compare(key, blockStartItemCache[curBlockIdx]);
				if (compVal == 0) {		//	キー値が同じであるなら、ブロック番号の小さい方を先にする。（安定のため）
					if (blockIdx < curBlockIdx)
						compVal = -1;
					else
						compVal = 1;
				}
				if (compVal < 0) {
					maxIdx = curIdx;
				} else {
					minIdx = curIdx + 1;
				}
				curIdx = (minIdx + maxIdx) / 2;
			}
			//	挿入処理
			for (int j = minOrderBlocksIdx - 1; j >= curIdx; j--) {
				minOrderBlocks[j + 1] = minOrderBlocks[j];
			}
			minOrderBlocks[curIdx] = minOrderBlocksIdx;
		}

		//	先頭要素でソートしたブロックごとにブロックの末尾が次のブロックの先頭より小さければ、ブロック全体を転送することで高速化
		//	(ソート済み・逆順ソート済みで効果)
		int idx = 0;
		int blockTransIdx;
		for (blockTransIdx = 0; blockTransIdx < blockCount - 1; blockTransIdx++) {
			int blockIdx = minOrderBlocks[blockTransIdx];		//	比較の基準のブロック
			int blockIdx2 = minOrderBlocks[blockTransIdx + 1];	//	比較の基準の次のブロック

			//	「比較の基準のブロックの末尾」と「比較の基準の次のブロックの先頭」を比較
			int compVal = comparator.compare(works[blockEnd[blockIdx] - 1], works[blockStart[blockIdx2]]);
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
			System.arraycopy(works, blockStart[blockIdx], array, min + idx, rangeBlock);
			idx += rangeBlock;
		}
		//	最後のブロックのみ残った場合は、比較のブロックがないのでこのブロックも転送する。
		if (blockTransIdx == blockCount - 1) {
			int blockIdx = minOrderBlocks[blockTransIdx];
			int rangeBlock = blockEnd[blockIdx] - blockStart[blockIdx];
			System.arraycopy(works, blockStart[blockIdx], array, min + idx, rangeBlock);
			idx += rangeBlock;
			blockTransIdx++;
		}
		//	ブロック転送したブロックを minOrderBlocks の配列から除去
		for (int minBlockIdx = blockTransIdx; minBlockIdx < blockCount; minBlockIdx++) {
			minOrderBlocks[minBlockIdx - blockTransIdx] = minOrderBlocks[minBlockIdx];
		}
		blockCount -= blockTransIdx;

		//	全ブロックをマージ
		//	各ブロックをキューのように扱っていると考えると理解しやすいかも
		for (/*idx = 0*/; idx < range; idx++) {
			//	最小の値を持つブロック（minOrderBlocks[0]）の先頭要素を取り出し、ソート対象へ先頭から詰めていく
			int blockIdx = minOrderBlocks[0];
			int valueIdx = blockStart[blockIdx];
			T value = works[valueIdx];
			array[min + idx] = value;
			//	取り出したブロックの先頭位置をインクリメント
			blockStart[blockIdx]++;

			if (blockStart[minOrderBlocks[0]] >= blockEnd[minOrderBlocks[0]]) {
				//	ブロックのすべての値がなくなったら、minOrderBlocksの配列からブロック№を取り除く
				for (int i = 1; i < blockCount; i++)
					minOrderBlocks[i - 1] = minOrderBlocks[i];
				blockCount--;
			}
			else {
				//	minOrderBlocks[0] のブロックは先頭の値を取り出したので次の先頭は大きな値になる。二分検索でminOrderBlocksのどの位置にすればよいか決定し位置を修正する
				//	（部分的な二分探索を使った挿入ソート…先頭ブロックの最小値が変わったのでそこだけ適切な順序に並べ替え）
				blockStartItemCache[blockIdx] = works[valueIdx + 1];
				int minBlockIdx = minOrderBlocks[0];			//	minOrderBlocks[0]の最小値は取り出したので新しい最小値が他のブロックの最小値と比較して何番目か調べる
				//T key = works[blockStart[minBlockIdx]];
				T key = blockStartItemCache[minBlockIdx];
				int minIdx = 1;									//	探索範囲の最小インデックス
				int maxIdx = blockCount;						//	探索範囲の最大インデックス
				int curIdx = (minIdx + maxIdx) / 2;				//	探索範囲の中央インデックス
				//	二分探索処理
				while (minIdx < maxIdx) {
					int curBlockIdx = minOrderBlocks[curIdx];
					//int compVal = comparator.compare(key, works[blockStart[curBlockIdx]]);
					int compVal = comparator.compare(key, blockStartItemCache[curBlockIdx]);
					if (compVal == 0) {							//	キー値が同じであるなら、ブロック番号の小さい方を先にする。（安定のため）
						if (minBlockIdx < curBlockIdx)
							compVal = -1;
						else
							compVal = 1;
					}
					if (compVal < 0) {
						maxIdx = curIdx;
						curIdx = (minIdx + maxIdx) / 2;
					} else {
						minIdx = curIdx + 1;
					}
					curIdx = (minIdx + maxIdx) / 2;
				}
				//	挿入処理
				for (int j = 1; j < curIdx; j++) {
					minOrderBlocks[j - 1] = minOrderBlocks[j];
				}
				minOrderBlocks[curIdx - 1] = minBlockIdx;
			}
		}
	}


	/**
	 * MasSort
	 * 高速化版マージソート
	 * @param array 検索対象
	 * @param min 検索対象の添え字の最小値
	 * @param max 検索対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static <T> void masSort(final T[] array, final int min, final int max, final Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] works = (T[])new Object[array.length];
		masSort(array, min, max, works, comparator);
	}


	/**
	 * 前方優先バイナリサーチ
	 * 先頭から、範囲を２倍ずつ拡張しながら、ある程度位置を絞り込む。その後絞り込んだ範囲内でバイナリサーチ
	 * @param key 検索値
	 * @param array 検索対象
	 * @param min 検索対象の添え字の最小値
	 * @param max 検索対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 * @return keyの値と等しいか、より大きい最初の要素の添え字
	 */
	public static final <T> int searchFowardingBinSearch(final T key, final T[] array, int min, int max, Comparator<? super T> comparator)
	{
		//	先頭から1個目, 2個目, 4個目, 8個目…として、対象位置を大まかに絞り込む
		int skipSize = 1;
		int idx = min;
		while (idx < max) {
			final int comp = comparator.compare(array[idx], key);
			if (comp < 0) {
				min = idx + 1;
				idx += skipSize;
				skipSize *= 2;
			} else  {
				max = idx;
				break;
			}
		}

		//	大まかに絞り込んだ範囲内で２分検索する
		int minIdx = min;
		int maxIdx = max;
		int curIdx = (minIdx + maxIdx) / 2;
		while (minIdx < maxIdx) {
			final int compVal = comparator.compare(key, array[curIdx]);
			if (compVal <= 0) {
				maxIdx = curIdx;
				curIdx = (minIdx + maxIdx) / 2;
				continue;
			} else {
				minIdx = curIdx + 1;
				curIdx = (minIdx + maxIdx) / 2;
				continue;
			}
		}
		return curIdx;
	}


	/**
	 * MatSort
	 * 省メモリ版マージソート(省メモリ版 MasSort)
	 * workSizeはソート対象の要素数を最大値としてそれ以下の値を指定できる。
	 * workSizeをソート対象の要素数と同じ値にするならパフォーマンスは最大となり、実質的に MasSort となる。
	 * workSizeを小さな値にするとパフォーマンスはより低くなり、1であるなら、実質的にインサートソートとなる。
	 * workSizeは小さすぎる場合は明らかにパフォーマンスに影響するので、固定値ではなく、1/N といったような要素数との比で指定する方がよいと思われる。
	 * 論理的にはworkSizeはより大きな方がパフォーマンスが向上するはずだが、1/2～1/10程度を指定すると通常の MasSort より速くなるケースも見受けられた。
	 *
	 * @param array ソート対象
	 * @param min ソート対象の添え字の最小値
	 * @param max ソート対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 * @param workSize 要素数で作業領域サイズ
	 */
	public static <T> void matSort(final T[] array, final int min, final int max, final Comparator<? super T> comparator, int workSize)
	{
		int range = (max - min);
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
		int minIdx = max - workSize;
		masSort(array, minIdx, max, works, comparator);

		//	最終ブロックの一つ手前のブロックをソートしてマージ…をすべてのブロックをマージするまで繰り返す。
		while (minIdx > min) {
			//	ひとつ前のブロックをソート (minIdx ～ midIdx)
			final int midIdx = minIdx;
			minIdx = minIdx - workSize;
			if (minIdx < min)
				minIdx = min;
			masSort(array, minIdx, midIdx, works, comparator);

			int idx1 = minIdx;
			int idx2 = midIdx;

			if (comparator.compare(array[midIdx - 1], array[midIdx]) < 0) {
				continue;			//	範囲１の値はすべて範囲２の値より小さかった（再配置なし）
			}

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
			if (midIdx - idx1 > 0) {
				System.arraycopy(array, idx1, works, 0, midIdx - idx1);
			}

			//	最後のブロックとその１つ手前のブロックをマージして新しい（大きな）ブロックを作る。これをブロックが１つになるまで繰り返す。
			int idx = idx1;	//	現在処理中の位置を設定
			while (idx1 < midIdx && idx2 < max)  {
				final T value1 = works[idx1 - minIdx];
//				T value2 = array[idx2];

				final int toIdx = searchFowardingBinSearch(value1, array, idx2, max, comparator);
				if (toIdx != idx2) {
					//	後方のブロックから値をいくつか取り出して、ソート済み領域に格納（後ろのブロックが大きいため、後ろのブロックから連続して値が採用される可能性が高い）
					System.arraycopy(array, idx2, array, idx, toIdx - idx2);
					idx += (toIdx - idx2);
					idx2 = toIdx;
				}
				if (toIdx < max) {
					//	前方のブロックから値を１つ取り出して、ソート済み領域に格納
					array[idx] = value1;
					idx1++;
					idx++;
				}
			}

			//	残ったワーク領域をソート対象へ詰める
			while (idx1 < midIdx)  {
				array[idx] = works[idx1 - minIdx];
				idx++;
				idx1++;
			}
		}
	}
}
