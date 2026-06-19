@echo off
REM ================================================================================
REM TransitFlow Analytics - Test Execution Script
REM ================================================================================
REM This script runs all JUnit tests using Maven Wrapper (mvnw.cmd)
REM ================================================================================

echo.
echo ================================================================================
echo TRANSITFLOW ANALYTICS - RUNNING ALL TESTS
echo ================================================================================
echo.

cd /d "%~dp0"

echo Current Directory: %CD%
echo.
echo Starting test execution...
echo.

REM Run tests using Maven Wrapper
call mvnw.cmd clean test

echo.
echo ================================================================================
echo TEST EXECUTION COMPLETE
echo ================================================================================
echo.
echo Check the results above for:
echo   - Tests run
echo   - Failures
echo   - Errors
echo   - Skipped
echo.
echo For detailed reports, check:
echo   target\surefire-reports\
echo.

pause
