@echo off
set MYPATH=%~dp0
cd %~dp0

set MAVEN_HOME=%MYPATH%builders\apache-maven
set JAVA_HOME=C:\Progra~1\Java\jdk1.7.0_40
REM set JAVA_HOME=V:\dev\utils\jdk1.7.0_25
set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin\;%~dp0;%PATH%



REM download dependencies using maven
mvn compile

java -jar fitnesse-standalone.jar -p 8000 -o -e 0 -d .
