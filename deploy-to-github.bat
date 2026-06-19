@echo off
REM TransitFlow Analytics System - GitHub Deployment Script

setlocal enabledelayedexpansion

echo ========================================
echo TransitFlow GitHub Deployment Script
echo ========================================
echo.

REM Check if Git is installed
git --version >nul 2>&1
if errorlevel 1 (
    echo Error: Git is not installed
    echo Please install Git from: https://git-scm.com/download/win
    pause
    exit /b 1
)

echo Git is installed
echo.

REM Get current directory
set "PROJECT_DIR=%cd%"
echo Project Directory: %PROJECT_DIR%
echo.

REM Check if .git directory exists
if not exist ".git" (
    echo Initializing Git repository...
    git init
    echo.
) else (
    echo Git repository already initialized
    echo.
)

REM Configure Git user (if not already configured)
git config user.name >nul 2>&1
if errorlevel 1 (
    echo Please enter your GitHub username:
    set /p GIT_USERNAME="> "
    git config user.name "!GIT_USERNAME!"
    echo.
)

git config user.email >nul 2>&1
if errorlevel 1 (
    echo Please enter your GitHub email:
    set /p GIT_EMAIL="> "
    git config user.email "!GIT_EMAIL!"
    echo.
)

echo Git Configuration:
echo Username: 
git config user.name
echo Email: 
git config user.email
echo.

REM Check for unstaged changes
echo Checking for changes...
git status --short
echo.

REM Ask for commit message
echo Enter commit message (or press Enter for default):
set /p COMMIT_MSG="> "
if "!COMMIT_MSG!"=="" set COMMIT_MSG=Update: TransitFlow Analytics System with Docker support

echo.
echo Staging all changes...
git add .

echo.
echo Creating commit...
git commit -m "!COMMIT_MSG!"

if errorlevel 1 (
    echo No changes to commit or commit failed
    echo.
)

REM Check if remote exists
git remote get-url origin >nul 2>&1
if errorlevel 1 (
    echo.
    echo No remote repository configured
    echo.
    echo GitHub Repository URL:
    echo https://github.com/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool.git
    echo.
    set /p ADD_REMOTE="Do you want to add this remote? (Y/N): "
    if /i "!ADD_REMOTE!"=="Y" (
        git remote add origin https://github.com/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool.git
        echo Remote added successfully
    ) else (
        echo Skipping remote addition
        echo Please add remote manually: git remote add origin [URL]
        pause
        exit /b 0
    )
) else (
    echo Remote repository already configured:
    git remote get-url origin
)

echo.
echo ========================================
echo Ready to push to GitHub
echo ========================================
echo.
echo Branch: main
echo Remote: origin
echo.
set /p PUSH_NOW="Push to GitHub now? (Y/N): "

if /i "!PUSH_NOW!"=="Y" (
    echo.
    echo Pushing to GitHub...
    echo.
    
    REM Try to push
    git push -u origin main
    
    if errorlevel 1 (
        echo.
        echo Push failed. This might be because:
        echo 1. The branch name is different (try 'master' instead of 'main')
        echo 2. Authentication failed (check your credentials)
        echo 3. Remote repository doesn't exist
        echo.
        set /p TRY_MASTER="Try pushing to 'master' branch? (Y/N): "
        if /i "!TRY_MASTER!"=="Y" (
            git branch -M main
            git push -u origin main
        )
    ) else (
        echo.
        echo ========================================
        echo Successfully pushed to GitHub!
        echo ========================================
        echo.
        echo Repository URL:
        echo https://github.com/RISLAN-MOH-TM/APDP_TransitFlow-Analytics-System---Data-Analytical-Tool
        echo.
        echo Next Steps:
        echo 1. Visit your repository on GitHub
        echo 2. Verify all files are uploaded
        echo 3. Update repository description
        echo 4. Add topics for discoverability
        echo 5. Enable GitHub Actions
        echo.
    )
) else (
    echo.
    echo Push cancelled by user
    echo To push manually later, run:
    echo   git push -u origin main
    echo.
)

echo ========================================
echo Deployment Script Complete
echo ========================================
echo.
pause

endlocal
