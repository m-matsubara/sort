@echo off
rem Copyright (c) 2015 masakazu matsubara
rem Released under the MIT license
rem https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

set TIMES=10

java.exe  mmsort.SortTest %1 100 R %TIMES%
java.exe  mmsort.SortTest %1 100 A %TIMES%
java.exe  mmsort.SortTest %1 100 D %TIMES%
java.exe  mmsort.SortTest %1 100 F %TIMES%
java.exe  mmsort.SortTest %1 1000 R %TIMES%
java.exe  mmsort.SortTest %1 1000 A %TIMES%
java.exe  mmsort.SortTest %1 1000 D %TIMES%
java.exe  mmsort.SortTest %1 1000 F %TIMES%
java.exe  mmsort.SortTest %1 10000 R %TIMES%
java.exe  mmsort.SortTest %1 10000 A %TIMES%
java.exe  mmsort.SortTest %1 10000 D %TIMES%
java.exe  mmsort.SortTest %1 10000 F %TIMES%
java.exe  mmsort.SortTest %1 100000 R %TIMES%
java.exe  mmsort.SortTest %1 100000 A %TIMES%
java.exe  mmsort.SortTest %1 100000 D %TIMES%
java.exe  mmsort.SortTest %1 100000 F %TIMES%
java.exe  mmsort.SortTest %1 1000000 R %TIMES%
java.exe  mmsort.SortTest %1 1000000 A %TIMES%
java.exe  mmsort.SortTest %1 1000000 D %TIMES%
java.exe  mmsort.SortTest %1 1000000 F %TIMES%
java.exe  mmsort.SortTest %1 10000000 R %TIMES%
java.exe  mmsort.SortTest %1 10000000 A %TIMES%
java.exe  mmsort.SortTest %1 10000000 D %TIMES%
java.exe  mmsort.SortTest %1 10000000 F %TIMES%
java.exe  mmsort.SortTest %1 100000000 R %TIMES%
java.exe  mmsort.SortTest %1 100000000 A %TIMES%
java.exe  mmsort.SortTest %1 100000000 D %TIMES%
java.exe  mmsort.SortTest %1 100000000 F %TIMES%
