# Grandma's Furniture App - Backend

A Spring Boot REST API for buying and selling antique furniture. Users can post ads with images, search for furniture, and manage their listings with advanced filtering and pagination.

## 🚀 Features

- **User Authentication**: JWT-based authentication with role-based access control
- **Ad Management**: Create, update, delete furniture advertisements with image support
- **Advanced Search**: Unified search with filters for title, category, price, location, condition, and availability
- **Smart Filtering**: Support for "My Items" vs "All Items" with automatic user context
- **Image Upload**: Secure file handling with validation and storage
- **Pagination**: Efficient data retrieval with customizable page sizes and sorting
- **API Documentation**: Comprehensive OpenAPI/Swagger documentation
- **Role-based Security**: Admin and User roles with appropriate permissions

## 🗄️ Database Setup

### SQL Schema File
The project includes a complete database setup script:
```
src/main/resources/mysql/schema query.sql
```

This file provides:
- **Complete database schema** with all tables and relationships
- **Sample data** including categories, cities, and furniture ads  
- **Test users** with predefined credentials (admin1/user1: `Cosmote1@`)
- **Proper indexing** for optimal search performance
- **Database user creation** with appropriate permissions

### Database Features
- **MySQL 8.0+** compatibility with UTF-8 support
- **Optimized indexes** for search and filtering operations
- **Sample furniture data** (100+ items) for testing
- **Pre-configured categories** (12 furniture types)
- **Greek cities** (50+ locations) for realistic testing

## 🛠️ Technology Stack

- **Java 17** with Spring Boot 3.4.6
- **Spring Security** with JWT authentication and CORS support
- **Spring Data JPA** with MySQL database and advanced specifications
- **Lombok** for boilerplate reduction and cleaner code
- **OpenAPI 3** for comprehensive API documentation
- **Gradle** for build management and dependency handling
- **MySQL 8.0** for persistent data storage

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Gradle 7.0 or higher
- At least 2GB RAM for development

## 🔧 Setup & Installation

### 1. Clone and Setup
```bash
git clone <repository-url>
cd grandmas-furniture-app-backend
```

### 2. Database Configuration

#### Option 1: Quick Setup with SQL File (Recommended)
```bash
# Connect to MySQL
mysql -u root -p

# Run the complete database setup script
source src/main/resources/mysql/schema\ query.sql;
```

#### Option 2: Manual Setup
```bash
# Create MySQL database
mysql -u root -p
CREATE DATABASE grandmas_furniture_app;
CREATE USER 'furniture_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON grandmas_furniture_app.* TO 'furniture_user'@'localhost';
FLUSH PRIVILEGES;
```

**Note:** The SQL file automatically creates:
- Database: `grandmasfurnitureappdb`
- User: `Giannis` with password `12345`
- All tables with proper structure and indexes
- Sample categories and cities
- Test users (including admin1/user1 with password `Cosmote1@`)
- Sample furniture ads (100 items)

### 3. Application Properties
Update `src/main/resources/application.properties`:
```properties
# Database Configuration
# If using SQL file setup (recommended):
spring.datasource.url=jdbc:mysql://localhost:3306/grandmasfurnitureappdb
spring.datasource.username=Giannis
spring.datasource.password=12345

# If using manual setup:
# spring.datasource.url=jdbc:mysql://localhost:3306/grandmas_furniture_app
# spring.datasource.username=furniture_user
# spring.datasource.password=your_password

# JWT Configuration
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 4. Run the Application
```bash
# Development mode
./gradlew bootRun

# Or build and run
./gradlew build
java -jar build/libs/grandmasfurnitureapp-0.0.1-SNAPSHOT.jar
```

### 5. Access Points
- **API Base URL**: `http://localhost:8080/api`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`

## 📚 API Endpoints

### 🔐 Authentication
- `POST /api/auth/login` - User login with JWT response
- `POST /api/auth/register` - User registration

### 🪑 Ads Management
- `GET /api/ads` - Get paginated ads with sorting
- `GET /api/ads/{id}` - Get ad by ID with full details
- `POST /api/ads/save` - Create new ad with multipart image upload
- `PUT /api/ads/{id}` - Update existing ad with image support
- `DELETE /api/ads/{id}` - Delete ad
- `GET /api/ads/available` - Get all available ads
- `GET /api/ads/my-ads` - Get current user's ads

### 🔍 Advanced Search
- `POST /api/ads/search` - Search ads with filters (no pagination)
- `POST /api/ads/search/paginated` - **Unified search with pagination and all filters**

