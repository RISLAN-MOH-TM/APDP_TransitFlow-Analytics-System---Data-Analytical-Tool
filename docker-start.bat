@echo off
REM TransitFlow Analytics System - Docker Startup Script (Windows)

echo ========================================
echo TransitFlow Analytics System
echo Docker Startup Script
echo ========================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker is not installed
    echo Please install Docker Desktop from: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

REM Check if Docker Compose is installed
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker Compose is not installed
    echo Please install Docker Desktop which includes Docker Compose
    pause
    exit /b 1
)

REM Check if Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo Error: Docker daemon is not running
    echo Please start Docker Desktop and try again
    pause
    exit /b 1
)

echo Docker is installed and running
echo.

REM Check if docker-compose.yml exists
if not exist "docker-compose.yml" (
    echo Error: docker-compose.yml not found
    echo Please run this script from the project root directory
    pause
    exit /b 1
)

echo Building and starting TransitFlow services...
echo.

REM Build and start services
docker-compose up -d --build

echo.
echo Waiting for services to be healthy...
timeout /t 10 /nobreak >nul

REM Check service status
echo.
echo Service Status:
docker-compose ps

echo.
echo ========================================
echo TransitFlow System Started!
echo ========================================
echo.
echo Access the application at:
echo   Frontend:   http://localhost:8501
echo   Backend:    http://localhost:8080
echo   H2 Console: http://localhost:8080/h2-console
echo.
echo To view logs:
echo   docker-compose logs -f
echo.
echo To stop the system:
echo   docker-compose down
echo.
echo ========================================
echo.
pause
