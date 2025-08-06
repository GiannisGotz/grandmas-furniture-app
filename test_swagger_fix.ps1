# Test script to verify Swagger UI server configuration fix

Write-Host "=== Testing Swagger UI Server Configuration ===" -ForegroundColor Green

# Step 1: Check if the app is running
Write-Host "1. Checking if app is running on port 8080..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/v3/api-docs" -Method GET
    Write-Host "✅ App is running and API docs are accessible" -ForegroundColor Green
} catch {
    Write-Host "❌ App is not running or not accessible: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Step 2: Check the OpenAPI spec for server configuration
Write-Host "`n2. Checking OpenAPI server configuration..." -ForegroundColor Yellow
try {
    $openApiSpec = Invoke-RestMethod -Uri "http://localhost:8080/v3/api-docs" -Method GET
    if ($openApiSpec.servers -and $openApiSpec.servers.Count -gt 0) {
        Write-Host "✅ Server configuration found:" -ForegroundColor Green
        foreach ($server in $openApiSpec.servers) {
            Write-Host "   URL: $($server.url)" -ForegroundColor Gray
            Write-Host "   Description: $($server.description)" -ForegroundColor Gray
        }
    } else {
        Write-Host "❌ No server configuration found in OpenAPI spec" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Failed to get OpenAPI spec: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test the specific endpoint that was failing in Swagger
Write-Host "`n3. Testing the search/paginated endpoint directly..." -ForegroundColor Yellow
try {
    # First login to get token
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"Admin1","password":"Cosmote1@"}'
    $token = $loginResponse.token
    Write-Host "✅ Login successful" -ForegroundColor Green
    
    # Test the endpoint with the same parameters from the Swagger image
    $testBody = @{
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
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $testBody
    
    Write-Host "✅ Search/paginated endpoint works correctly" -ForegroundColor Green
    Write-Host "   Response: Page=$($response.page), Size=$($response.size), Total=$($response.totalElements)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Search/paginated endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Swagger UI Configuration Test Complete ===" -ForegroundColor Green
Write-Host "`nNext steps:" -ForegroundColor Yellow
Write-Host "1. Open http://localhost:8080/swagger-ui in your browser" -ForegroundColor White
Write-Host "2. Try the /api/ads/search/paginated endpoint again" -ForegroundColor White
Write-Host "3. The 401 error should now be resolved" -ForegroundColor White 