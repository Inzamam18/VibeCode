# SmartShop AI - Backend

AI-Powered Personal Shopping Assistant Backend built with **Java** & **Spring Boot**.

SmartShop AI interprets user natural language shopping queries (e.g. *"I need a phone under ₹30,000 with a great camera and battery"*), extracts structured requirements using **Google Gemini AI** (with an instant heuristic fallback), queries candidate products from an existing **Supabase PostgreSQL** database, and calculates deterministic suitability scores (0–100) with factual recommendation explanations.

---

## Key Features

- **AI Intent Parsing**: Natural language shopping queries translated to structured constraints via Gemini Free Tier.
- **Resilient Fallback**: Zero-downtime heuristic rule-based parser kicks in automatically if Gemini is rate-limited or offline.
- **Deterministic Suitability Engine**: Multi-dimensional scoring (0–100) factoring in price fit, user priorities, specifications, features, performance, rating, and discounts.
- **Strict Fact-Based Explanations**: Recommendation reasons, strengths, and tradeoffs are constructed solely from PostgreSQL database attributes.
- **RESTful Endpoints**: Full suite for product catalog, filtering, sorting, assistant search, wishlists, and reviews.
- **OpenAPI / Swagger 3.0**: Interactive API documentation at `/swagger-ui.html`.
- **Security & Validation**: Jakarta Validation, secure CORS configuration, custom security headers, and zero secret logging.

---

## Tech Stack

- **Language**: Java 17+
- **Framework**: Spring Boot 3.4.3
- **Data & ORM**: Spring Data JPA, Hibernate, PostgreSQL Driver
- **Validation**: Jakarta Bean Validation
- **AI**: Google Gemini API (`gemini-1.5-flash`)
- **Documentation**: Springdoc OpenAPI / Swagger UI
- **Build**: Maven / Maven Wrapper

---

## Architecture Overview

```
com.smartshop
├── controller/         # REST API Endpoints
├── service/            # Business Logic Orchestration
├── repository/         # Spring Data JPA Repositories & Specifications
├── entity/             # JPA Entities for Existing Database Tables
├── dto/                # Request & Response DTOs
├── mapper/             # Entity <-> DTO Mappers & JSON Parsers
├── ai/                 # Gemini API Client & Heuristic Fallback Extractor
├── recommendation/     # Deterministic Suitability Scorer & Ranking Engine
├── config/             # App, CORS, and OpenAPI Configuration
├── exception/          # Centralized Global Exception Handler (@RestControllerAdvice)
└── security/           # Security Headers & Filters
```

---

## Getting Started

### 1. Prerequisites
- Java 17 or higher
- Supabase PostgreSQL Database (or any PostgreSQL instance)
- Google Gemini API Key (optional for development, fallback extractor is included)

### 2. Environment Configuration
Copy `.env.example` to `.env` or configure the following environment variables:

```env
# Database Configuration (Supabase PostgreSQL)
DB_URL=jdbc:postgresql://<your-supabase-host>:5432/postgres?sslmode=require
DB_USERNAME=postgres
DB_PASSWORD=<your-database-password>

# Google Gemini API Key
GEMINI_API_KEY=<your-gemini-api-key>

# Frontend Origin for CORS
FRONTEND_URL=http://localhost:3000,http://localhost:5173

# Server Port
PORT=8080
```

### 3. Run Locally

Using the included Maven Wrapper:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

### 4. Run Tests

```bash
# Windows
.\mvnw.cmd test

# macOS / Linux
./mvnw test
```

---

## API Endpoints

### Products
- `GET /api/products` - Filter products (`category`, `brand`, `minPrice`, `maxPrice`, `minRating`, `sort`)
- `GET /api/products/{id}` - Get product details by ID
- `GET /api/products/category/{category}` - Get products by category

### AI Shopping Assistant
- `POST /api/assistant/search` - Natural language query shopping assistant

```json
// Request
{
  "query": "I need a phone under ₹30,000 with a great camera and battery."
}
```

### Wishlist
- `POST /api/wishlist` - Add product to wishlist
- `DELETE /api/wishlist/{productId}?userId={userId}` - Remove from wishlist
- `GET /api/wishlist/{userId}` - Get user's wishlist
- `GET /api/wishlist/{userId}/check/{productId}` - Check wishlist status

### Reviews
- `GET /api/products/{productId}/reviews` - Get product reviews
- `POST /api/products/{productId}/reviews` - Submit product review

---

## Interactive Documentation

When the application is running, view and test all APIs interactively via Swagger UI:
👉 **`http://localhost:8080/swagger-ui.html`**

---

## License
MIT License