### 👥 User Management
- `GET /api/users` - Get paginated users 
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}/role` - Update user role

## 🔍 Search & Filtering

### Unified Search Endpoint
The `/api/ads/search/paginated` endpoint provides comprehensive filtering:

```json
{
  "title": "vintage chair",
  "categoryName": "Chairs",
  "cityName": "Athens",
  "condition": "GOOD",
  "minPrice": 50,
  "maxPrice": 500,
  "isAvailable": true,
  "myAds": false,
  "page": 0,
  "pageSize": 10,
  "sortBy": "id",
  "sortDirection": "asc"
}
```

### Filter Options
- **Text Search**: `title`, `categoryName`, `cityName`
- **Condition Filter**: `condition` (EXCELLENT, GOOD, AGE_WORN, DAMAGED)
- **Price Range**: `minPrice`, `maxPrice`
- **Availability**: `isAvailable` (true/false)
- **User Context**: `myAds` (true for current user's ads only)
- **Pagination**: `page`, `pageSize`
- **Sorting**: `sortBy`, `sortDirection`

### Smart Context Handling
- **"All Items" Mode**: Shows all marketplace ads
- **"My Items" Mode**: Automatically filters by authenticated user
- **Availability Filter**: Optional filter for available/unavailable items

### Available Condition Values
The `condition` filter accepts these enum values:
- **EXCELLENT**: Like new condition
- **GOOD**: Good used condition
- **AGE_WORN**: Shows signs of age but functional
- **DAMAGED**: Has visible damage or defects

## 🔐 Security Features

### Authentication
- JWT token-based authentication
- Secure HTTP-only cookie storage
- Automatic token validation and refresh

### Authorization
- **USER Role**: Can manage own ads and browse marketplace
- **ADMIN Role**: Full system access including user management
- Route-level security with Spring Security

### Data Protection
- Password encryption with BCrypt
- CORS configuration for frontend integration
- Input validation and sanitization
- SQL injection prevention with JPA

## 📁 Project Architecture

```
src/main/java/gr/aueb/cf/grandmasfurnitureapp/
├── authentication/           # JWT authentication services
│   ├── AuthenticationService.java
│   ├── CustomUserDetailsService.java
│   └── JwtAuthenticationFilter.java
├── config/                  # Application configuration
│   ├── OpenApiConfig.java
│   └── WebConfig.java
├── core/                    # Core components
│   ├── enums/              # Enums (Condition, Role)
│   ├── exceptions/          # Custom exception classes
│   ├── filters/             # Search filters and pagination
│   └── specifications/      # JPA specifications for filtering
├── dto/                     # Data Transfer Objects
├── mapper/                  # Entity-DTO mapping
├── model/                   # JPA entities
│   └── static_data/         # Static data (Category, City)
├── repository/              # Data access layer
├── rest/                    # REST controllers
├── security/                # Security configuration
└── service/                 # Business logic services
```

## 🧪 Testing

### Run Tests
```bash
# All tests
./gradlew test

# Specific test class
./gradlew test --tests *AdServiceTest

# Test with coverage
./gradlew test jacocoTestReport
```


## 🚀 Performance Features

### Database Optimization
- JPA Specifications for dynamic query building
- Efficient pagination with Spring Data
- Optimized joins and indexing

### Search Performance
- Server-side filtering for large datasets
- Pagination to limit result sizes
- Efficient sorting and indexing

### File Handling
- Multipart file upload support
- Image validation and processing
- Secure file storage

## 🔧 Development

### Building
```bash
# Clean build
./gradlew clean build

# Skip tests
./gradlew build -x test

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Debugging
```bash
# Enable debug logging
./gradlew bootRun --args='--logging.level.gr.aueb.cf=DEBUG'

# Remote debugging
./gradlew bootRun --args='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005'
```

### Logging
- Structured logging with SLF4J
- Configurable log levels
- Log file rotation and management

## 🐛 Troubleshooting

### Common Issues

**Database Connection Failed**
- Verify MySQL service is running
- Check database credentials in `application.properties`
- **Quick fix**: Use the SQL file for automatic database setup: `src/main/resources/mysql/schema query.sql`
- Ensure database exists and user has permissions

**JWT Authentication Issues**
- Verify JWT secret in properties
- Check token expiration settings
- Clear browser cookies if testing

**File Upload Problems**
- Check file size limits in properties
- Verify upload directory permissions
- Ensure proper multipart configuration

**Search Not Working**
- Check database indexes on search fields
- Verify specification implementations
- Test with simple filters first

## 📝 Recent Improvements

### v2.0 - Unified Search Architecture
- **Consolidated Search**: Single endpoint for all search operations
- **Smart Filtering**: Automatic user context for "My Items" vs "All Items"
- **Better Performance**: Server-side filtering with JPA specifications
- **Cleaner Code**: Removed duplicate specifications and simplified logic
- **Enhanced UX**: Users can see all ads with clear availability indicators

## 📄 License

This project is licensed under the MIT License.


