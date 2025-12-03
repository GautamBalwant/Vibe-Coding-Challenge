# RealtyX - Start All Services

Write-Host "Starting RealtyX Services..." -ForegroundColor Green
Write-Host ""

# Create log directory
$logDir = "logs"
if (-not (Test-Path $logDir)) {
    New-Item -ItemType Directory -Path $logDir | Out-Null
}

Write-Host "1. Starting Data Service (Port 9090)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd data-service; mvn spring-boot:run"

Write-Host "   Waiting for Data Service to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "2. Starting Main Backend (Port 8080)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd backend; mvn spring-boot:run"

Write-Host "   Waiting for Backend to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "3. Starting UI Application (Port 3001)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd ui; node app.js"

Write-Host ""
Write-Host "All services are starting..." -ForegroundColor Green
Write-Host ""
Write-Host "Services:" -ForegroundColor White
Write-Host "  Data Service:  http://localhost:9090" -ForegroundColor Yellow
Write-Host "  Main Backend:  http://localhost:8080" -ForegroundColor Yellow
Write-Host "  UI Application: http://localhost:3001" -ForegroundColor Yellow
Write-Host ""
Write-Host "Press any key to stop all services..." -ForegroundColor Red
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

Write-Host ""
Write-Host "Stopping all services..." -ForegroundColor Red
taskkill /F /IM java.exe
taskkill /F /IM node.exe
Write-Host "All services stopped." -ForegroundColor Green
