/*
 * ArraysSort
 *
 * Copyright (c) 2015 masakazu matsubara
 * Released under the MIT license
 * https://github.com/m-matsubara/sort/blob/master/LICENSE.txt
 */
package mmsort;

import java.util.Arrays;
import java.util.Comparator;

public class ArraysSort implements ISortAlgorithm {

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		Arrays.sort(array, from, to, comparator);
	}

	public boolean isStable()
	{
		return true;
	}

	public String getName()
	{
		return "Binary Insert Sort";
	}

}
