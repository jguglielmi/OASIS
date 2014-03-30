set WGET_HOME=%CD%\lib\wget-1.11.4-1-bin
set Path=%Path%;%WGET_HOME%;%WGET_HOME%\bin
cd %~dp0
call local-ant.bat resolve run
