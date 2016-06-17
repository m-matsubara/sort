/*
 * Sort Algorithm Benchmark Program
 * Command line Arguments : <SortClassName> <ArraySize> <ArrayType> <Times>
 *   Example : $ java mmsort.SortTest mmsort.MmsSort 10000000 R 10
 *   ArrayType:
 *     R: Random Data
 *     U: Unique Random Data
 *     H: Half Sorted (Half Random)
 *     A: Ascending Ordered
 *     D: Descending Ordered
 *     F: Flat Data
 *
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 matsubara masakazu
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;
import java.util.Random;

public class SortTest {
	protected static final int ARRAY_TYPE_RANDOM = 0;
	protected static final int ARRAY_TYPE_UNIQUE_RANDOM = 1;
	protected static final int ARRAY_TYPE_HALF_SORTED = 2;
	protected static final int ARRAY_TYPE_ASC = 3;
	protected static final int ARRAY_TYPE_DESC = 4;
	protected static final int ARRAY_TYPE_FLAT = 5;

	protected static long compareCount = 0;									//	比較された回数

	//	比較器
	protected static Comparator<SortItem> comparator = new Comparator<SortItem>() {
		@Override
		public final int compare(SortItem o1, SortItem o2) {
			//	マルチスレッド間の調停をしていないのでマルチスレッドのソートは正確な値にならないが、大まかな数値としては問題ない。
			//	スレッドセーフにするとマルチスレッドの効果が分からなくなるのでやらない。
			SortTest.compareCount++;
			final int i1 = o1.key;
			final int i2 = o2.key;
			return (i1 < i2) ? -1 : (i1 > i2) ? 1 : 0;
		}
	};

	/**
	 * Sort element type
	 * ソート対象配列の要素
	 * @author matsubara
	 *
	 */
	static class SortItem
	{
		public int key;				//	sort key / ソートのキー
		public int orginalOrder;	//	Sort pre-order (for confirmation of stable sort) / ソート前の順序（安定ソートの確認用）
		public SortItem(int key)
		{
			this.key = key;
			this.orginalOrder = 0;
		}
		public String toString() {
			return "(Key=" + key + ":OriginalOrder=" + orginalOrder + ")";
		}
	}


	/**
	 * 配列を昇順の値で初期化する
	 * @param array 対象配列
	 */
	public static void initArray(SortItem[] array, int duplicate)
	{
		for (int i = 0; i < array.length; i++) {
			array[i] = new SortItem(i / duplicate);
		}
	}


	/**
	 * 配列を降順の値で初期化する
	 * @param array 対象配列
	 */
	public static void initReverseArray(SortItem[] array, int duplicate)
	{
		for (int i = 0; i < array.length; i++) {
			array[i] = new SortItem((array.length - i - 1) / duplicate);
		}
	}


	/**
	 * 配列を降順の値で初期化する
	 * @param array 対象配列
	 */
	public static void initFlatArray(SortItem[] array)
	{
		for (int i = 0; i < array.length; i++) {
			array[i] = new SortItem(0);
		}
	}

	/**
	 * 半分ソートされた状態で初期化
	 * @param array 対象配列
	 * @param randSeed 乱数の種
	 * @param array 対象配列
	 */
	public static void initHalfSortedArray(SortItem[] array, long randSeed, int duplicate)
	{
		final int half = array.length / 2;
		for (int i = 0; i < half; i++) {
			array[i] = new SortItem((i * 2) / duplicate);
		}
		for (int i = half; i < array.length; i++) {
			array[i] = new SortItem(((i - half) * 2 + 1) / duplicate);
		}
		final Random rand = new Random(randSeed);
		for (int i = half; i < array.length; i++) {
			final int j = half + rand.nextInt(array.length - half);
			final int work = array[i].key;
			array[i].key = array[j].key;
			array[j].key = work;
		}
	}

	/**
	 * 配列のキー値をシャッフルする
	 * オブジェクトのキー値のみ入れ替え、オブジェクトの入れ替えは行わない。
	 * @param array 対象配列
	 * @param randSeed 乱数の種
	 */
	public static void shuffleArray(SortItem[] array, long randSeed)
	{
		final Random rand = new Random(randSeed);
		for (int i = 0; i < array.length; i++) {
			final int j = rand.nextInt(array.length);
			final int work = array[i].key;
			array[i].key = array[j].key;
			array[j].key = work;
		}
	}


	/**
	 * ソート前順序の値を確定
	 * @param array 対象配列
	 */
	public static void assignOriginalOrderArray(SortItem[] array)
	{
		for (int i = 0; i < array.length; i++) {
			array[i].orginalOrder = i;
		}
	}


	/**
	 * 配列のソート結果を確認する
	 * @param array 対象配列
	 */
	public static void validateArray(SortItem[] array, boolean stable)
	{
		if (stable) {
			//	安定ソート用
			for (int i = 0; i < array.length - 1; i++) {
				if (array[i].key > array[i + 1].key || ((array[i].key == array[i + 1].key) && (array[i].orginalOrder > array[i + 1].orginalOrder)))
					throw new RuntimeException("array validation error. (stable mode) : " + i);
			}
		} else {
			//	非安定ソート用
			for (int i = 0; i < array.length - 1; i++) {
				if (array[i].key > array[i + 1].key)
					throw new RuntimeException("array validation error. (unstable mode) : " + i);
			}
		}

		//	originalOrderの合計値確認（オブジェクトが入れ替えでなく、上書きとかされていないか）
		long sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i].orginalOrder;
		}
		if (sum != ((long)array.length - 1) * array.length / 2)	//	ガウスの方式とかいうやつ
			throw new RuntimeException("array validation error.");
	}


	/**
	 * Sort Algorithm Benchmark Program
	 * Command line Arguments : <SortClassName> <ArraySize> <ArrayType> <Times>
	 *   Example : $ java mmsort.SortTest mmsort.MmsSort 10000000 R 10
	 *   ArrayType:
	 *     R: Random Data
	 *     U: Unique Random Data
	 *     H: Half Sorted (Half Random)
	 *     A: Ascending Ordered
	 *     D: Descending Ordered
	 *     F: Flat Data
	 *
	 * @param args arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//	Arguments : mmsort.MmsSort 10000000 R 10
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		//	ソートのタイプ（アルゴリズム）の決定
		String sortType = args[0];

		Class<?> alg = Class.forName(sortType);
		ISortAlgorithm sorter = (ISortAlgorithm)alg.newInstance();

		//	ソート対象サイズ
		int arraySize = Integer.parseInt(args[1]);

		//	ソート対象種類（ランダム・昇順ソート済み・降順ソート済み・同じ値（キー値）・半分ソート済み）
		int arrayType = 0;
		int duplicate = 10;
		if (args.length >= 3) {
			if (args[2].equals("R")) {	//	Random
				arrayType = ARRAY_TYPE_RANDOM;
			} else if (args[2].equals("U")) {	//	Unique Random
				arrayType = ARRAY_TYPE_UNIQUE_RANDOM;
				duplicate = 1;
			} else if (args[2].equals("H")) {	//	Half sorted
				arrayType = ARRAY_TYPE_HALF_SORTED;
			} else if (args[2].equals("A")) {	//	Ascending ordered
				arrayType = ARRAY_TYPE_ASC;
			} else if (args[2].equals("D")) {	//	Deccending ordered
				arrayType = ARRAY_TYPE_DESC;
			} else if (args[2].equals("F")) {	//	Flat values
				arrayType = ARRAY_TYPE_FLAT;
			}
			else
				throw new Exception("arguments error ");
		}
		//	繰り返し数
		int times = 10;
		if (args.length >= 4) {
			times = Integer.parseInt(args[3]);
		}

		SortItem[] array = new SortItem[arraySize];

		//System.out.println("times	algorithm	array type	array size	time	compare count	stable");
		for (int idx = 1; idx <= times; idx++) {
			//	配列の準備
			String arrayTypeName = "";
			switch (arrayType) {
				case ARRAY_TYPE_RANDOM:
				{
					initArray(array, duplicate);
					// 実行ごとに乱数配列が変わったら比較に宜しくないので、idxを乱数の種とすることで疑似乱数配列を固定化する。
					shuffleArray(array, idx);
					arrayTypeName = "Random";
					break;
				}
				case ARRAY_TYPE_UNIQUE_RANDOM:
				{
					initArray(array, 1);
					// 実行ごとに乱数配列が変わったら比較に宜しくないので、idxを乱数の種とすることで疑似乱数配列を固定化する。
					shuffleArray(array, idx);
					arrayTypeName = "Unique Random";
					break;
				}
				case ARRAY_TYPE_HALF_SORTED:
				{
					initHalfSortedArray(array, idx, duplicate);
					arrayTypeName = "Half sorted";
					break;
				}
				case ARRAY_TYPE_ASC:
				{
					initArray(array, duplicate);
					arrayTypeName = "Ascending ordered";
					break;
				}
				case ARRAY_TYPE_DESC:
				{
					initReverseArray(array, duplicate);
					arrayTypeName = "Descending ordered";
					break;
				}
				case ARRAY_TYPE_FLAT:
				{
					initFlatArray(array);
					arrayTypeName = "Flat";
					break;
				}
			}
			assignOriginalOrderArray(array);	//	元の順序を記憶する（安定ソートの確認用）

			final String sortName = sorter.getName();
			final boolean stable = sorter.isStable();
			final String stableStr = stable ? "stable" : "unstable";
			SortTest.compareCount = 0;

			System.gc();	//	ソート中にGCが（できるだけ）発生しないように
			final long startTime = System.nanoTime();
			sorter.sort(array, 0, array.length, comparator);
			final long endTime = System.nanoTime();

			final long compareCount = SortTest.compareCount;
			System.out.printf("%d	%s	%s	%d	%f	%d	%s\n", idx, sortName, arrayTypeName, arraySize, (endTime - startTime) / 1000000000.0, compareCount, stableStr);
			validateArray(array, stable);
		}
	}

}
