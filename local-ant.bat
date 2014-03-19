@echo off
set MYPATH=%~dp0
cd %~dp0

REM Need to declare ANT_HOME for apache-ivy to work, ivy.jar needs to be in $ANT_HOME\lib
set ANT_HOME=%MYPATH%builders\apache-ant
REM set JAVA_HOME=C:\Progra~1\Java\jdk1.7.0_40
FOR /F "skip=2 tokens=2*" %%A IN ('REG QUERY "HKLM\Software\JavaSoft\Java Development Kit" /v CurrentVersion') DO set JAVA_VER=%%B
FOR /F "skip=2 tokens=2*" %%A IN ('REG QUERY "HKLM\Software\JavaSoft\Java Development Kit\%JAVA_VER%" /v JavaHome') DO set JAVA_HOME=%%B
set PATH=%ANT_HOME%\bin;%JAVA_HOME%\bin;%~dp0;%PATH%

REM bootstrap can be used to download the apache ivy library if not already installed
REM call ant bootstrap
REM if you're offline and you already have all libraries don't call the resolve line below
REM call ant resolve
REM the below ant command will startup the oasis framework
REM call ant run

REM java -jar fitnesse-standalone.jar -p 8000 -o -e 0 -d .

call ant %*

