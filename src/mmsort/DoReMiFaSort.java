package mmsort;

import java.util.Comparator;

public class DoReMiFaSort implements ISortAlgorithm {

// C++ language Original DoReMiFaSort Source Code.
// 	http://doremifasort.web.fc2.com/DoReMiFaSort.txt
/*

 static void __stdcall DualSort(SortRecStruct* p1, SortRecStruct* p2)
{
	if( p1->Key > p2->Key )
		SwapUU( p1, p2 );
}

static void __stdcall TriSort(SortRecStruct* p1, SortRecStruct* p2, SortRecStruct* p3)
{
	DualSort( p1, p2 );
	DualSort( p2, p3 );
	DualSort( p1, p2 );
}

static void __stdcall InsertionP(SortRecStruct* pTop, SortRecStruct* pBtm)
{
	SortRecStruct	TempData;
	SortRecStruct	*ScanPtr, *CopyPtr;

	ScanPtr = pBtm - 1;
	while( ScanPtr >= pTop )
	{
		if( ( ScanPtr + 1 )->Key < ScanPtr->Key )
		{
			CopyRec( &TempData, ScanPtr ); CopyPtr = ScanPtr;
			do
			{
				CopyRec( CopyPtr, CopyPtr + 1 ); CopyPtr++;
			}
			while( ( CopyPtr < pBtm ) && ( ( CopyPtr + 1 )->Key < TempData.Key ) );
			CopyRec( CopyPtr, &TempData );
		}
		ScanPtr--;
	}
}

#ifdef	_M_X64
 static DWORD __stdcall DoReMiFaSortP(SortRecStruct* pTop, SortRecStruct* pBtm, bool* pAbort)
{
	SortRecStruct	*nPtrL, *nPtrR, *nPivotL, *nPivotR, *nTemp;
	SortRecStruct	*RecRe, *RecMe, *RecFa;
	SortRecStruct	Pivot;

	if( pBtm > pTop )
	{
		if( ( pBtm - pTop ) > 48 )	// 48 is best much value at win32 and x64
		{
			// Do-Re-Mi-Fa-Sort
			RecMe = ( pBtm - pTop ) / 2 + pTop; RecRe = ( RecMe - pTop ) / 2 + pTop; RecFa = ( pBtm - RecMe ) / 2 + RecMe;
			TriSort( pTop, RecRe, RecMe ); TriSort( RecRe, RecMe, RecFa ); TriSort( RecMe, RecFa, pBtm );
			DualSort( pTop, RecRe ); DualSort( RecRe, RecMe );
			SwapUU( RecMe, pBtm );
			//Pivot = *pBtm;
			CopyRec( &Pivot, pBtm );
			// adjust pivot range
			nPivotR = pBtm;
			while( ( nPivotR > pTop ) && ( nPivotR[ -1 ].Key == Pivot.Key ) )
			{
				nPivotR--;
			}
			if( nPivotR == pTop ) return 0;	// All records same data
			nPtrR = nPivotR; nPivotL = pTop - 1; nPtrL = pTop - 1;
			for( ; ; )
			{
				while( ( ++nPtrL )->Key < Pivot.Key );
				while( Pivot.Key < ( --nPtrR )->Key )
					if( nPtrR == pTop ) break;
				if( nPtrL >= nPtrR ) break;
				SwapUU( nPtrL, nPtrR );
				if( nPtrL->Key == Pivot.Key )
				{
					nPivotL++; SwapUU( nPivotL, nPtrL );
				}
				if( Pivot.Key == nPtrR->Key )
				{
					nPivotR--; SwapUU( nPtrR, nPivotR );
				}
			}
			SwapUU( nPtrL, pBtm ); nPtrR = nPtrL - 1; nPtrL = nPtrL + 1;
			for( nTemp = pTop; nTemp <= nPivotL; nTemp++, nPtrR-- )
			{
				SwapUU( nTemp, nPtrR );
			}
			for( nTemp = pBtm - 1; nTemp >= nPivotR; nTemp--, nPtrL++ )
			{
				SwapUU( nPtrL, nTemp );
			}
			if( *pAbort ) return 1223;
			if( nPtrR > pTop )
				DoReMiFaSortP( pTop, nPtrR, pAbort );
			if( nPtrL < pBtm )
				DoReMiFaSortP( nPtrL, pBtm, pAbort );
		}
		else
		{
			InsertionP( pTop, pBtm );
		}
	}
	return 0;
}
 */


	public static final <T> void dualSort(T[] array, int p1, int p2, Comparator<T> comparator)
	{
		if( comparator.compare(array[p1], array[p2]) > 0 )
		{
			T work = array[p1];
			array[p1] = array[p2];
			array[p2] = work;
		}
	}


