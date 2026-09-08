@echo off
setlocal
cd /d "%~dp0"

echo ==========================================
echo   BTl_java - Quan ly nhan su
echo ==========================================
echo.

where java >nul 2>nul
if errorlevel 1 (
    echo [LOI] Chua cai Java. Hay cai JDK 21.
    pause
    exit /b 1
)

if not exist "lib" mkdir "lib"
if not exist "lib\mysql-connector-j-9.0.0.jar" (
    echo [INFO] Chua co MySQL Connector/J. Dang tai ve...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.0.0/mysql-connector-j-9.0.0.jar' -OutFile 'lib\mysql-connector-j-9.0.0.jar'"
    if errorlevel 1 (
        echo [LOI] Khong tai duoc MySQL Connector/J.
        echo Hay tai mysql-connector-j-9.0.0.jar va dat vao thu muc lib.
        pause
        exit /b 1
    )
)

if not exist "out\classes" mkdir "out\classes"

echo [1/2] Dang bien dich...
del /q "out\classes\*.class" >nul 2>nul
javac -encoding UTF-8 -cp "lib\mysql-connector-j-9.0.0.jar" -d "out\classes" src\config\DBConnection.java src\model\*.java src\dao\*.java src\bus\*.java src\utils\*.java src\gui\*.java src\main\*.java
if errorlevel 1 (
    echo.
    echo [LOI] Bien dich that bai. Xem loi o phia tren.
    pause
    exit /b 1
)

echo [2/2] Dang khoi dong...
java -Dfile.encoding=UTF-8 -cp "out\classes;lib\mysql-connector-j-9.0.0.jar" main.Main

if errorlevel 1 (
    echo.
    echo [LOI] Ung dung da dung. Kiem tra MySQL va cau hinh DB.
    pause
)
endlocal
