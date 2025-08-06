# Test script for search/paginated endpoint scenarios

Write-Host "=== Testing /api/ads/search/paginated Endpoint ===" -ForegroundColor Green

# Step 1: Login to get token
Write-Host "1. Logging in to get token..." -ForegroundColor Yellow
try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"Admin1","password":"Cosmote1@"}'
    $token = $loginResponse.token
    Write-Host "✅ Login successful. Token obtained." -ForegroundColor Green
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Step 2: Test with no filters (should return all ads with pagination)
Write-Host "`n2. Testing with no filters (empty body)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{}'
    Write-Host "✅ No filters successful" -ForegroundColor Green
    Write-Host "   Page: $($response.page), Size: $($response.size), Total: $($response.totalElements), Content: $($response.content.Count) items" -ForegroundColor Gray
} catch {
    Write-Host "❌ No filters failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test with title filter only
Write-Host "`n3. Testing with title filter..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"title":"chair"}'
    Write-Host "✅ Title filter successful" -ForegroundColor Green
    Write-Host "   Page: $($response.page), Size: $($response.size), Total: $($response.totalElements), Content: $($response.content.Count) items" -ForegroundColor Gray
} catch {
    Write-Host "❌ Title filter failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 4: Test with pagination parameters
Write-Host "`n4. Testing with explicit pagination..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"page":0,"pageSize":5}'
    Write-Host "✅ Explicit pagination successful" -ForegroundColor Green
    Write-Host "   Page: $($response.page), Size: $($response.size), Total: $($response.totalElements), Content: $($response.content.Count) items" -ForegroundColor Gray
} catch {
    Write-Host "❌ Explicit pagination failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 5: Test with sorting
Write-Host "`n5. Testing with sorting..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"sortBy":"title","sortDirection":"ASC"}'
    Write-Host "✅ Sorting successful" -ForegroundColor Green
    Write-Host "   Page: $($response.page), Size: $($response.size), Total: $($response.totalElements), Content: $($response.content.Count) items" -ForegroundColor Gray
} catch {
    Write-Host "❌ Sorting failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 6: Test with complex filters
Write-Host "`n6. Testing with complex filters..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"title":"chair","page":0,"pageSize":3,"sortBy":"price","sortDirection":"DESC"}'
    Write-Host "✅ Complex filters successful" -ForegroundColor Green
    Write-Host "   Page: $($response.page), Size: $($response.size), Total: $($response.totalElements), Content: $($response.content.Count) items" -ForegroundColor Gray
} catch {
    Write-Host "❌ Complex filters failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 7: Test with invalid page size (should use default)
Write-Host "`n7. Testing with invalid page size (0)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"pageSize":0}'
    Write-Host "✅ Invalid page size handled correctly" -ForegroundColor Green
    Write-Host "   Page: $($response.page), Size: $($response.size), Total: $($response.totalElements), Content: $($response.content.Count) items" -ForegroundColor Gray
} catch {
    Write-Host "❌ Invalid page size failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 8: Test with negative page size (should use default)
Write-Host "`n8. Testing with negative page size..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"pageSize":-5}'
    Write-Host "✅ Negative page size handled correctly" -ForegroundColor Green
    Write-Host "   Page: $($response.page), Size: $($response.size), Total: $($response.totalElements), Content: $($response.content.Count) items" -ForegroundColor Gray
} catch {
    Write-Host "❌ Negative page size failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 9: Test with negative page number (should use 0)
Write-Host "`n9. Testing with negative page number..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
        "Authorization"="Bearer $token"
        "Content-Type"="application/json"
    } -Body '{"page":-2}'
    Write-Host "✅ Negative page number handled correctly" -ForegroundColor Green
    Write-Host "   Page: $($response.page), Size: $($response.size), Total: $($response.totalElements), Content: $($response.content.Count) items" -ForegroundColor Gray
} catch {
    Write-Host "❌ Negative page number failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Search/Paginated Testing Complete ===" -ForegroundColor Green 