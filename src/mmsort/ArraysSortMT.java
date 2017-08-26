/*
 * ArraysSort (Multi Thread)
 *
 * http://www.mmatsubara.com/developer/sort/
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Arrays;
import java.util.Comparator;

public class ArraysSortMT implements ISortAlgorithm {

	@Override
	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		Arrays.parallelSort(array, from, to, comparator);
	}

	@Override
	public boolean isStable()
	{
		//	本当は安定ソートのはずだが、 JDK 1.8.0には不具合があって安定ではない。
		//	Bug ID: JDK-8076446 (array) Arrays.parallelSort is not stable
		//	http://bugs.java.com/bugdatabase/view_bug.do?bug_id=8076446
		return false;
	}

	@Override
	public String getName()
	{
		return "Arrays.parallelSort";
	}

}
