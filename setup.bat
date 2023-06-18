@ECHO OFF 

cls

TITLE Yooreeka setup on Windows 10 (or later)

ECHO Checking some system information.
ECHO:
ECHO ==================================================================================

ECHO   WINDOWS INFO

ECHO ==================================================================================

systeminfo | findstr /c:"OS Name"

systeminfo | findstr /c:"OS Version"

systeminfo | findstr /c:"System Type"

ECHO:
ECHO ==================================================================================
ECHO   HARDWARE INFO
ECHO ==================================================================================

systeminfo | findstr /c:"Total Physical Memory"

wmic cpu get name

ECHO:

ECHO ==================================================================================

ECHO   Set up YOOOREEKA_HOME

ECHO ==================================================================================

set YOOREEKA_HOME=%~dp0
ECHO   YOOOREEKA_HOME=%YOOREEKA_HOME%

ECHO __________________________________________________________________________________ 
ECHO NOTE: Set the property yooreeka.home in the yooreeka.properties file, so that
ECHO       it points to the installation directory mentioned above. Make sure the rest 
ECHO       of the properties are consistently pointing to the appropriate locations. 

ECHO ==================================================================================

SETLOCAL ENABLEEXTENSIONS

IF DEFINED JAVA_HOME (ECHO JAVA_HOME = %JAVA_HOME%) ELSE (exit /b)
ECHO:
 
IF DEFINED ANT_HOME (ECHO ANT_HOME = %ANT_HOME%) ELSE (exit /b)

set PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%PATH%

ECHO %PATH%
ECHO:
ECHO ==================================================================================

ECHO:

ECHO Setting up JAVA_TOOLS_OPTIONS

set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8

ECHO:

ECHO Reset the CLASSPATH variable

set "CLASSPATH="

ECHO:

ECHO Checking Java

ECHO:

java -version

ECHO:

call ant -version

ECHO:

ECHO ==================================================================================
ECHO:
ECHO To run the build file go to %YOOREEKA_HOME%\build and run: ant
ECHO to run the tests run: ant tests
ECHO:
ECHO ==================================================================================
