package mmsort;

import java.util.Comparator;

/**
 * Quick Sort (Richard Newcombe edition)
 *
 * http://www.codeguru.com/vb/gen/vb_misc/algorithms/article.php/c14627/Sorting-Algorithms-In-VB.htm#page-5
 * (Listing 9)
 *
 * @author matsubara
 *
 */
public class RichardSort implements ISortAlgorithm {
/* Original Richard Newcombe Source code
Sub QuickSort(Low, Up)
While Up > Low
    Loop1 = Low
    Loop2 = Up
    Part = List(Low)
    While Loop1 < Loop2
        While List(Loop2) > Part
            Loop2 = Loop2 - 1
        Wend
        List(Loop1) = List(Loop2)
        While (Loop1 < Loop2) And List(Loop1) <= Part
            Loop1 = Loop1 + 1
        Wend
        List(Loop2) = List(Loop1)
    Wend
    List(Loop1) = Part
    QuickSort Low, Loop1 - 1
    Low = Loop1 + 1
Wend
 */


	public static final <T> void richardSort(T[] array, int low, int up, Comparator<T> comparator)
	{
		int loop1;
		int loop2;
		while (up > low) {
			loop1 = low;
			loop2 = up;
			T Part = array[low];
			while (loop1 < loop2) {
				while (comparator.compare(array[loop2], Part) > 0) {
					loop2 = loop2 - 1;
				}
				array[loop1] = array[loop2];
				while ((loop1 < loop2) && (comparator.compare(array[loop1], Part) <= 0)) {
					loop1 = loop1 + 1;
				}
				array[loop2] = array[loop1];
			}
			array[loop1] = Part;
			richardSort(array, low, loop1 - 1, comparator);
			low = loop1 + 1;
		}
	}

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		richardSort(array, from, to - 1, comparator);
	}

	public boolean isStable()
	{
		return false;
	}

	public String getName()
	{
		return "Quick Sort (Rechard Newcomb edition)";
	}
}
