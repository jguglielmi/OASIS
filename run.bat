cd %~dp0

REM set JAVA_HOME=C:\Progra~1\Java\jdk1.7.0_40
echo searching windows registry for java dev kit (jdk)
FOR /F "skip=2 tokens=2*" %%A IN ('REG QUERY "HKLM\Software\JavaSoft\Java Development Kit" /v CurrentVersion') DO set JAVA_VER=%%B
FOR /F "skip=2 tokens=2*" %%A IN ('REG QUERY "HKLM\Software\JavaSoft\Java Development Kit\%JAVA_VER%" /v JavaHome') DO set JAVA_HOME=%%B
IF ["%JAVA_HOME%"] == [""] set JAVA_HOME=%~dp0..\jdk\bin

echo setting java variables JAVA_VER=%JAVA_VER% and JAVA_HOME=%JAVA_HOME%
set PATH=%JAVA_HOME%\bin;%~dp0;%PATH%

REM only download libraries if they don't already exist
if not exist lib\ivy call local-ant.bat resolve
if not exist lib\jmeter call local-ant.bat resolve


REM only compile fixtures if they don't already exist
if not exist build call local-ant.bat compile unit_test


REM give optional test.url argument to fitnesse otherwise set default value as test.com
set URL=%1
IF [%1] == [] set url=test.com


REM start oasis using java
java -Dtest.url=%URL% -jar fitnesse-standalone.jar -p 8000 -o -e 0 -d .
