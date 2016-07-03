@echo off
rem Copyright (c) 2015 matsubara masakazu
rem Released under the MIT license
rem https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

set TIMES=%2
title %1 100 R
java.exe -server -classpath bin mmsort.SortTest %1 100 R I %TIMES%
title %1 100 R (Str)
java.exe -server -classpath bin mmsort.SortTest %1 100 R S %TIMES%
title %1 100 U
java.exe -server -classpath bin mmsort.SortTest %1 100 U I %TIMES%
title %1 100 H
java.exe -server -classpath bin mmsort.SortTest %1 100 H I %TIMES%
title %1 100 A
java.exe -server -classpath bin  mmsort.SortTest %1 100 A I %TIMES%
title %1 100 D
java.exe -server -classpath bin  mmsort.SortTest %1 100 D I %TIMES%
title %1 100 F
java.exe -server -classpath bin  mmsort.SortTest %1 100 F I %TIMES%

title %1 1000 R
java.exe -server -classpath bin  mmsort.SortTest %1 1000 R I %TIMES%
title %1 1000 R (Str)
java.exe -server -classpath bin  mmsort.SortTest %1 1000 R S %TIMES%
title %1 1000 U
java.exe -server -classpath bin  mmsort.SortTest %1 1000 U I %TIMES%
title %1 1000 H
java.exe -server -classpath bin  mmsort.SortTest %1 1000 H I %TIMES%
title %1 1000 A
java.exe -server -classpath bin  mmsort.SortTest %1 1000 A I %TIMES%
title %1 1000 D
java.exe -server -classpath bin  mmsort.SortTest %1 1000 D I %TIMES%
title %1 1000 F
java.exe -server -classpath bin  mmsort.SortTest %1 1000 F I %TIMES%

title %1 10000 R
java.exe -server -classpath bin  mmsort.SortTest %1 10000 R I %TIMES%
title %1 10000 R (Str)
java.exe -server -classpath bin  mmsort.SortTest %1 10000 R S %TIMES%
title %1 10000 U
java.exe -server -classpath bin  mmsort.SortTest %1 10000 U I %TIMES%
title %1 10000 H
java.exe -server -classpath bin  mmsort.SortTest %1 10000 H I %TIMES%
title %1 10000 A
java.exe -server -classpath bin  mmsort.SortTest %1 10000 A I %TIMES%
title %1 10000 D
java.exe -server -classpath bin  mmsort.SortTest %1 10000 D I %TIMES%
title %1 10000 F
java.exe -server -classpath bin  mmsort.SortTest %1 10000 F I %TIMES%

title %1 100000 R
java.exe -server -classpath bin  mmsort.SortTest %1 100000 R I %TIMES%
title %1 100000 R (Str)
java.exe -server -classpath bin  mmsort.SortTest %1 100000 R S %TIMES%
title %1 100000 U
java.exe -server -classpath bin  mmsort.SortTest %1 100000 U I %TIMES%
title %1 100000 H
java.exe -server -classpath bin  mmsort.SortTest %1 100000 H I %TIMES%
title %1 100000 A
java.exe -server -classpath bin  mmsort.SortTest %1 100000 A I %TIMES%
title %1 100000 D
java.exe -server -classpath bin  mmsort.SortTest %1 100000 D I %TIMES%
title %1 100000 F
java.exe -server -classpath bin  mmsort.SortTest %1 100000 F I %TIMES%

title %1 1000000 R
java.exe -server -classpath bin  mmsort.SortTest %1 1000000 R I %TIMES%
title %1 1000000 R (Str)
java.exe -server -classpath bin  mmsort.SortTest %1 1000000 R S %TIMES%
title %1 1000000 U
java.exe -server -classpath bin  mmsort.SortTest %1 1000000 U I %TIMES%
title %1 1000000 H
java.exe -server -classpath bin  mmsort.SortTest %1 1000000 H I %TIMES%
title %1 1000000 A
java.exe -server -classpath bin  mmsort.SortTest %1 1000000 A I %TIMES%
title %1 1000000 D
java.exe -server -classpath bin  mmsort.SortTest %1 1000000 D I %TIMES%
title %1 1000000 F
java.exe -server -classpath bin  mmsort.SortTest %1 1000000 F I %TIMES%

title %1 10000000 R
java.exe -server -classpath bin  mmsort.SortTest %1 10000000 R I %TIMES%
title %1 10000000 R (Str)
java.exe -server -classpath bin  mmsort.SortTest %1 10000000 R S %TIMES%
title %1 10000000 U
java.exe -server -classpath bin  mmsort.SortTest %1 10000000 U I %TIMES%
title %1 10000000 H
java.exe -server -classpath bin  mmsort.SortTest %1 10000000 H I %TIMES%
title %1 10000000 A
java.exe -server -classpath bin  mmsort.SortTest %1 10000000 A I %TIMES%
title %1 10000000 D
java.exe -server -classpath bin  mmsort.SortTest %1 10000000 D I %TIMES%
title %1 10000000 F
java.exe -server -classpath bin  mmsort.SortTest %1 10000000 F I %TIMES%

rem title %1 100000000 R
rem java.exe -server -classpath bin  mmsort.SortTest %1 100000000 R I %TIMES%
rem title %1 100000000 R (Str)
rem java.exe -server -classpath bin  mmsort.SortTest %1 100000000 R S %TIMES%
rem title %1 100000000 U
rem java.exe -server -classpath bin  mmsort.SortTest %1 100000000 U I %TIMES%
rem title %1 100000000 H
rem java.exe -server -classpath bin  mmsort.SortTest %1 100000000 H I %TIMES%
rem title %1 100000000 A
rem java.exe -server -classpath bin  mmsort.SortTest %1 100000000 A I %TIMES%
rem title %1 100000000 D
rem java.exe -server -classpath bin  mmsort.SortTest %1 100000000 D I %TIMES%
rem title %1 100000000 F
rem java.exe -server -classpath bin  mmsort.SortTest %1 100000000 F I %TIMES%
