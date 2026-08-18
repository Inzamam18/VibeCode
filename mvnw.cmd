<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM ----------------------------------------------------------------------------

@ECHO OFF

@IF "%DEBUG%"=="" @ECHO OFF

@SETLOCAL

SET ERROR_CODE=0

@REM To isolate internal variables from possible post scripts, we use another setlocal
@SETLOCAL

@REM ==== START VALIDATION ====
IF NOT "%JAVA_HOME%"=="" GOTO OkJHome
FOR %%i IN (java.exe) DO SET JAVA_EXE=%%~$PATH:i
IF NOT "%JAVA_EXE%"=="" GOTO CheckEnv
ECHO.
ECHO Error: JAVA_HOME not found in your environment. >&2
ECHO Please set the JAVA_HOME variable in your environment to match the >&2
ECHO location of your Java installation. >&2
ECHO.
GOTO error

:OkJHome
SET JAVA_EXE=%JAVA_HOME%\bin\java.exe

:CheckEnv
IF EXIST "%JAVA_EXE%" GOTO init

ECHO.
ECHO Error: JAVA_HOME is set to an invalid directory. >&2
ECHO JAVA_HOME = "%JAVA_HOME%" >&2
ECHO Please set the JAVA_HOME variable in your environment to match the >&2
ECHO location of your Java installation. >&2
ECHO.
GOTO error

:init
@REM Find the project base dir
SET BASE_DIR=%~dp0
SET BASE_DIR=%BASE_DIR:~0,-1%

SET MAVEN_PROJECTBASEDIR=%BASE_DIR%
IF "%MAVEN_PROJECTBASEDIR%"=="" SET MAVEN_PROJECTBASEDIR=%CD%

SET WRAPPER_JAR="%BASE_DIR%\.mvn\wrapper\maven-wrapper.jar"
SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

IF EXIST %WRAPPER_JAR% GOTO run

powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar', '%WRAPPER_JAR%')}"

:run
"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -classpath %WRAPPER_JAR% %WRAPPER_LAUNCHER% %*
IF ERRORLEVEL 1 GOTO error
GOTO end

:error
SET ERROR_CODE=1

:end
@ENDLOCAL
EXIT /B %ERROR_CODE%
#>
