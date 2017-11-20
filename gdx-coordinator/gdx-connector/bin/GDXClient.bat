@echo off

rem Specify the java home directory 
rem set JAVA_HOME=@javahome

rem build the java command 
set JAVA=java
if not "%JAVA_HOME%" == "" set JAVA=%JAVA_HOME%\bin\java

rem This will be the run directory
set RUN_DIR=%cd%
rem Move up...
cd ..
rem This will be the base directory
set BASE_DIR=%cd%

set JAVA_OPTS="-Xmx2048m -Dosiris.run.dir=%RUN_DIR% -Dosiris.base.dir=%BASE_DIR%"


echo.
echo.========================================================================
echo.
echo   Osiris3 Server (ETRACS) 
echo.
echo   JAVA        : %JAVA%
echo   JAVA_HOME   : %JAVA_HOME%
echo   JAVA_OPTS   : %JAVA_OPTS%
echo.
echo.========================================================================
echo.

"%JAVA%" "%JAVA_OPTS%" -cp lib/*;. com.rameses.main.bootloader.MainBootLoader
pause
