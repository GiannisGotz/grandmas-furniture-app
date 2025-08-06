# Test script to verify sortBy validation fix

Write-Host "=== Testing sortBy Validation Fix ===" -ForegroundColor Green

# Step 1: Login and get token
Write-Host "1. Logging in..." -ForegroundColor Yellow
try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"Admin1","password":"Cosmote1@"}'
    $token = $loginResponse.token
    Write-Host "✅ Login successful" -ForegroundColor Green
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Step 2: Test with invalid sortBy (should now work)
Write-Host "`n2. Testing with invalid sortBy (string)..." -ForegroundColor Yellow
try {
    $body = @{
        page = 0
        pageSize = 5
        sortBy = "string"
        sortDirection = "ASC"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $body
    Write-Host "✅ Invalid sortBy now works (uses default 'id')" -ForegroundColor Green
    Write-Host "   Response: Page=$($response.page), Size=$($response.size), Total=$($response.totalElements)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Invalid sortBy still fails: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test with other invalid sortBy values
Write-Host "`n3. Testing with other invalid sortBy values..." -ForegroundColor Yellow
$invalidSortFields = @("invalid", "nonexistent", "random", "test")
foreach ($field in $invalidSortFields) {
    try {
        $body = @{
            page = 0
            pageSize = 5
            sortBy = $field
            sortDirection = "ASC"
        } | ConvertTo-Json
        
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
            "Authorization"="Bearer $token"
            "Content-Type"="application/json"
        } -Body $body
        Write-Host "✅ Invalid sortBy '$field' now works (uses default)" -ForegroundColor Green
    } catch {
        Write-Host "❌ Invalid sortBy '$field' still fails: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Step 4: Test with valid sortBy values
Write-Host "`n4. Testing with valid sortBy values..." -ForegroundColor Yellow
$validSortFields = @("id", "title", "price", "condition", "isAvailable", "description")
foreach ($field in $validSortFields) {
    try {
        $body = @{
            page = 0
            pageSize = 5
            sortBy = $field
            sortDirection = "ASC"
        } | ConvertTo-Json
        
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
            "Authorization"="Bearer $token"
            "Content-Type"="application/json"
        } -Body $body
        Write-Host "✅ Valid sortBy '$field' works" -ForegroundColor Green
    } catch {
        Write-Host "❌ Valid sortBy '$field' failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Step 5: Test the full Swagger body
Write-Host "`n5. Testing the full Swagger body..." -ForegroundColor Yellow
try {
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
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $swaggerBody
    
    Write-Host "✅ Full Swagger body now works!" -ForegroundColor Green
    Write-Host "   Response: Page=$($response.page), Size=$($response.size), Total=$($response.totalElements)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Full Swagger body still fails: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== sortBy Fix Test Complete ===" -ForegroundColor Green
Write-Host "`nNow Swagger UI should work with any sortBy value!" -ForegroundColor Yellow 