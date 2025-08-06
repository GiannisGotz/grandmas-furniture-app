# Test script for search endpoints investigation

Write-Host "=== Testing Search Endpoints ===" -ForegroundColor Green

# Step 1: Login to get token
Write-Host "1. Logging in to get token..." -ForegroundColor Yellow
try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"Admin1","password":"Cosmote1@"}'
    $token = $loginResponse.token
    Write-Host "✅ Login successful. Token obtained." -ForegroundColor Green
    Write-Host "Token: $($token.Substring(0, 50))..." -ForegroundColor Gray
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Step 2: Test /api/ads/search endpoint
Write-Host "`n2. Testing /api/ads/search endpoint..." -ForegroundColor Yellow
try {
    $searchResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"title":"chair"}'
    Write-Host "✅ /api/ads/search successful" -ForegroundColor Green
    Write-Host "Response count: $($searchResponse.Count)" -ForegroundColor Gray
} catch {
    Write-Host "❌ /api/ads/search failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody" -ForegroundColor Red
    }
}

# Step 3: Test /api/ads/search/paginated endpoint
Write-Host "`n3. Testing /api/ads/search/paginated endpoint..." -ForegroundColor Yellow
try {
    $searchPaginatedResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"title":"chair"}'
    Write-Host "✅ /api/ads/search/paginated successful" -ForegroundColor Green
    Write-Host "Response data count: $($searchPaginatedResponse.data.Count)" -ForegroundColor Gray
} catch {
    Write-Host "❌ /api/ads/search/paginated failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody" -ForegroundColor Red
    }
}

# Step 4: Test a working endpoint for comparison
Write-Host "`n4. Testing working endpoint /api/ads/available for comparison..." -ForegroundColor Yellow
try {
    $availableResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/available" -Method GET -Headers @{
        "Authorization"="Bearer $token"
    }
    Write-Host "✅ /api/ads/available successful" -ForegroundColor Green
    Write-Host "Response count: $($availableResponse.Count)" -ForegroundColor Gray
} catch {
    Write-Host "❌ /api/ads/available failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Investigation Complete ===" -ForegroundColor Green 