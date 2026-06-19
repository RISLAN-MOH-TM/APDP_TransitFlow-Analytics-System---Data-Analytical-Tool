@echo off
REM ============================================================================
REM Run All Model Tests
REM ============================================================================

cd /d "%~dp0"

echo.
echo ============================================================================
echo RUNNING ALL MODEL TESTS
echo ============================================================================
echo.
echo Test Classes:
echo   - CustomerModelTest (8 tests)
echo   - DeliveryModelTest (4 tests)
echo   - FaultyRecordModelTest (9 tests)
echo   - OrderItemModelTest (7 tests)
echo   - OrderModelTest (5 tests)
echo   - PaymentModelTest (6 tests)
echo   - ProductModelTest (3 tests)
echo   - SellerModelTest (8 tests)
echo.
echo Total: 50 tests
echo ============================================================================
echo.

call mvnw.cmd test -Dtest=*ModelTest

echo.
echo ============================================================================
echo TEST EXECUTION COMPLETED
echo ============================================================================
echo.
pause
