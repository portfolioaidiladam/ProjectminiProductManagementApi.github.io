# Product Management API Documentation

## Table of Contents
- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
    - [Authentication](#authentication)
    - [Product Management](#product-management)
- [Request & Response Examples](#request--response-examples)
- [Error Handling](#error-handling)
- [Database Schema](#database-schema)
- [Security](#security)
- [Testing](#testing)

## Overview

Product Management API is a RESTful web service that provides comprehensive product management functionality with JWT-based authentication. This API allows users to register, authenticate, and perform CRUD operations on products with search capabilities.

### Key Features
- 🔐 JWT-based Authentication & Authorization
- 📦 Complete Product CRUD Operations
- 🔍 Advanced Product Search (by name and price range)
- ✅ Input Validation with detailed error messages
- 📝 Comprehensive Logging with Log4j
- 🚀 Asynchronous Operations with CompletableFuture
- 💾 Redis Caching for improved performance
- 📊 H2/PostgreSQL Database Support
- 🎨 React Frontend Application

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security 6.x** with JWT
- **Spring Data JPA** with Hibernate
- **H2 Database** (Development) / **PostgreSQL** (Production)
- **Redis** for caching
- **Maven** for dependency management
- **JUnit 5** for testing
- **Lombok** for boilerplate reduction

### Frontend
- **React 18**
- **React Router v6**
- **React Bootstrap**
- **Axios** for HTTP requests

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Node.js 16+ and npm (for React frontend)
- Redis (optional, for caching)
- PostgreSQL (for production)

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd product-management-system
```

2. **Backend Setup**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

3. **Frontend Setup**
```bash
cd frontend
npm install
npm start
```

### Configuration

#### application.properties
```properties
# Server Configuration
server.port=8080

# Database Configuration (H2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# JWT Configuration
jwt.secret=mySecretKey
jwt.expiration=86400000

# Redis Configuration (Optional)
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
```

## API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Authentication

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Validation Rules:**
- Username: 4-20 characters
- Password: minimum 6 characters
- Email: valid email format

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

### Product Management

All product endpoints require JWT authentication. Include the token in the Authorization header:
```http
Authorization: Bearer <jwt-token>
```

#### Get All Products
```http
GET /api/products
Authorization: Bearer <jwt-token>
```

#### Get Product by ID
```http
GET /api/products/{id}
Authorization: Bearer <jwt-token>
```

#### Create Product
```http
POST /api/products
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99
}
```

**Validation Rules:**
- Name: 3-100 characters, required
- Description: max 500 characters, optional
- Price: must be greater than 0, max 2 decimal places

#### Update Product
```http
PUT /api/products/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Updated Laptop",
  "description": "Updated description",
  "price": 1099.99
}
```

#### Delete Product
```http
DELETE /api/products/{id}
Authorization: Bearer <jwt-token>
```

#### Search Products
```http
POST /api/products/search
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Laptop",
  "minPrice": 500.00,
  "maxPrice": 1500.00
}
```

All search parameters are optional.

## Request & Response Examples

### Successful Registration Response
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": "johndoe"
}
```

### Successful Login Response
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "username": "johndoe",
    "email": "john@example.com"
  }
}
```

### Product Response
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "createdAt": "2024-01-15T10:30:00.000+00:00"
  }
}
```

### Product List Response
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Laptop",
      "description": "High-performance laptop",
      "price": 999.99,
      "createdAt": "2024-01-15T10:30:00.000+00:00"
    },
    {
      "id": 2,
      "name": "Mouse",
      "description": "Wireless mouse",
      "price": 29.99,
      "createdAt": "2024-01-15T11:00:00.000+00:00"
    }
  ]
}
```

## Error Handling

### Error Response Format
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

### Common HTTP Status Codes
- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data or validation error
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Validation Error Example
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "name": "Product name must be between 3 and 100 characters",
    "price": "Price must be greater than 0"
  }
}
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

### Roles Table
```sql
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);
```

### User_Roles Table
```sql
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
```

### Products Table
```sql
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Security

### JWT Token Structure
- **Header**: Algorithm and token type
- **Payload**: User information and expiration
- **Signature**: Verified using secret key

### Security Features
1. **Password Encryption**: BCrypt with strength 10
2. **JWT Expiration**: 24 hours (configurable)
3. **Role-based Access Control**: ROLE_USER, ROLE_ADMIN
4. **CORS Configuration**: Configured for React frontend
5. **CSRF Protection**: Disabled for REST API
6. **SQL Injection Protection**: Via JPA/Hibernate

### Security Best Practices
- Always use HTTPS in production
- Store JWT tokens securely in frontend
- Implement token refresh mechanism
- Regular security audits
- Input validation and sanitization

## Testing

### Unit Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductServiceTest

# Generate test coverage report
mvn jacoco:report
```

### Integration Tests
- AuthController tests
- ProductController tests
- Repository tests
- Service layer tests

### Test Coverage Requirements
- Minimum 80% code coverage
- All critical paths tested
- Edge cases covered

### Manual Testing with cURL

#### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "name": "New Product",
    "description": "Product description",
    "price": 99.99
  }'
```

## Deployment

### Docker Deployment
```yaml.
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - db
      - redis
  
  frontend:
    build: ./frontend
    ports:
      - "3000:80"
    depends_on:
      - backend
  
  db:
    image: postgres:14
    environment:
      - POSTGRES_DB=productdb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
  
  redis:
    image: redis:7
    ports:
      - "6379:6379"
```

### Cloud Deployment Options
- **Heroku**: Easy deployment with Git
- **AWS Elastic Beanstalk**: Scalable Java applications
- **Google Cloud Platform**: App Engine or Kubernetes
- **Azure App Service**: Managed platform for Java apps

## Performance Optimization

### Caching Strategy
- Products cached for 10 minutes
- Cache invalidation on updates
- Redis for distributed caching

### Async Operations
- All service methods use CompletableFuture
- Non-blocking I/O operations
- Thread pool configuration

### Database Optimization
- Indexed columns: username, email, product name
- Query optimization with JPA
- Connection pooling with HikariCP

## Monitoring and Logging

### Logging Configuration
```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <logger name="com.productapi" level="DEBUG"/>
    <logger name="org.springframework.security" level="DEBUG"/>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

### Monitoring Endpoints
- Health check: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`

## API Versioning

Future versions will follow semantic versioning:
- v1: Current version
- v2: Planned enhancements
    - GraphQL support
    - WebSocket notifications
    - Advanced search filters

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact & Support

- **Email**: support@productapi.com
- **Documentation**: [API Docs](http://localhost:8080/swagger-ui.html)
- **Issues**: GitHub Issues

---

**Version**: 1.0.0  
**Last Updated**: July 2025  
**Author**: Aidil