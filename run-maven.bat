REM @echo off
REM set MYPATH=%~dp0
REM cd %~dp0

REM set MAVEN_HOME=%MYPATH%builders\apache-maven
REM set JAVA_HOME=C:\Progra~1\Java\jdk1.7.0_40
REM set JAVA_HOME=V:\dev\utils\jdk1.7.0_25
REM set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin\;%~dp0;%PATH%



REM download dependencies using maven
mvn -Pfitnesse test

REM java -jar fitnesse-standalone.jar -p 8000 -o -e 0 -d .

