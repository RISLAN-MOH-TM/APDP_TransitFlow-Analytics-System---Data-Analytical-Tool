@echo off
REM ============================================================================
REM Interactive Test Menu
REM ============================================================================

:MENU
cls
cd /d "%~dp0"

echo.
echo ============================================================================
echo TRANSITFLOW ANALYTICS - TEST EXECUTION MENU
echo ============================================================================
echo.
echo Select an option:
echo.
echo   1. Run ALL tests (106 tests)
echo   2. Run all MODEL tests (50 tests)
echo   3. Run all CONTROLLER tests (14 tests)
echo   4. Run SPECIFIC test class
echo   5. Run INTEGRATION tests only
echo   6. Run UNIT tests only
echo   7. Quick test (CustomerModelTest)
echo   8. Quick test (DatasetServiceTest)
echo   9. View test list
echo   0. Exit
echo.
echo ============================================================================
echo.

set /p CHOICE="Enter your choice (0-9): "

if "%CHOICE%"=="1" goto RUN_ALL
if "%CHOICE%"=="2" goto RUN_MODELS
if "%CHOICE%"=="3" goto RUN_CONTROLLERS
if "%CHOICE%"=="4" goto RUN_SPECIFIC
if "%CHOICE%"=="5" goto RUN_INTEGRATION
if "%CHOICE%"=="6" goto RUN_UNIT
if "%CHOICE%"=="7" goto RUN_CUSTOMER
if "%CHOICE%"=="8" goto RUN_DATASET
if "%CHOICE%"=="9" goto VIEW_LIST
if "%CHOICE%"=="0" goto EXIT

echo Invalid choice! Please try again.
timeout /t 2 >nul
goto MENU

:RUN_ALL
echo.
echo Running ALL tests...
echo.
call mvnw.cmd test
pause
goto MENU

:RUN_MODELS
echo.
echo Running all MODEL tests...
echo.
call mvnw.cmd test -Dtest=*ModelTest
pause
goto MENU

:RUN_CONTROLLERS
echo.
echo Running all CONTROLLER tests...
echo.
call mvnw.cmd test -Dtest=*ControllerTest
pause
goto MENU

:RUN_SPECIFIC
echo.
set /p TEST_NAME="Enter test class name: "
echo.
echo Running %TEST_NAME%...
echo.
call mvnw.cmd test -Dtest=%TEST_NAME%
pause
goto MENU

:RUN_INTEGRATION
echo.
echo Running INTEGRATION tests...
echo.
call mvnw.cmd test -Dtest=DatasetServiceTest,TransitFlowApplicationTest
pause
goto MENU

:RUN_UNIT
echo.
echo Running UNIT tests (service package)...
echo.
call mvnw.cmd test -Dtest=com.transitflow.service.*
pause
goto MENU

:RUN_CUSTOMER
echo.
echo Running CustomerModelTest (quick test)...
echo.
call mvnw.cmd test -Dtest=CustomerModelTest
pause
goto MENU

:RUN_DATASET
echo.
echo Running DatasetServiceTest (quick test)...
echo.
call mvnw.cmd test -Dtest=DatasetServiceTest
pause
goto MENU

:VIEW_LIST
cls
echo.
echo ============================================================================
echo ALL AVAILABLE TEST CLASSES (17 files, 106 tests)
echo ============================================================================
echo.
echo ADAPTER TESTS (10 tests):
echo   - CsvDatasetAdapterTest
echo.
echo CONTROLLER TESTS (14 tests):
echo   - AnalyticsControllerTest (8 tests)
echo   - DatasetControllerTest (6 tests)
echo.
echo SERVICE TESTS (9 tests):
echo   - DatasetServiceTest - Integration (7 tests)
echo   - com.transitflow.service.DatasetServiceTest - Unit (2 tests)
echo.
echo FACADE TESTS (9 tests):
echo   - TransitFlowFacadeTest
echo.
echo FACTORY TESTS (10 tests):
echo   - FactoryPatternTest
echo.
echo MODEL TESTS (50 tests):
echo   - CustomerModelTest (8 tests)
echo   - DeliveryModelTest (4 tests)
echo   - FaultyRecordModelTest (9 tests)
echo   - OrderItemModelTest (7 tests)
echo   - OrderModelTest (5 tests)
echo   - PaymentModelTest (6 tests)
echo   - ProductModelTest (3 tests)
echo   - SellerModelTest (8 tests)
echo.
echo STRATEGY TESTS (3 tests):
echo   - RevenueAnalysisStrategyTest
echo.
echo APPLICATION TESTS (1 test):
echo   - TransitFlowApplicationTest
echo.
echo ============================================================================
echo.
pause
goto MENU

:EXIT
echo.
echo Goodbye!
echo.
exit /b 0
