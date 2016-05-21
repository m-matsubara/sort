@echo off
rem Copyright (c) 2015 masakazu matsubara
rem Released under the MIT license
rem https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

rem unstable sort
call test.bat mmsort.ManyPivotSort %1
call test.bat mmsort.ManyPivotSort3W %1
call test.bat mmsort.No5Sort %1
call test.bat mmsort.QuickSort %1
call test.bat mmsort.QuickSort3WM5 %1
call test.bat mmsort.QuickSortM3 %1
call test.bat mmsort.QuickSortM5 %1

rem stable sort
call test.bat mmsort.ArraysSort %1
call test.bat mmsort.ImprovedMergeSort %1
call test.bat mmsort.InplaceMergeSort %1
call test.bat mmsort.No6Sort %1
call test.bat mmsort.MasSort %1
call test.bat mmsort.MatSort %1
call test.bat mmsort.MergeSort %1
