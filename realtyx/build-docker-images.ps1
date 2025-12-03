Write-Host "================================================" -ForegroundColor Cyan
Write-Host "   BUILDING DOCKER IMAGES FOR REALTYX" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$images = @(
    @{Name="Service Registry"; Path="service-registry"; Tag="realtyx-service-registry"},
    @{Name="Data Service"; Path="data-service"; Tag="realtyx-data-service"},
    @{Name="Backend Service"; Path="backend"; Tag="realtyx-backend"},
    @{Name="API Gateway"; Path="api-gateway"; Tag="realtyx-api-gateway"},
    @{Name="UI Application"; Path="ui"; Tag="realtyx-ui"}
)

$successCount = 0
$failedCount = 0

foreach ($image in $images) {
    Write-Host "================================================" -ForegroundColor Yellow
    Write-Host "Building: $($image.Name)" -ForegroundColor Yellow
    Write-Host "================================================" -ForegroundColor Yellow
    
    $imagePath = Join-Path $PSScriptRoot $image.Path
    
    if (Test-Path $imagePath) {
        try {
            Set-Location $imagePath
            
            Write-Host "Building Docker image: $($image.Tag):latest" -ForegroundColor Cyan
            docker build -t "$($image.Tag):latest" .
            
            if ($LASTEXITCODE -eq 0) {
                Write-Host "Successfully built: $($image.Tag):latest" -ForegroundColor Green
                $successCount++
            } else {
                Write-Host "Failed to build: $($image.Tag):latest" -ForegroundColor Red
                $failedCount++
            }
        } catch {
            Write-Host "Error building $($image.Name): $_" -ForegroundColor Red
            $failedCount++
        }
        
        Write-Host ""
    } else {
        Write-Host "Directory not found: $imagePath" -ForegroundColor Red
        $failedCount++
        Write-Host ""
    }
}

# Return to script directory
Set-Location $PSScriptRoot

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "   BUILD SUMMARY" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "Successful builds: $successCount" -ForegroundColor Green
Write-Host "Failed builds: $failedCount" -ForegroundColor Red
Write-Host ""

if ($successCount -eq $images.Count) {
    Write-Host "All images built successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "To view images run: docker images | findstr realtyx" -ForegroundColor Cyan
    Write-Host "To start services run: docker-compose up -d" -ForegroundColor Cyan
} else {
    Write-Host "Some builds failed. Please check the errors above." -ForegroundColor Yellow
}

Write-Host "================================================" -ForegroundColor Cyan
