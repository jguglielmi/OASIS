REM @echo off
REM set MYPATH=%~dp0
REM cd %~dp0

REM set MAVEN_HOME=%MYPATH%builders\apache-maven
REM set JAVA_HOME=C:\Progra~1\Java\jdk1.7.0_40
REM set JAVA_HOME=V:\dev\utils\jdk1.7.0_25
REM set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin\;%~dp0;%PATH%



REM download dependencies using maven
call mvn install:install-file -Dfile=./fitnesse-standalone.jar -DgroupId=org.fitnesse -DartifactId=fitnesse-standalone -Dversion=03192014 -Dpackaging=jar
call mvn install:install-file -Dfile=./lib/JmeterBundle.jar -DgroupId=JMeterBundle -DartifactId=JMeterBundle -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
call mvn install:install-file -Dfile=./lib/sqljdbc4.jar -DgroupId=sqljdbc4 -DartifactId=sqljdbc4 -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=./plugins/synthuse.jar -DgroupId=synthuse -DartifactId=synthuse -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=./plugins/sikuli-java.jar -DgroupId=org.sikuli -DartifactId=sikuli-java -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=./plugins/oasisplugin.jar -DgroupId=oasis -DartifactId=oasis-plugin -Dversion=1.0 -Dpackaging=jar
call mvn -Pfitnesse test

REM java -jar fitnesse-standalone.jar -p 8000 -o -e 0 -d .

