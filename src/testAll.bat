@echo off
rem Copyright (c) 2015 masakazu matsubara
rem Released under the MIT license
rem https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

call test.bat MatSort(1/10)
call test.bat MasSort
call test.bat QuickSort
call test.bat QuickSort(Median3)
call test.bat ManyPivotSort
call test.bat Arrays.sort
call test.bat MergeSort
