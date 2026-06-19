@echo off
REM ============================================================================
REM Run All Controller Tests
REM ============================================================================

cd /d "%~dp0"

echo.
echo ============================================================================
echo RUNNING ALL CONTROLLER TESTS
echo ============================================================================
echo.
echo Test Classes:
echo   - AnalyticsControllerTest (8 tests)
echo   - DatasetControllerTest (6 tests)
echo.
echo Total: 14 tests
echo ============================================================================
echo.

call mvnw.cmd test -Dtest=*ControllerTest

echo.
echo ============================================================================
echo TEST EXECUTION COMPLETED
echo ============================================================================
echo.
pause
