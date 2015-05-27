@echo off
rem Copyright (c) 2015 masakazu matsubara
rem Released under the MIT license
rem https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

call test.bat mmsort.ManyPivotSort %1
call test.bat mmsort.ManyPivotSort3W %1
if exist mmsort\DoReMiFaSort.class call test.bat mmsort.DoReMiFaSort %1
rem if exist mmsort\RichardSort.class call test.bat mmsort.RichardSort %1
call test.bat mmsort.QuickSort %1
call test.bat mmsort.QuickSortM3 %1

call test.bat mmsort.MatSort %1
call test.bat mmsort.MasSort %1
call test.bat mmsort.ArraysSort %1
call test.bat mmsort.MergeSort %1
call test.bat mmsort.InplaceMergeSort %1
call test.bat mmsort.ImprovedMergeSort %1

