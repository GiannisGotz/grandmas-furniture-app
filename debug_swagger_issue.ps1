# Debug script to compare working vs failing requests

Write-Host "=== Debugging Swagger UI Issue ===" -ForegroundColor Green

# Step 1: Login and get token
Write-Host "1. Logging in..." -ForegroundColor Yellow
try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"Admin1","password":"Cosmote1@"}'
    $token = $loginResponse.token
    Write-Host "✅ Login successful. Token: $($token.Substring(0,50))..." -ForegroundColor Green
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Step 2: Test with working request (simple body)
Write-Host "`n2. Testing with working request (simple body)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"page":0,"pageSize":5}'
    Write-Host "✅ Working request successful" -ForegroundColor Green
    Write-Host "   Response: Page=$($response.page), Size=$($response.size), Total=$($response.totalElements)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Working request failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test with complex body (like Swagger)
Write-Host "`n3. Testing with complex body (like Swagger)..." -ForegroundColor Yellow
try {
    $complexBody = @{
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
    
    Write-Host "Complex body JSON: $complexBody" -ForegroundColor Gray
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $complexBody
    Write-Host "✅ Complex request successful" -ForegroundColor Green
    Write-Host "   Response: Page=$($response.page), Size=$($response.size), Total=$($response.totalElements)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Complex request failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $errorBody = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($errorBody)
        $errorContent = $reader.ReadToEnd()
        Write-Host "   Error Body: $errorContent" -ForegroundColor Red
    }
}

# Step 4: Test with minimal complex body
Write-Host "`n4. Testing with minimal complex body..." -ForegroundColor Yellow
try {
    $minimalBody = @{
        page = 0
        pageSize = 0
        sortDirection = "ASC"
        sortBy = "id"
    } | ConvertTo-Json
    
    Write-Host "Minimal body JSON: $minimalBody" -ForegroundColor Gray
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body $minimalBody
    Write-Host "✅ Minimal complex request successful" -ForegroundColor Green
    Write-Host "   Response: Page=$($response.page), Size=$($response.size), Total=$($response.totalElements)" -ForegroundColor Gray
} catch {
    Write-Host "❌ Minimal complex request failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Debug Complete ===" -ForegroundColor Green 