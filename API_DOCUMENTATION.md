# Authentication REST APIs Documentation

## Overview
This document outlines the REST APIs for user authentication (Login and Signup) using JWT tokens with refresh token support.

## Base URL
```
http://localhost:4000/api/auth
```

## Authentication Flow
1. User signs up → Receives access token + refresh token
2. User logs in → Receives access token + refresh token
3. Use access token in Authorization header for protected endpoints
4. When access token expires, use refresh token to get a new one
5. Protected endpoints require `Authorization: Bearer <access_token>`

---

## API Endpoints

### 1. User Signup
**Endpoint:** `POST /api/auth/signup`

**Description:** Register a new user with email and password

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "confirmPassword": "SecurePass123"
}
```

**Validation Rules:**
- `username`: Required, 3-50 characters
- `email`: Required, valid email format, must be unique
- `password`: Required, minimum 8 characters
- `confirmPassword`: Must match password

**Success Response (201 Created):**
```json
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

**Error Responses:**

400 - Invalid Input:
```json
{
  "username": "Username must be between 3 and 50 characters",
  "email": "Email should be valid",
  "password": "Password must be at least 8 characters"
}
```

400 - Email Already Exists:
```json
{
  "message": "Email address already exists"
}
```

400 - Username Already Taken:
```json
{
  "message": "Username already taken: john_doe"
}
```

400 - Passwords Don't Match:
```json
{
  "message": "Passwords do not match"
}
```

---

### 2. User Login
**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate user and get JWT tokens

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Validation Rules:**
- `email`: Required, valid email format
- `password`: Required

**Success Response (200 OK):**
```json
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

**Error Responses:**

400 - Invalid Credentials:
```json
{
  "message": "Invalid email or password"
}
```

400 - User Inactive:
```json
{
  "message": "User account is inactive"
}
```

---

### 3. Refresh Access Token
**Endpoint:** `POST /api/auth/refresh-token?refreshToken=<token>`

**Description:** Generate a new access token using refresh token

**Query Parameters:**
- `refreshToken` (required): The refresh token received during login/signup

**Request Headers:**
```
Content-Type: application/json
```

**Example Request:**
```
POST /api/auth/refresh-token?refreshToken=eyJhbGciOiJIUzUxMiJ9...
```

**Success Response (200 OK):**
```json
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

**Error Responses:**

400 - Invalid/Expired Refresh Token:
```json
{
  "message": "Invalid or expired refresh token"
}
```

404 - User Not Found:
```json
{
  "message": "User not found"
}
```

---

## Using Access Token in Protected Requests

For any protected endpoint, include the access token in the Authorization header:

```
Authorization: Bearer <access_token>
```

**Example:**
```bash
curl -X GET http://localhost:4000/api/employees/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

---

## Token Details

### Access Token
- **Expiration:** 24 hours (86400000 ms)
- **Purpose:** Used for authenticated API requests
- **Claims:** userId, email, issuedAt, expiration

### Refresh Token
- **Expiration:** 7 days (604800000 ms)
- **Purpose:** Generate new access tokens
- **Claims:** userId, email, type: "refresh", issuedAt, expiration

---

## Security Configuration

The application uses:
- **Password Encoding:** BCrypt
- **Authentication:** Spring Security with JWT
- **Session:** Stateless (No server-side sessions)
- **CSRF:** Disabled (Stateless JWT auth)

---

## Public Endpoints (No Authentication Required)
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `POST /api/auth/refresh-token`
- `GET /api/employees` (List employees)
- `GET /api/employees/{id}` (Get employee details)
- `GET /swagger-ui/**` (API documentation)
- `GET /v3/api-docs/**` (OpenAPI spec)
- `GET /h2-console/**` (Database console - development only)

---

## Protected Endpoints (JWT Authentication Required)
- `POST /api/employees` (Create employee)
- `PUT /api/employees/{id}` (Update employee)
- `DELETE /api/employees/{id}` (Delete employee)

---

## Example Usage Flow

### 1. Sign Up
```bash
curl -X POST http://localhost:4000/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "confirmPassword": "SecurePass123"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:4000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

### 3. Use Access Token to Access Protected Resource
```bash
curl -X GET http://localhost:4000/api/employees \
  -H "Authorization: Bearer <accessToken>"
```

### 4. Refresh Expired Token
```bash
curl -X POST "http://localhost:4000/api/auth/refresh-token?refreshToken=<refreshToken>"
```

---

## Error Handling

All errors follow a consistent format:

**400 - Bad Request:**
```json
{
  "message": "Error description"
}
```

**401 - Unauthorized:**
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

**404 - Not Found:**
```json
{
  "message": "Resource not found"
}
```

**500 - Internal Server Error:**
```json
{
  "message": "An internal server error occurred"
}
```

---

## JWT Token Structure

JWT tokens have 3 parts separated by dots:
```
header.payload.signature
```

**Header:**
```json
{
  "alg": "HS512",
  "typ": "JWT"
}
```

**Payload (Access Token):**
```json
{
  "sub": "john@example.com",
  "userId": 1,
  "iat": 1710000000,
  "exp": 1710086400
}
```

**Payload (Refresh Token):**
```json
{
  "sub": "john@example.com",
  "userId": 1,
  "type": "refresh",
  "iat": 1710000000,
  "exp": 1710604800
}
```

---

## Configuration

JWT settings are configured in `application.properties`:

```properties
app.jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationPurposeOnly123456
app.jwt.expiration=86400000         # 24 hours in milliseconds
app.jwt.refreshExpiration=604800000 # 7 days in milliseconds
```

**Note:** In production, change the secret to a strong, random value and use environment variables.

---

## Next Steps

1. Build and run the application: `mvn spring-boot:run`
2. Access Swagger UI: `http://localhost:4000/swagger-ui.html`
3. Test the APIs using Postman or Swagger UI
4. Access the database: `http://localhost:4000/h2-console`
   - JDBC URL: `jdbc:h2:mem:mydb`
   - Username: `admin`
   - Password: `pass`

