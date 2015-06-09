/*
 * MasSort
 * Faster version of the merge sort.
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;

public class MasSort implements ISortAlgorithm {
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
	 *
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
		if (range < 200) {
			BinInsertionSort.binInsertionSort(arrayFrom, from, to, comparator);
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
			int fromIdx = 0;									//	探索範囲の最小インデックス
			int toIdx = fromOrderBlocksIdx;						//	探索範囲の最大インデックス + 1
			int curIdx = fromIdx + (toIdx - fromIdx) / 2;		//	探索範囲の中央インデックス
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
				/* virtual code
					if (key < blockStartItemCache[curBlockIdx]) then
					begin
						toIdx = curIdx;
					end
					else if (blockStartItemCache[curBlockIdx] < key) then
					begin
						fromIdx = curIdx + 1;
					end
					else
					begin
						// case of key == blockStartItemCache[curBlockIdx]
						if (blockIdx < curBlockIdx)
							toIdx = curIdx
						else
							fromIdx = curIdx + 1;
					end;
				 */
				curIdx = fromIdx + (toIdx - fromIdx) / 2;
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
			/* virtual code
				if (arrayFrom[from + blockEnd[blockIdx] - 1] < arrayFrom[from + blockStart[blockIdx2]]) then
				begin
					toIdx = curIdx;
				end
				else if (arrayFrom[from + blockStart[blockIdx2]] < arrayFrom[from + blockEnd[blockIdx] - 1]) then
				begin
					fromIdx = curIdx + 1;
				end
				else
				begin
					// case of arrayFrom[from + blockStart[blockIdx2]] == arrayFrom[from + blockEnd[blockIdx] - 1]
					if (blockIdx < blockIdx2) then
						toIdx = curIdx
					else
						fromIdx = curIdx + 1;
				end;
			*/

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
				int toIdx = blockCount;								//	探索範囲の最大インデックス
				int curIdx = fromIdx + (toIdx - fromIdx) / 2;		//	探索範囲の中央インデックス
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
						curIdx = fromIdx + (toIdx - fromIdx) / 2;
					} else {
						fromIdx = curIdx + 1;
					}
					/* virtual code
						if (key < blockStartItemCache[curBlockIdx]) then
						begin
							toIdx = curIdx;
							curIdx = fromIdx + (toIdx - fromIdx) / 2;
						end
						else if (blockStartItemCache[curBlockIdx] < key) then
						begin
							fromIdx = curIdx + 1;
						end
						else
						begin
							// case of key == blockStartItemCache[curBlockIdx]
							if (blockIdx < blockIdx2) then
							begin
								toIdx = curIdx;
								curIdx = fromIdx + (toIdx - fromIdx) / 2;
							end
							else
								fromIdx = curIdx + 1;
						end;
					*/
					curIdx = fromIdx + (toIdx - fromIdx) / 2;
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
	 *
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
		return "MasSort";
	}
}