	public static final <T> void triSort(T[] array, int p1, int p2, int p3, Comparator<T> comparator)
	{
		dualSort( array, p1, p2, comparator );
		dualSort( array, p2, p3, comparator );
		dualSort( array, p1, p2, comparator );
	}


	public static final <T> void insertionP(T[] array, int pTop, int pBtm, Comparator<T> comparator)
	{
		T	TempData;
		int	ScanPtr, CopyPtr;

		ScanPtr = pBtm - 1;
		while( ScanPtr >= pTop )
		{
			if( comparator.compare(array[ScanPtr + 1], array[ScanPtr]) < 0 )
			{
				TempData = array[ScanPtr];
				CopyPtr = ScanPtr;
				do
				{
					array[CopyPtr] = array[CopyPtr + 1];
					CopyPtr++;
				}
				while( ( CopyPtr < pBtm ) && ( comparator.compare(array[CopyPtr + 1], TempData) < 0 ) );
				array[CopyPtr] = TempData;
			}
			ScanPtr--;
		}
	}



	public static final <T> void doReMiFaSort(T[] array, int pTop, int pBtm, Comparator<T> comparator)
	{
		int	nPtrL, nPtrR, nPivotL, nPivotR, nTemp;
		int	recRe, recMe, recFa;
		T pivot;
		T work;

		if( pBtm > pTop )
		{
			if( ( pBtm - pTop ) > 48 )	// 48 is best much value at win32 and x64
			{
				// Do-Re-Mi-Fa-Sort
				recMe = ( pBtm - pTop ) / 2 + pTop; recRe = ( recMe - pTop ) / 2 + pTop; recFa = ( pBtm - recMe ) / 2 + recMe;
				triSort( array, pTop, recRe, recMe, comparator ); triSort( array, recRe, recMe, recFa, comparator ); triSort( array, recMe, recFa, pBtm, comparator );
				dualSort( array, pTop, recRe, comparator ); dualSort( array, recRe, recMe, comparator );
				work = array[recMe];
				array[recMe] = array[pBtm];
				array[pBtm] =  work;
				//Pivot = *pBtm;
				pivot = array[pBtm];
				// adjust pivot range
				nPivotR = pBtm;
				while( ( nPivotR > pTop ) && ( comparator.compare(array[nPivotR -1], pivot) == 0 ) )
				{
					nPivotR--;
				}
				if( nPivotR == pTop ) return;	// All records same data
				nPtrR = nPivotR; nPivotL = pTop - 1; nPtrL = pTop - 1;
				for( ; ; )
				{
					while( comparator.compare(array[++nPtrL], pivot) < 0 );
					while( comparator.compare(pivot, array[--nPtrR]) < 0 )
						if( nPtrR == pTop ) break;
					if( nPtrL >= nPtrR ) break;
					work = array[nPtrL];
					array[nPtrL] = array[nPtrR];
					array[nPtrR] = work;
					if( comparator.compare(array[nPtrL], pivot) == 0 )
					{
						nPivotL++;
						work = array[nPivotL];
						array[nPivotL] = array[nPtrL];
						array[nPtrL] = work;
					}
					if( comparator.compare(pivot, array[nPtrR]) == 0 )
					{
						nPivotR--;
						work = array[nPtrR];
						array[nPtrR] = array[nPivotR];
						array[nPivotR] = work;
					}
				}
				work = array[nPtrL];
				array[nPtrL] = array[pBtm];
				array[pBtm] = work;

				nPtrR = nPtrL - 1; nPtrL = nPtrL + 1;
				for( nTemp = pTop; nTemp <= nPivotL; nTemp++, nPtrR-- )
				{
					work = array[nTemp];
					array[nTemp] = array[nPtrR];
					array[nPtrR] = work;
				}
				for( nTemp = pBtm - 1; nTemp >= nPivotR; nTemp--, nPtrL++ )
				{
					work = array[nPtrL];
					array[nPtrL] = array[nTemp];
					array[nTemp] = work;
				}
				if( nPtrR > pTop )
					doReMiFaSort(array, pTop, nPtrR, comparator );
				if( nPtrL < pBtm )
					doReMiFaSort(array, nPtrL, pBtm, comparator );
			}
			else
			{
				insertionP( array, pTop, pBtm, comparator);
			}
		}
		return;
	}

	public <T> void sort(final T[] array, final int from, final int to, final Comparator<? super T> comparator)
	{
		doReMiFaSort(array, from, to - 1, comparator);
	}

	public boolean isStable()
	{
		return false;
	}

	public String getName()
	{
		return "♪Do Re Mi Fa Sort";
	}
}
