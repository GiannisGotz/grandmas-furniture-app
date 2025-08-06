# Debug script to understand why complex request body causes 401

Write-Host "=== Debugging Complex Request Body Issue ===" -ForegroundColor Green

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

# Step 2: Test with minimal fields one by one
Write-Host "`n2. Testing with minimal fields..." -ForegroundColor Yellow

$testCases = @(
    @{ name = "Empty body"; body = '{}' },
    @{ name = "Only page"; body = '{"page":0}' },
    @{ name = "Only pageSize"; body = '{"pageSize":0}' },
    @{ name = "Page + pageSize"; body = '{"page":0,"pageSize":0}' },
    @{ name = "With sortBy"; body = '{"page":0,"pageSize":0,"sortBy":"string"}' },
    @{ name = "With title"; body = '{"page":0,"pageSize":0,"title":"string"}' },
    @{ name = "With condition"; body = '{"page":0,"pageSize":0,"condition":"EXCELLENT"}' },
    @{ name = "With isAvailable"; body = '{"page":0,"pageSize":0,"isAvailable":true}' },
    @{ name = "With numeric fields"; body = '{"page":0,"pageSize":0,"categoryId":0,"cityId":0,"userId":0,"minPrice":0,"maxPrice":0}' },
    @{ name = "With string fields"; body = '{"page":0,"pageSize":0,"title":"string","categoryName":"string","cityName":"string","description":"string"}' },
    @{ name = "Full complex body"; body = '{"page":0,"pageSize":0,"sortDirection":"ASC","sortBy":"string","title":"string","categoryId":0,"categoryName":"string","condition":"EXCELLENT","minPrice":0,"maxPrice":0,"cityId":0,"cityName":"string","userId":0,"isAvailable":true,"description":"string"}' }
)

foreach ($testCase in $testCases) {
    try {
        Write-Host "Testing: $($testCase.name)" -ForegroundColor Gray
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
            "Authorization"="Bearer $token"
            "Content-Type"="application/json"
        } -Body $testCase.body
        
        Write-Host "✅ $($testCase.name) works" -ForegroundColor Green
    } catch {
        Write-Host "❌ $($testCase.name) failed: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            Write-Host "   Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
        }
    }
}

# Step 3: Test with different Content-Type headers
Write-Host "`n3. Testing with different Content-Type headers..." -ForegroundColor Yellow
$complexBody = '{"page":0,"pageSize":0,"sortDirection":"ASC","sortBy":"string","title":"string","categoryId":0,"categoryName":"string","condition":"EXCELLENT","minPrice":0,"maxPrice":0,"cityId":0,"cityName":"string","userId":0,"isAvailable":true,"description":"string"}'

$contentTypes = @(
    "application/json",
    "application/json; charset=utf-8",
    "application/json; charset=UTF-8"
)

foreach ($contentType in $contentTypes) {
    try {
        Write-Host "Testing with Content-Type: $contentType" -ForegroundColor Gray
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/ads/search/paginated" -Method POST -Headers @{
            "Authorization"="Bearer $token"
            "Content-Type"=$contentType
        } -Body $complexBody
        
        Write-Host "✅ Content-Type $contentType works" -ForegroundColor Green
    } catch {
        Write-Host "❌ Content-Type $contentType failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n=== Debug Complete ===" -ForegroundColor Green 