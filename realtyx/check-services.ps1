Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "   MICROSERVICES STATUS CHECK" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

$services = @(
    @{Port=8761; Name="Service Registry (Eureka)"; URL="http://localhost:8761"},
    @{Port=9090; Name="Data Service"; URL="http://localhost:9090/actuator/health"},
    @{Port=8080; Name="Backend Service"; URL="http://localhost:8080/actuator/health"},
    @{Port=8765; Name="API Gateway"; URL="http://localhost:8765/actuator/health"}
)

foreach ($service in $services) {
    $result = Test-NetConnection -ComputerName localhost -Port $service.Port -WarningAction SilentlyContinue -InformationLevel Quiet
    
    if ($result) {
        Write-Host "✓" -ForegroundColor Green -NoNewline
        Write-Host " $($service.Name) is " -NoNewline
        Write-Host "RUNNING" -ForegroundColor Green -NoNewline
        Write-Host " on port $($service.Port)"
    } else {
        Write-Host "✗" -ForegroundColor Red -NoNewline
        Write-Host " $($service.Name) is " -NoNewline
        Write-Host "NOT RUNNING" -ForegroundColor Red -NoNewline
        Write-Host " on port $($service.Port)"
    }
}

Write-Host ""
Write-Host "-----------------------------------------------" -ForegroundColor Cyan
Write-Host "Checking Service Registration in Eureka..." -ForegroundColor Yellow
Write-Host ""

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8761/eureka/apps" -Headers @{"Accept"="application/json"} -ErrorAction Stop
    
    if ($response.applications.application) {
        $apps = $response.applications.application
        if ($apps -is [System.Array]) {
            Write-Host "Registered Services: $($apps.Count)" -ForegroundColor Green
            foreach ($app in $apps) {
                Write-Host "  • $($app.name)" -ForegroundColor Cyan
            }
        } else {
            Write-Host "Registered Services: 1" -ForegroundColor Green
            Write-Host "  • $($apps.name)" -ForegroundColor Cyan
        }
    } else {
        Write-Host "⚠ No services registered yet" -ForegroundColor Yellow
        Write-Host "  Services may still be starting up..." -ForegroundColor Gray
    }
} catch {
    Write-Host "✗ Cannot connect to Eureka Server" -ForegroundColor Red
}

Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "Access URLs:" -ForegroundColor White
Write-Host "  • Eureka Dashboard: http://localhost:8761" -ForegroundColor Gray
Write-Host "  • API Gateway: http://localhost:8765" -ForegroundColor Gray
Write-Host "  • Backend Direct: http://localhost:8080" -ForegroundColor Gray
Write-Host "  • Data Service: http://localhost:9090" -ForegroundColor Gray
Write-Host "===============================================" -ForegroundColor Cyan
