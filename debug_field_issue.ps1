# Debug script to identify which field causes the 401 error

Write-Host "=== Debugging Field-Specific Issues ===" -ForegroundColor Green

# Step 1: Login and get token
Write-Host "1. Logging in..." -ForegroundColor Yellow
try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"Admin1","password":"Cosmote1@"}'
    $token = $loginResponse.token
    Write-Host "✅ Login successful." -ForegroundColor Green
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Step 2: Test with condition field
Write-Host "`n2. Testing with condition field..." -ForegroundColor Yellow
try {
    $body = @{
        page = 0
        pageSize = 5
        condition = "EXCELLENT"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $body
    Write-Host "✅ Condition field works" -ForegroundColor Green
} catch {
    Write-Host "❌ Condition field failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test with isAvailable field
Write-Host "`n3. Testing with isAvailable field..." -ForegroundColor Yellow
try {
    $body = @{
        page = 0
        pageSize = 5
        isAvailable = $true
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $body
    Write-Host "✅ isAvailable field works" -ForegroundColor Green
} catch {
    Write-Host "❌ isAvailable field failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 4: Test with numeric fields
Write-Host "`n4. Testing with numeric fields..." -ForegroundColor Yellow
try {
    $body = @{
        page = 0
        pageSize = 5
        categoryId = 0
        cityId = 0
        userId = 0
        minPrice = 0
        maxPrice = 0
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $body
    Write-Host "✅ Numeric fields work" -ForegroundColor Green
} catch {
    Write-Host "❌ Numeric fields failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 5: Test with string fields
Write-Host "`n5. Testing with string fields..." -ForegroundColor Yellow
try {
    $body = @{
        page = 0
        pageSize = 5
        title = "string"
        categoryName = "string"
        cityName = "string"
        description = "string"
        sortBy = "string"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $body
    Write-Host "✅ String fields work" -ForegroundColor Green
} catch {
    Write-Host "❌ String fields failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 6: Test the exact combination from Swagger
Write-Host "`n6. Testing exact Swagger combination..." -ForegroundColor Yellow
try {
    $body = @{
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
    
    Write-Host "Testing body: $body" -ForegroundColor Gray
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $body
    Write-Host "✅ Exact Swagger combination works" -ForegroundColor Green
} catch {
    Write-Host "❌ Exact Swagger combination failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

Write-Host "`n=== Field Debug Complete ===" -ForegroundColor Green 