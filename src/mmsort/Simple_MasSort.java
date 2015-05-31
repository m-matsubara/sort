/*
 * MasSort (Simple implementation for education)
 * Faster version of the merge sort.
 *
 * MasSort (Simple implementation for education)
 * 高速化版マージソート (学習用のシンプルな実装版)
 *
 * ご注意
 *   このコードは学習用に実装されたシンプルな実装版です。
 *   実用コードは MasSort クラスを利用してください。
 *
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class Simple_MasSort implements ISortAlgorithm {
	/**
	 * MasSort (Simple implementation for education)
	 * 高速化版マージソート (学習用のシンプルな実装版)
	 *
	 * ソート対象を多くのブロックに分割してマージする。
	 * 通常のマージソートに比べて、メモリ転送回数を減らすことができるため、メモリアクセスの遅い環境で特に有利と考えられる。
	 * @param array ソート対象
	 * @param from ソート対象の添え字の最小値
	 * @param to ソート対象の添え字の最大値 + 1
	 * @param works 作業用一時領域
	 * @param comparator 比較器
	 */
	public static final <T> void masSort(final T[] array, final int from, final int to, final T[] works, final Comparator<? super T> comparator)
	{
		final int range = to - from;			//	ソート範囲サイズ

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
			BinInsertionSort.binInsertionSort(array, from, to, comparator);
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
			masSort(array, from + blockStart[blockIdx], from + blockEnd[blockIdx], works, comparator);
			minOrderBlocks[blockIdx] = blockIdx;
		}

		//	ワーク領域へコピー
		System.arraycopy(array, from, works, 0, range);	//	正確には最後のブロックはworkにコピーしなくてもよいのだが、かえって制御が複雑になるので全部コピーする。

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
			int fromIdx = 0;									//	探索範囲の最小インデックス
			int toIdx = minOrderBlocksIdx;						//	探索範囲の最大インデックス + 1
			int curIdx = (fromIdx + toIdx) / 2;					//	探索範囲の中央インデックス
			//	二分探索処理
			while (fromIdx < toIdx) {
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
					toIdx = curIdx;
				} else {
					fromIdx = curIdx + 1;
				}
				curIdx = (fromIdx + toIdx) / 2;
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
			System.arraycopy(works, blockStart[blockIdx], array, from + idx, rangeBlock);
			idx += rangeBlock;
		}
		//	最後のブロックのみ残った場合は、比較のブロックがないのでこのブロックも転送する。
		if (blockTransIdx == blockCount - 1) {
			int blockIdx = minOrderBlocks[blockTransIdx];
			int rangeBlock = blockEnd[blockIdx] - blockStart[blockIdx];
			System.arraycopy(works, blockStart[blockIdx], array, from + idx, rangeBlock);
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
			array[from + idx] = value;
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
				int fromIdx = 1;									//	探索範囲の最小インデックス
				int toIdx = blockCount;						//	探索範囲の最大インデックス
				int curIdx = (fromIdx + toIdx) / 2;				//	探索範囲の中央インデックス
				//	二分探索処理
				while (fromIdx < toIdx) {
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
						toIdx = curIdx;
						curIdx = (fromIdx + toIdx) / 2;
					} else {
						fromIdx = curIdx + 1;
					}
					curIdx = (fromIdx + toIdx) / 2;
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
	 * MasSort (Simple implementation for education)
	 * 高速化版マージソート（勉強用のシンプルな実装版）
	 * @param array 検索対象
	 * @param from 検索対象の添え字の最小値
	 * @param to 検索対象の添え字の最大値 + 1
	 * @param comparator 比較器
	 */
	public static <T> void masSort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		@SuppressWarnings("unchecked")
		final T[] works = (T[])new Object[to - from];
		masSort(array, from, to, works, comparator);
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
		return "MasSort (Simple implementation)";
	}
}
