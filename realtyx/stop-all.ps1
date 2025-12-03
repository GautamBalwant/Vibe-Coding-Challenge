# RealtyX - Stop All Services

Write-Host "Stopping RealtyX Services..." -ForegroundColor Red

Write-Host "Stopping Java processes (Backend & Data Service)..." -ForegroundColor Yellow
taskkill /F /IM java.exe 2>&1 | Out-Null

Write-Host "Stopping Node.js processes (UI)..." -ForegroundColor Yellow
taskkill /F /IM node.exe 2>&1 | Out-Null

Write-Host ""
Write-Host "All services stopped." -ForegroundColor Green
