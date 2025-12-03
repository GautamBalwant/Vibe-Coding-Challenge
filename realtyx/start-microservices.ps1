# RealtyX Microservices Startup Script

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Starting RealtyX Microservices" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan

# Function to check if port is in use
function Test-Port {
    param($port)
    $connection = Test-NetConnection -ComputerName localhost -Port $port -WarningAction SilentlyContinue -InformationLevel Quiet
    return $connection
}

# Stop any existing services
Write-Host "[1/5] Stopping existing services..." -ForegroundColor Yellow
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
Get-Process node -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 2

# Start Service Registry (Eureka)
Write-Host "`n[2/5] Starting Service Registry (Eureka - Port 8761)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd service-registry; Write-Host 'Starting Eureka Server...' -ForegroundColor Green; mvn spring-boot:run"
Write-Host "      Waiting for Service Registry to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Verify Eureka is running
if (Test-Port 8761) {
    Write-Host "      [OK] Service Registry is running!" -ForegroundColor Green
} else {
    Write-Host "      [!!] Service Registry failed to start!" -ForegroundColor Red
    exit 1
}

# Start Data Service
Write-Host "`n[3/5] Starting Data Service (Port 9090)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd data-service; Write-Host 'Starting Data Service...' -ForegroundColor Green; mvn spring-boot:run"
Write-Host "      Waiting for Data Service to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 12

# Start Backend Service
Write-Host "`n[4/5] Starting Backend Service (Port 8080)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd backend; Write-Host 'Starting Backend Service...' -ForegroundColor Green; mvn spring-boot:run"
Write-Host "      Waiting for Backend Service to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 12

# Start API Gateway
Write-Host "`n[5/5] Starting API Gateway (Port 8765)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd api-gateway; Write-Host 'Starting API Gateway...' -ForegroundColor Green; mvn spring-boot:run"
Write-Host "      Waiting for API Gateway to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 12

# Start UI Application
Write-Host "`n[6/6] Starting UI Application (Port 3001)..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd ui; Write-Host 'Starting UI Application...' -ForegroundColor Green; node app.js"
Write-Host "      Waiting for UI Application to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

# Final Status Check
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Service Status Check" -ForegroundColor White
Write-Host "========================================`n" -ForegroundColor Cyan

$services = @(
    @{Name="Service Registry"; Port=8761; URL="http://localhost:8761"},
    @{Name="Data Service"; Port=9090; URL="http://localhost:9090"},
    @{Name="Backend Service"; Port=8080; URL="http://localhost:8080"},
    @{Name="API Gateway"; Port=8765; URL="http://localhost:8765"},
    @{Name="UI Application"; Port=3001; URL="http://localhost:3001"}
)

foreach ($service in $services) {
    if (Test-Port $service.Port) {
        Write-Host "[OK] $($service.Name) - $($service.URL)" -ForegroundColor Green
    } else {
        Write-Host "[!!] $($service.Name) - NOT RESPONDING" -ForegroundColor Red
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Access URLs" -ForegroundColor White
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "Service Registry: " -NoNewline
Write-Host "http://localhost:8761" -ForegroundColor Yellow

Write-Host "API Gateway:      " -NoNewline
Write-Host "http://localhost:8765" -ForegroundColor Yellow

Write-Host "UI Application:   " -NoNewline
Write-Host "http://localhost:3001" -ForegroundColor Yellow

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Circuit Breaker Endpoints" -ForegroundColor White
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "Gateway Health:        http://localhost:8765/actuator/health" -ForegroundColor Gray
Write-Host "Circuit Breakers:      http://localhost:8765/actuator/circuitbreakers" -ForegroundColor Gray
Write-Host "Circuit Breaker Events: http://localhost:8765/actuator/circuitbreakerevents" -ForegroundColor Gray
Write-Host "Metrics:               http://localhost:8765/actuator/metrics" -ForegroundColor Gray

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   All services started successfully!" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan
