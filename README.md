# Grandma's Furniture App

A Spring Boot REST API for buying and selling antique furniture. Users can post ads with images, search for furniture, and manage their listings.

## 🚀 Features

- **User Authentication**: JWT-based authentication with role-based access
- **Ad Management**: Create, update, delete furniture advertisements
- **Image Upload**: Support for furniture image attachments
- **Advanced Search**: Filter ads by category, price, location, and condition
- **Pagination**: Efficient data retrieval with pagination support
- **API Documentation**: OpenAPI/Swagger documentation

## 🛠️ Technology Stack

- **Java 17** with Spring Boot 3.4.6
- **Spring Security** with JWT authentication
- **Spring Data JPA** with MySQL database
- **Lombok** for boilerplate reduction
- **OpenAPI** for API documentation
- **Gradle** for build management

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Gradle 7.0 or higher

## 🔧 Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd grandmasfurnitureapp
   ```

2. **Configure database**
   - Create MySQL database: `grandmas_furniture_app`
   - Update `application.properties` with your database credentials

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

4. **Access the API**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui`

## 📚 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Ads Management
- `GET /api/ads` - Get paginated ads
- `GET /api/ads/{id}` - Get ad by ID
- `POST /api/ads/save` - Create new ad with image
- `PUT /api/ads/{id}` - Update ad
- `DELETE /api/ads/{id}` - Delete ad
- `POST /api/ads/search` - Search ads with filters
- `GET /api/ads/my-ads` - Get current user's ads

### User Management
- `GET /api/users` - Get paginated users
- `GET /api/users/{id}` - Get user by ID

## 🔐 Security

- JWT token-based authentication
- Role-based authorization (USER, ADMIN)
- Password encryption with BCrypt
- CORS configuration for frontend integration

## 📁 Project Structure

```
src/main/java/gr/aueb/cf/grandmasfurnitureapp/
├── authentication/     # JWT authentication services
├── config/           # Application configuration
├── core/             # Core components (exceptions, filters, enums)
├── dto/              # Data Transfer Objects
├── mapper/           # Entity-DTO mapping
├── model/            # JPA entities
├── repository/       # Data access layer
├── rest/             # REST controllers
├── security/         # Security configuration
└── service/          # Business logic services
```

## 🧪 Testing

Run tests with:
```bash
./gradlew test
```

## 📝 License

This project is licensed under the MIT License.
