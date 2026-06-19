@echo off
REM ============================================================================
REM Run Specific Test Class or Method
REM ============================================================================

cd /d "%~dp0"

echo.
echo ============================================================================
echo RUN SPECIFIC TEST
echo ============================================================================
echo.
echo Examples:
echo   - CustomerModelTest                     (run entire class)
echo   - CustomerModelTest#testCustomerCreation (run specific method)
echo   - *ModelTest                            (run all model tests)
echo   - *ControllerTest                       (run all controller tests)
echo.
echo Available Test Classes:
echo   Adapter:     CsvDatasetAdapterTest
echo   Controllers: AnalyticsControllerTest, DatasetControllerTest
echo   Service:     DatasetServiceTest
echo   Facade:      TransitFlowFacadeTest
echo   Factory:     FactoryPatternTest
echo   Models:      CustomerModelTest, OrderModelTest, PaymentModelTest,
echo                DeliveryModelTest, ProductModelTest, SellerModelTest,
echo                OrderItemModelTest, FaultyRecordModelTest
echo   Strategy:    RevenueAnalysisStrategyTest
echo   Application: TransitFlowApplicationTest
echo.
echo ============================================================================
echo.

set /p TEST_NAME="Enter test name (or pattern): "

if "%TEST_NAME%"=="" (
    echo Error: No test name provided!
    pause
    exit /b 1
)

echo.
echo Running: %TEST_NAME%
echo.

call mvnw.cmd test -Dtest=%TEST_NAME%

echo.
echo ============================================================================
echo TEST EXECUTION COMPLETED
echo ============================================================================
echo.
pause
