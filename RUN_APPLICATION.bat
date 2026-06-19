@echo off
echo ========================================
echo TransitFlow Analytics System Launcher
echo ========================================
echo.

echo Step 1: Starting Spring Boot Backend...
echo.
cd backend
start "TransitFlow Backend" cmd /k "mvnw.cmd spring-boot:run"

echo Waiting for backend to initialize (15 seconds)...
timeout /t 15 /nobreak

echo.
echo Step 2: Starting Streamlit Frontend...
echo.
cd ..\frontend
start "TransitFlow Frontend" cmd /k "streamlit run streamlit_app.py"

echo.
echo ========================================
echo Both services are starting!
echo ========================================
echo Backend: http://localhost:8080
echo Frontend: http://localhost:8501
echo H2 Console: http://localhost:8080/h2-console
echo.
echo IMPORTANT: Keep both terminal windows open!
echo Close them to stop the services.
echo.
echo Press any key to close this launcher...
pause
