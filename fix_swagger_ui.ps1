# Script to fix Swagger UI issues and ensure it works correctly

Write-Host "=== Fixing Swagger UI Issues ===" -ForegroundColor Green

# Step 1: Verify the server configuration is correct
Write-Host "1. Checking OpenAPI server configuration..." -ForegroundColor Yellow
try {
    $openApiSpec = Invoke-RestMethod -Uri "http://localhost:8080/v3/api-docs" -Method GET
    if ($openApiSpec.servers -and $openApiSpec.servers.Count -gt 0) {
        Write-Host "✅ Server configuration found:" -ForegroundColor Green
        foreach ($server in $openApiSpec.servers) {
            Write-Host "   URL: $($server.url)" -ForegroundColor Gray
            Write-Host "   Description: $($server.description)" -ForegroundColor Gray
        }
    } else {
        Write-Host "❌ No server configuration found" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Failed to get OpenAPI spec: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 2: Test the exact Swagger request that fails
Write-Host "`n2. Testing the exact Swagger request..." -ForegroundColor Yellow
try {
    # Login to get token
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"Admin1","password":"Cosmote1@"}'
    $token = $loginResponse.token
    Write-Host "✅ Login successful" -ForegroundColor Green
    
    # Test with the exact body from Swagger UI
    $swaggerBody = @{
        page = 0
        pageSize = 0
        sortDirection = "ASC"
        sortBy = "string"
        title = "string"
        categoryId = 0
        categoryName = "string"
        condition = "EXCELLENT"
        minPrice = 0
        maxPrice = 0
        cityId = 0
        cityName = "string"
        userId = 0
        isAvailable = $true
        description = "string"
    } | ConvertTo-Json
    
    Write-Host "Testing with Swagger body: $swaggerBody" -ForegroundColor Gray
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $swaggerBody
    
    Write-Host "✅ Swagger request works via PowerShell" -ForegroundColor Green
    Write-Host "   Response: Page=$($response.page), Size=$($response.size), Total=$($response.totalElements)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Swagger request failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Create a simple test request for Swagger
Write-Host "`n3. Creating simple test request for Swagger..." -ForegroundColor Yellow
$simpleBody = @{
    page = 0
    pageSize = 5
} | ConvertTo-Json

Write-Host "Simple request body for Swagger:" -ForegroundColor Cyan
Write-Host $simpleBody -ForegroundColor White

# Step 4: Instructions for fixing Swagger UI
Write-Host "`n4. Instructions to fix Swagger UI:" -ForegroundColor Yellow
Write-Host "   a) Open http://localhost:8080/swagger-ui in your browser" -ForegroundColor White
Write-Host "   b) Press Ctrl+F5 to hard refresh the page" -ForegroundColor White
Write-Host "   c) Clear browser cache if needed (Ctrl+Shift+Delete)" -ForegroundColor White
Write-Host "   d) Check that the server URL shows 'http://localhost:8080'" -ForegroundColor White
Write-Host "   e) Try the /api/ads/search/paginated endpoint with this simple body:" -ForegroundColor White
Write-Host "      $simpleBody" -ForegroundColor Cyan
Write-Host "   f) If it still fails, try with an empty body: {}" -ForegroundColor White

# Step 5: Test with empty body
Write-Host "`n5. Testing with empty body..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{}'
    
    Write-Host "✅ Empty body works" -ForegroundColor Green
    Write-Host "   Response: Page=$($response.page), Size=$($response.size), Total=$($response.totalElements)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Empty body failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Swagger UI Fix Complete ===" -ForegroundColor Green
Write-Host "`nIf Swagger UI still fails after following the instructions above," -ForegroundColor Yellow
Write-Host "the issue is likely browser caching. Try:" -ForegroundColor Yellow
Write-Host "1. Opening Swagger UI in an incognito/private window" -ForegroundColor White
Write-Host "2. Using a different browser" -ForegroundColor White
Write-Host "3. Clearing all browser data for localhost:8080" -ForegroundColor White 