# 🔗 Link Shortening Service

A secure, customizable URL shortening service built with Spring Boot. Users can shorten URLs, track click metrics, and
manage their links through authenticated endpoints.

---

## 🚀 Features

- Shorten URLs with optional custom code
- Redirect shortened links to original URLs
- Track click counts and access analytics
- JWT-based user authentication
- In-memory H2 database for quick setup
- RESTful API with validation and error handling

---

## 🛠 Technologies Used

| Technology          | Purpose                         |
|---------------------|---------------------------------|
| Spring Boot 3.3.x   | Core application framework      |
| Spring Security     | Authentication & authorization  |
| JJWT 0.12.5         | JWT token generation & parsing  |
| H2 Database         | In-memory persistence           |
| Spring Data JPA     | ORM and repository abstraction  |
| Hibernate Validator | Input validation                |
| Maven               | Build and dependency management |

---

## 📦 Setup Instructions

### Prerequisites

- Java 21+
- Maven 3.8+

### Clone and Build

```bash
git clone https://github.com/your-org/link-shortener.git
cd link-shortner-api
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The service will start on `http://localhost:8080`.

### Access H2 Console (Dev Only)

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:shortener`
- Username: `sa`
- Password: *(leave blank)*

---

## 🧪 Testing the Service

### 1. Register a User

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"StrongPass123"}'
```

### 2. Login and Get JWT

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"StrongPass123"}'
```

Save the returned `token` for authenticated requests.

### 3.1 Shorten a URL Private. Input 'customCode' value optional

```bash
curl -X POST http://localhost:8080/shorten \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"originalUrl":"https://example.com","customCode":"abc123"}'
```

### 3.2 Shorten a URL Public. Input 'customCode' value optional

```bash
curl -X POST http://localhost:8080/shorten \
  -H "Content-Type: application/json" \
  -d '{"originalUrl":"https://example.com","customCode":""}'
```

### 4. Redirect via Shortened URL

```bash
curl -i http://localhost:8080/abc123
```

### 5. View User’s Links

```bash
curl -X GET http://localhost:8080/user/links?page=0&size=20 \
  -H "Authorization: Bearer <token>"
```

### 6. View Analytics for a specific Short Link created by the user, example: abc123

```bash
curl -X GET http://localhost:8080/user/short_link/abc123/analytics \
  -H "Authorization: Bearer <token>"
```

---

## 📚 API Documentation

### 🔐 Authentication

#### `POST /auth/register`

Registers a new user.

**Request Body**:

```json
{
  "email": "user@example.com",
  "password": "StrongPass123"
}
```

**Response**:

```json
  "User registered successfully"
```

#### `POST /auth/login`

Authenticates a user and returns a JWT.

**Request Body**:

```json
{
  "email": "user@example.com",
  "password": "StrongPass123"
}
```

**Response**:

```json
{
  "token": "jwt"
}
```

---

### 🔗 URL Shortening

#### `POST /shorten`

Creates a shortened URL.

**Headers**: `Authorization: Bearer <JWT>`

**Request Body**:

```json
{
  "originalUrl": "https://example.com",
  "customCode": "abc123"
}
```

**Response**:

```json
{
  "shortenedUrl": "http://localhost:8080/abc123"
}
```

---

### 🚀 Redirection

#### `GET /{shortenedCode}`

Redirects to the original URL.

**Response**: `302 Found` with `Location` header pointing to the original URL.

---

### 📈 Analytics

#### `GET /user/links?page=0&size=20`

Lists all links created by the authenticated user.

**Response**:

```json
[
  {
    "shortLink": "http://localhost:8080/abc123",
    "originalUrl": "https://example.com",
    "clickCount": 10,
    "createdDate": "2025-08-28T09:36:44.429444",
    "lastAccessedAt": "2025-08-28T09:42:05.3690213"
  }
]
```

#### `GET /user/short_link/{shortenedCode}/analytics`

Returns detailed analytics for a specific link.

**Response**:

```json
{
  "shortLink": "http://localhost:8080/abc123",
  "originalUrl": "https://example.com",
  "clickCount": 10,
  "createdDate": "2025-08-28T09:36:44.429444",
  "lastAccessedAt": "2025-08-28T09:42:05.3690213"
}
```

---

## 📌 Notes

- Only `http` and `https` URLs are accepted.
- Custom Code must be alphanumeric and unique.
- JWT tokens expire after 60 minutes (configurable).
- All endpoints return structured error messages on failure.
