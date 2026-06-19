@echo off
echo ========================================
echo Starting TransitFlow Backend Server
echo ========================================
echo.
echo Server will start on http://localhost:8080
echo H2 Console: http://localhost:8080/h2-console
echo.
echo Press Ctrl+C to stop the server
echo ========================================
echo.

cd /d "%~dp0"

REM Clean and compile first
echo Cleaning and compiling...
call mvnw.cmd clean compile

echo.
echo Starting Spring Boot application...
echo.

REM Start the application with Java 24 compatibility flags
call mvnw.cmd spring-boot:run

pause
