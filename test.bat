@echo off
rem Copyright (c) 2015 masakazu matsubara
rem Released under the MIT license
rem https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

set TIMES=%2
title %1 100 R
java.exe -classpath bin mmsort.SortTest %1 100 R %TIMES%
title %1 100 A
java.exe -classpath bin  mmsort.SortTest %1 100 A %TIMES%
title %1 100 D
java.exe -classpath bin  mmsort.SortTest %1 100 D %TIMES%
title %1 100 F
java.exe -classpath bin  mmsort.SortTest %1 100 F %TIMES%
title %1 1000 R
java.exe -classpath bin  mmsort.SortTest %1 1000 R %TIMES%
title %1 1000 A
java.exe -classpath bin  mmsort.SortTest %1 1000 A %TIMES%
title %1 1000 D
java.exe -classpath bin  mmsort.SortTest %1 1000 D %TIMES%
title %1 1000 F
java.exe -classpath bin  mmsort.SortTest %1 1000 F %TIMES%
title %1 10000 R
java.exe -classpath bin  mmsort.SortTest %1 10000 R %TIMES%
title %1 10000 A
java.exe -classpath bin  mmsort.SortTest %1 10000 A %TIMES%
title %1 10000 D
java.exe -classpath bin  mmsort.SortTest %1 10000 D %TIMES%
title %1 10000 F
java.exe -classpath bin  mmsort.SortTest %1 10000 F %TIMES%
title %1 100000 R
java.exe -classpath bin  mmsort.SortTest %1 100000 R %TIMES%
title %1 100000 A
java.exe -classpath bin  mmsort.SortTest %1 100000 A %TIMES%
title %1 100000 D
java.exe -classpath bin  mmsort.SortTest %1 100000 D %TIMES%
title %1 100000 F
java.exe -classpath bin  mmsort.SortTest %1 100000 F %TIMES%
title %1 1000000 R
java.exe -classpath bin  mmsort.SortTest %1 1000000 R %TIMES%
title %1 1000000 A
java.exe -classpath bin  mmsort.SortTest %1 1000000 A %TIMES%
title %1 1000000 D
java.exe -classpath bin  mmsort.SortTest %1 1000000 D %TIMES%
title %1 1000000 F
java.exe -classpath bin  mmsort.SortTest %1 1000000 F %TIMES%
title %1 10000000 R
java.exe -classpath bin  mmsort.SortTest %1 10000000 R %TIMES%
title %1 10000000 A
java.exe -classpath bin  mmsort.SortTest %1 10000000 A %TIMES%
title %1 10000000 D
java.exe -classpath bin  mmsort.SortTest %1 10000000 D %TIMES%
title %1 10000000 F
java.exe -classpath bin  mmsort.SortTest %1 10000000 F %TIMES%
rem title %1 100000000 R
rem java.exe -classpath bin  mmsort.SortTest %1 100000000 R %TIMES%
rem title %1 100000000 A
rem java.exe -classpath bin  mmsort.SortTest %1 100000000 A %TIMES%
rem title %1 100000000 D
rem java.exe -classpath bin  mmsort.SortTest %1 100000000 D %TIMES%
rem title %1 100000000 F
rem java.exe -classpath bin  mmsort.SortTest %1 100000000 F %TIMES%
