@echo off
rem Copyright (c) 2015 matsubara masakazu
rem Released under the MIT license
rem https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

set TIMES=%2
set JVM_ARGS=-server
title %1 100 U
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100 U   I %TIMES%
title %1 100 U (Str)
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100 U   S %TIMES%
title %1 100 R10
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100 R10 I %TIMES%
title %1 100 H
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100 H   I %TIMES%
title %1 100 A
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100 A   I %TIMES%
title %1 100 D
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100 D   I %TIMES%
title %1 100 F
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100 F   I %TIMES%

title %1 1000 U
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000 U    I %TIMES%
title %1 1000 U (Str)
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000 U    S %TIMES%
title %1 1000 R10
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000 R10  I %TIMES%
title %1 1000 R100
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000 R100 I %TIMES%
title %1 1000 H
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000 H    I %TIMES%
title %1 1000 A
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000 A    I %TIMES%
title %1 1000 D
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000 D    I %TIMES%
title %1 1000 F
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000 F    I %TIMES%

title %1 10000 U
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 U     I %TIMES%
title %1 10000 U (Str)
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 U     S %TIMES%
title %1 10000 R10
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 R10   I %TIMES%
title %1 10000 R100
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 R100  I %TIMES%
title %1 10000 R1000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 R1000 I %TIMES%
title %1 10000 H
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 H     I %TIMES%
title %1 10000 A
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 A     I %TIMES%
title %1 10000 D
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 D     I %TIMES%
title %1 10000 F
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000 F     I %TIMES%

title %1 100000 U
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 U      I %TIMES%
title %1 100000 U (Str)
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 U      S %TIMES%
title %1 100000 R10
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 R10    I %TIMES%
title %1 100000 R100
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 R100   I %TIMES%
title %1 100000 R1000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 R1000  I %TIMES%
title %1 100000 R10000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 R10000 I %TIMES%
title %1 100000 H
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 H      I %TIMES%
title %1 100000 A
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 A      I %TIMES%
title %1 100000 D
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 D      I %TIMES%
title %1 100000 F
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 100000 F      I %TIMES%

title %1 1000000 U
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 U       I %TIMES%
title %1 1000000 U (Str)
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 U       S %TIMES%
title %1 1000000 R10
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 R10     I %TIMES%
title %1 1000000 R100
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 R100    I %TIMES%
title %1 1000000 R1000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 R1000   I %TIMES%
title %1 1000000 R10000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 R10000  I %TIMES%
title %1 1000000 R100000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 R100000 I %TIMES%
title %1 1000000 H
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 H       I %TIMES%
title %1 1000000 A
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 A       I %TIMES%
title %1 1000000 D
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 D       I %TIMES%
title %1 1000000 F
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 1000000 F       I %TIMES%

title %1 10000000 U
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 U        I %TIMES%
title %1 10000000 U (Str)
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 U        S %TIMES%
title %1 10000000 R10
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 R10      I %TIMES%
title %1 10000000 R100
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 R100     I %TIMES%
title %1 10000000 R1000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 R1000    I %TIMES%
title %1 10000000 R10000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 R10000   I %TIMES%
title %1 10000000 R100000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 R100000  I %TIMES%
title %1 10000000 R1000000
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 R1000000 I %TIMES%
title %1 10000000 H
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 H        I %TIMES%
title %1 10000000 A
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 A        I %TIMES%
title %1 10000000 D
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 D        I %TIMES%
title %1 10000000 F
java.exe %JVM_ARGS% -classpath bin  mmsort.SortTest %1 10000000 F        I %TIMES%
