# RealtyX Microservices - Quick Test Script

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Testing Microservices Architecture" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan

# Test 1: Check Service Registry
Write-Host "[Test 1] Checking Service Registry..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8761/actuator/health" -UseBasicParsing -TimeoutSec 5
    Write-Host "         [OK] Service Registry is healthy" -ForegroundColor Green
} catch {
    Write-Host "         [!!] Service Registry is not responding" -ForegroundColor Red
}

# Test 2: Check registered services
Write-Host "`n[Test 2] Checking registered services..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8761/eureka/apps" -UseBasicParsing -TimeoutSec 5
    if ($response.Content -like "*DATA-SERVICE*") {
        Write-Host "         [OK] Data Service is registered" -ForegroundColor Green
    }
    if ($response.Content -like "*BACKEND-SERVICE*") {
        Write-Host "         [OK] Backend Service is registered" -ForegroundColor Green
    }
    if ($response.Content -like "*API-GATEWAY*") {
        Write-Host "         [OK] API Gateway is registered" -ForegroundColor Green
    }
} catch {
    Write-Host "         [!!] Unable to fetch registered services" -ForegroundColor Red
}

# Test 3: Test API Gateway routing
Write-Host "`n[Test 3] Testing API Gateway routing..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8765/actuator/health" -UseBasicParsing -TimeoutSec 5
    Write-Host "         [OK] API Gateway is responding" -ForegroundColor Green
} catch {
    Write-Host "         [!!] API Gateway is not responding" -ForegroundColor Red
}

# Test 4: Test Data Service via Gateway
Write-Host "`n[Test 4] Testing Data Service via Gateway..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8765/data/properties" -UseBasicParsing -TimeoutSec 10
    $properties = ($response.Content | ConvertFrom-Json)
    Write-Host "         [OK] Retrieved $($properties.Count) properties" -ForegroundColor Green
} catch {
    Write-Host "         [!!] Data Service routing failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Test Backend Service via Gateway
Write-Host "`n[Test 5] Testing Backend Service via Gateway..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8765/api/properties" -UseBasicParsing -TimeoutSec 10
    $properties = ($response.Content | ConvertFrom-Json)
    Write-Host "         [OK] Retrieved $($properties.Count) properties" -ForegroundColor Green
} catch {
    Write-Host "         [!!] Backend Service routing failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Check Circuit Breaker status
Write-Host "`n[Test 6] Checking Circuit Breaker status..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8765/actuator/circuitbreakers" -UseBasicParsing -TimeoutSec 5
    $cbStatus = ($response.Content | ConvertFrom-Json)
    Write-Host "         [OK] Circuit Breakers are configured" -ForegroundColor Green
    Write-Host "         Circuit Breaker Names: $($cbStatus.circuitBreakers.name -join ', ')" -ForegroundColor Gray
} catch {
    Write-Host "         [!!] Unable to check Circuit Breaker status" -ForegroundColor Red
}

# Test 7: Test Circuit Breaker Fallback (simulate failure)
Write-Host "`n[Test 7] Testing Circuit Breaker Fallback..." -ForegroundColor Yellow
Write-Host "         (This test requires manually stopping a service)" -ForegroundColor Gray
Write-Host "         To test: Stop Data Service, then access http://localhost:8765/data/properties" -ForegroundColor Gray

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Testing Complete!" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan
