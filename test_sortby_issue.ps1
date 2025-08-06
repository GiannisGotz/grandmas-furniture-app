# Test script to verify sortBy field issue

Write-Host "=== Testing sortBy Field Issue ===" -ForegroundColor Green

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

# Step 2: Test with valid sortBy
Write-Host "`n2. Testing with valid sortBy (id)..." -ForegroundColor Yellow
try {
    $body = @{
        page = 0
        pageSize = 5
        sortBy = "id"
        sortDirection = "ASC"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $body
    Write-Host "✅ Valid sortBy works" -ForegroundColor Green
} catch {
    Write-Host "❌ Valid sortBy failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test with invalid sortBy
Write-Host "`n3. Testing with invalid sortBy (string)..." -ForegroundColor Yellow
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
    Write-Host "✅ Invalid sortBy works (unexpected)" -ForegroundColor Green
} catch {
    Write-Host "❌ Invalid sortBy failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 4: Test with other valid sortBy values
Write-Host "`n4. Testing with other valid sortBy values..." -ForegroundColor Yellow
$validSortFields = @("title", "price", "createdAt", "updatedAt")
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
        Write-Host "✅ sortBy '$field' works" -ForegroundColor Green
    } catch {
        Write-Host "❌ sortBy '$field' failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Step 5: Test with string fields but valid sortBy
Write-Host "`n5. Testing with string fields but valid sortBy..." -ForegroundColor Yellow
try {
    $body = @{
        page = 0
        pageSize = 5
        sortBy = "id"
        sortDirection = "ASC"
        title = "string"
        categoryName = "string"
        cityName = "string"
        description = "string"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $body
    Write-Host "✅ String fields with valid sortBy work" -ForegroundColor Green
} catch {
    Write-Host "❌ String fields with valid sortBy failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== sortBy Test Complete ===" -ForegroundColor Green 