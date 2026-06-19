@echo off
echo ========================================
echo Running Binary Search Algorithm Tests
echo ========================================
echo.

cd /d "%~dp0"

call mvnw.cmd test -Dtest=BinarySearchTest

echo.
echo ========================================
echo Algorithm Tests Complete
echo ========================================
pause
