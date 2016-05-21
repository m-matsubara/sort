/*
 * Benchmark of Sorting algorithm
 * ソートのベンチマークプログラム
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Comparator;
import java.util.Random;

public class SortTest {
	protected static int SAME_NUMBER = 2;
	protected static long compareCount = 0;									//	比較された回数

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
	public static void initArray(SortItem[] array)
	{
		for (int i = 0; i < array.length; i++) {
			array[i].key = i / SAME_NUMBER;
		}
	}


	/**
	 * 配列を降順の値で初期化する
	 * @param array 対象配列
	 */
	public static void initReverseArray(SortItem[] array)
	{
		for (int i = 0; i < array.length; i++) {
			array[i].key = (array.length - i - 1) / SAME_NUMBER;
		}
	}


	/**
	 * 配列を降順の値で初期化する
	 * @param array 対象配列
	 */
	public static void initFlatArray(SortItem[] array)
	{
		for (int i = 0; i < array.length; i++) {
			array[i].key = 0;
		}
	}

	/**
	 * 半分ソートされた状態で初期化
	 * @param array 対象配列
	 * @param randSeed 乱数の種
	 * @param array 対象配列
	 */
	public static void initHalfSortedArray(SortItem[] array, long randSeed)
	{
		int half = array.length / 2;
		for (int i = 0; i < half; i++) {
			array[i].key = (i * 2) / SAME_NUMBER ;
		}
		for (int i = half; i < array.length; i++) {
			array[i].key = ((i - half) * 2 + 1) / SAME_NUMBER ;
		}
		Random rand = new Random(randSeed);
		for (int i = half; i < array.length; i++) {
			int j = half + rand.nextInt(half);
			SortItem work = array[i];
			array[i] = array[j];
			array[j] = work;
		}
	}

	/**
	 * 配列の値をシャッフルする
	 * @param array 対象配列
	 * @param randSeed 乱数の種
	 */
	public static void shuffleArray(SortItem[] array, long randSeed)
	{
		Random rand = new Random(randSeed);
		for (int i = 0; i < array.length; i++) {
			int j = rand.nextInt(array.length);
			SortItem work = array[i];
			array[i] = array[j];
			array[j] = work;
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



	public static void main(String[] args) throws Exception {
		//	Arguments : MatSort(1/10) 10000000 R
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		SortItem[] array;

		long startTime;			//	開始時刻（ミリ秒のカウンタ）
		long endTime;			//	終了時刻（ミリ秒のカウンタ）
		long compareCount;		//	ソート中に比較を行った回数

		//	比較器
		Comparator<SortItem> comparator = new Comparator<SortItem>() {
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

		//	ソートのタイプ（アルゴリズム）の決定
		String sortType = args[0];

		Class<?> alg = Class.forName(sortType);
		ISortAlgorithm sorter = (ISortAlgorithm)alg.newInstance();

		//	ソート対象サイズ
		int arraySize = Integer.parseInt(args[1]);

		//	ソート対象種類（ランダム・昇順ソート済み・降順ソート済み・同じ値（キー値）・半分ソート済み）
		int arrayType = 0;
		if (args.length >= 3) {
			if (args[2].equals("R"))	//	Random
				arrayType = 0;
			else if (args[2].equals("A"))	//	Ascending ordered
				arrayType = 1;
			else if (args[2].equals("D"))	//	Deccending ordered
				arrayType = 2;
			else if (args[2].equals("F"))	//	Flat values
				arrayType = 3;
			else if (args[2].equals("H"))	//	Half sorted
				arrayType = 4;
			else
				throw new Exception("arguments error ");
		}
		//	繰り返し数
		int times = 10;
		if (args.length >= 4) {
			times = Integer.parseInt(args[3]);
		}

		//	ソート対象配列の初期化
		array = new SortItem[arraySize];
		for (int i = 0; i < array.length; i++) {
			array[i] = new SortItem(i / SAME_NUMBER);
		}
		//	配列の要素がきれいに並んでいる場合と、ランダムな位置のオブジェクトを指しているのではアクセス速度に差が出る。
		//	テストのケースによっては初回と２回目以降に差が出るため、最初にすべてシャッフルすることでアクセス速度を均一にする。
		shuffleArray(array, 0);

		//System.out.println("times	algorithm	array type	array size	time	compare count	stable");
		for (int idx = 1; idx <= times; idx++) {
			//	配列の準備
			String arrayTypeName = "";
			switch (arrayType) {
				case 0:
				{
					shuffleArray(array, idx);	//	実行ごとに乱数配列が変わったら比較に宜しくないので、idxを乱数の種とすることで疑似乱数配列を固定化する。
					arrayTypeName = "Random";
					break;
				}
				case 1:
				{
					initArray(array);
					arrayTypeName = "Ascending ordered";
					break;
				}
				case 2:
				{
					initReverseArray(array);
					arrayTypeName = "Descending ordered";
					break;
				}
				case 3:
				{
					initFlatArray(array);
					arrayTypeName = "Flat";
					break;
				}
				case 4:
				{
					initHalfSortedArray(array, idx);
					arrayTypeName = "Half sorted";
					break;
				}
			}
			assignOriginalOrderArray(array);	//	元の順序を記憶する（安定ソートの確認用）

			String sortName = sorter.getName();
			boolean stable = sorter.isStable();
			String stableStr = stable ? "stable" : "unstable";
			SortTest.compareCount = 0;

			System.gc();	//	ソート中にGCが（できるだけ）発生しないように
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			startTime = System.currentTimeMillis();
			sorter.sort(array, 0, array.length, comparator);
			endTime = System.currentTimeMillis();

			compareCount = SortTest.compareCount;
			System.out.printf("%d	%s	%s	%d	%f	%d	%s\n", idx, sortName, arrayTypeName, arraySize, (endTime - startTime) / 1000.0, compareCount, stableStr);
			validateArray(array, stable);
		}
	}

}
