# ShopWise AI — Database Layer Documentation

## Overview

This database layer is designed to support the ShopWise AI shopping assistant application. It uses PostgreSQL/Supabase and is structured for efficient Spring Boot + Hibernate integration.

**Key principles:**
- UUID primary keys throughout
- JSONB for flexible product specifications
- Row-level security (RLS) for API access control
- No external dependencies (no pgvector, Edge Functions, Storage)
- Minimal schema: 5 tables, no over-engineering

---

## Database Schema

### Tables Overview

| Table | Purpose | Rows | Type |
|---|---|---|---|
| `users` | User accounts | 2 | Core |
| `user_preferences` | User shopping preferences | 2 | Core |
| `products` | Product catalog | 10 | Core |
| `reviews` | Product reviews and ratings | 21 | Core |
| `wishlist_items` | User wishlist | 0 (populated by app) | Core |

---

## Table Definitions

### 1. `users`

Stores user account information.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `id` | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier |
| `email` | VARCHAR(255) | NOT NULL, UNIQUE | User email |
| `name` | VARCHAR(255) | NULL | User full name |
| `created_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Account creation |
| `updated_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Last update |

**Indexes:**
- `idx_users_email` — for fast email-based lookups

**Relationships:**
- 1 → Many: `user_preferences` (one-to-one in practice)
- 1 → Many: `wishlist_items`

**JPA Mapping:**
```java
@Entity
@Table(name = "users")
public class User {
  @Id
  private UUID id;
  
  @Column(unique = true)
  private String email;
  
  private String name;
  
  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;
  
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
```

---

### 2. `user_preferences`

Stores flexible user preferences as JSONB for extensibility.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `id` | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier |
| `user_id` | UUID | NOT NULL, UNIQUE, FK users.id | One-to-one relationship |
| `preferences` | JSONB | DEFAULT '{}' | Flexible preference object |
| `created_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| `updated_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Last update |

**JSONB Structure Example:**
```json
{
  "budget_range": {
    "min": 20000,
    "max": 100000
  },
  "preferred_brands": ["Apple", "Samsung", "Sony"],
  "priority_features": ["camera", "performance", "battery"],
  "product_categories": ["smartphone", "laptop"],
  "notification_preferences": {
    "email": true,
    "push": false
  }
}
```

**Indexes:**
- `idx_user_preferences_user_id` — for fast user lookups

**JPA Mapping:**
```java
@Entity
@Table(name = "user_preferences")
public class UserPreference {
  @Id
  private UUID id;
  
  @Column(unique = true)
  private UUID userId;
  
  @Type(JsonType.class)  // Hibernate 6.0+
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> preferences;
  
  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;
  
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
```

---

### 3. `products`

Main product catalog supporting 6 categories with flexible JSONB specifications.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `id` | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier |
| `name` | VARCHAR(255) | NOT NULL | Product name |
| `brand` | VARCHAR(100) | NOT NULL | Manufacturer |
| `category` | VARCHAR(50) | NOT NULL, CHECK IN (...) | 6 valid categories |
| `description` | TEXT | NULL | Product description |
| `price` | DECIMAL(12, 2) | NOT NULL, CHECK >= 0 | Price in local currency |
| `discount_percentage` | DECIMAL(5, 2) | DEFAULT 0, CHECK 0-100 | Current discount |
| `image_url` | VARCHAR(500) | NULL | Public HTTPS image |
| `rating` | DECIMAL(3, 2) | CHECK 0-5 | Average rating (1-5 stars) |
| `review_count` | INTEGER | DEFAULT 0, CHECK >= 0 | Number of reviews |
| `specifications` | JSONB | DEFAULT '{}' | Technical specifications |
| `features` | JSONB | DEFAULT '[]' | List of features |
| `performance` | JSONB | DEFAULT '{}' | Performance metrics |
| `pros` | JSONB | DEFAULT '[]' | List of advantages |
| `cons` | JSONB | DEFAULT '[]' | List of disadvantages |
| `availability` | BOOLEAN | DEFAULT true | In stock status |
| `in_stock_quantity` | INTEGER | DEFAULT 0, CHECK >= 0 | Available units |
| `created_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| `updated_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Last update |

**Valid Categories:**
- `smartphone`
- `laptop`
- `headphones`
- `smartwatch`
- `tablet`
- `camera`

**JSONB Field Examples:**

**Smartphone Specifications:**
```json
{
  "processor": "Apple A17 Pro",
  "ram": 8,
  "storage": 256,
  "display": "6.7 inch OLED",
  "battery": 4685,
  "camera_main": "48MP",
  "camera_front": "12MP",
  "os": "iOS 17"
}
```

**Laptop Specifications:**
```json
{
  "processor": "Intel Core i7-13th Gen",
  "ram": 16,
  "storage": 1024,
  "storage_type": "SSD",
  "display": "14 inch OLED",
  "gpu": "Intel Iris Xe",
  "battery_life": "12 hours",
  "weight": "1.65 kg",
  "os": "Windows 11"
}
```

**Features (Array):**
```json
["Titanium design", "Action button", "Excellent camera", "USB-C"]
```

**Performance (Object):**
```json
{
  "cpu_score": 98,
  "gaming": 95,
  "battery_hours": 16,
  "camera_score": 96
}
```

**Pros/Cons (Arrays):**
```json
["Exceptional camera", "Ultra-fast processor", "Premium design"]
```

**Indexes:**
- `idx_products_category` — for category filtering
- `idx_products_brand` — for brand filtering
- `idx_products_price` — for price sorting
- `idx_products_rating` — for rating sorting
- `idx_products_name` — for name searches

**JPA Mapping:**
```java
@Entity
@Table(name = "products")
public class Product {
  @Id
  private UUID id;
  
  @Column(length = 255, nullable = false)
  private String name;
  
  @Column(length = 100, nullable = false)
  private String brand;
  
  @Column(length = 50, nullable = false)
  private String category;
  
  @Column(columnDefinition = "TEXT")
  private String description;
  
  @Column(nullable = false)
  private BigDecimal price;
  
  private BigDecimal discountPercentage;
  
  private String imageUrl;
  
  private BigDecimal rating;
  private Integer reviewCount;
  
  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> specifications;
  
  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private List<String> features;
  
  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> performance;
  
  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private List<String> pros;
  
  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private List<String> cons;
  
  private Boolean availability;
  private Integer inStockQuantity;
  
  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;
  
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
```

---

### 4. `reviews`

Product reviews with ratings and engagement metrics.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `id` | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier |
| `product_id` | UUID | NOT NULL, FK products.id | Referenced product |
| `rating` | INTEGER | NOT NULL, CHECK 1-5 | Star rating |
| `title` | VARCHAR(200) | NULL | Review headline |
| `content` | TEXT | NULL | Review text |
| `helpful_count` | INTEGER | DEFAULT 0, CHECK >= 0 | Helpful votes |
| `created_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Review date |
| `updated_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Last update |

**Indexes:**
- `idx_reviews_product_id` — for product lookups
- `idx_reviews_rating` — for filtering by rating
- `idx_reviews_created_at` — for chronological sorting

**JPA Mapping:**
```java
@Entity
@Table(name = "reviews")
public class Review {
  @Id
  private UUID id;
  
  @Column(nullable = false)
  private UUID productId;
  
  @Column(nullable = false)
  private Integer rating;
  
  @Column(length = 200)
  private String title;
  
  @Column(columnDefinition = "TEXT")
  private String content;
  
  private Integer helpfulCount;
  
  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;
  
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
```

---

### 5. `wishlist_items`

User wishlist with product references.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `id` | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier |
| `user_id` | UUID | NOT NULL, FK users.id | User reference |
| `product_id` | UUID | NOT NULL, FK products.id | Product reference |
| `created_at` | TIMESTAMP WITH TIME ZONE | DEFAULT CURRENT_TIMESTAMP | Added date |

**Constraints:**
- `unique_user_product_wishlist` — Each user can only add a product once

**Indexes:**
- `idx_wishlist_user_id` — for user's wishlist lookups
- `idx_wishlist_product_id` — for product wishlist metrics

**JPA Mapping:**
```java
@Entity
@Table(name = "wishlist_items")
public class WishlistItem {
  @Id
  private UUID id;
  
  @Column(nullable = false)
  private UUID userId;
  
  @Column(nullable = false)
  private UUID productId;
  
  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;
}
```

---

## Seed Data

### Products (10 total)

**Smartphones (3):**
- iPhone 15 Pro Max (Apple) — ₹139,999
- Samsung Galaxy A54 (Samsung) — ₹28,999
- Google Pixel 8 Pro (Google) — ₹84,999

**Laptops (2):**
- Dell XPS 14 (Dell) — ₹94,999
- ASUS ROG Zephyrus G14 (ASUS) — ₹129,999

**Headphones (2):**
- Sony WH-1000XM5 (Sony) — ₹27,999
- Apple AirPods Pro 2 (Apple) — ₹26,999

**Smartwatch (1):**
- Apple Watch Ultra (Apple) — ₹89,999

**Tablet (1):**
- iPad Pro 12.9 (Apple) — ₹119,999

**Camera (1):**
- Sony A6700 (Sony) — ₹149,999

### Reviews

- 21 total reviews across all products
- Ratings from 4 to 5 stars
- Realistic review titles and content
- Ready for display in product detail pages

### Users

- 2 test users (alice@example.com, bob@example.com)
- Each with sample preferences

---

## Entity Relationships Diagram

```mermaid
erDiagram
    USERS ||--o| USER_PREFERENCES : has
    USERS ||--o{ WISHLIST_ITEMS : adds
    PRODUCTS ||--o{ REVIEWS : receives
    PRODUCTS ||--o{ WISHLIST_ITEMS : appears_in
    
    USERS {
        uuid id PK
        string email UK
        string name
        timestamp created_at
        timestamp updated_at
    }
    
    USER_PREFERENCES {
        uuid id PK
        uuid user_id FK UK
        jsonb preferences
        timestamp created_at
        timestamp updated_at
    }
    
    PRODUCTS {
        uuid id PK
        string name
        string brand
        string category
        text description
        decimal price
        decimal discount_percentage
        string image_url
        decimal rating
        integer review_count
        jsonb specifications
        jsonb features
        jsonb performance
        jsonb pros
        jsonb cons
        boolean availability
        integer in_stock_quantity
        timestamp created_at
        timestamp updated_at
    }
    
    REVIEWS {
        uuid id PK
        uuid product_id FK
        integer rating
        string title
        text content
        integer helpful_count
        timestamp created_at
        timestamp updated_at
    }
    
    WISHLIST_ITEMS {
        uuid id PK
        uuid user_id FK
        uuid product_id FK
        timestamp created_at
    }
```

---

## Supabase Setup Instructions

### Prerequisites

- Supabase account (free tier is sufficient)
- PostgreSQL 14+ (provided by Supabase)

### Setup Steps

1. **Create Supabase Project**
   - Visit [supabase.com](https://supabase.com)
   - Click "New Project"
   - Choose a region (preferably same as your application)
   - Wait for provisioning (2-3 minutes)

2. **Run Schema Migration**
   - In Supabase dashboard, go to **SQL Editor**
   - Create a new query
   - Copy entire contents of `schema.sql`
   - Paste into SQL Editor
   - Click **Run**
   - Wait for success message

3. **Load Seed Data**
   - In SQL Editor, create a new query
   - Copy entire contents of `seed.sql`
   - Paste into SQL Editor
   - Click **Run**
   - Verify output shows counts for all tables

4. **Verify Installation**
   - Go to **Table Editor**
   - Check that all 5 tables appear: users, user_preferences, products, reviews, wishlist_items
   - Click each table to see sample data

5. **Get Connection Credentials**
   - Go to **Settings** → **Database**
   - Note the following (needed for Spring Boot):
     - **Host:** `[project-id].db.supabase.co`
     - **Port:** `5432`
     - **Database:** `postgres`
     - **Username:** `postgres`
     - **Password:** Your database password

---

## Environment Variables

For Spring Boot application, set these environment variables:

```bash
# PostgreSQL/Supabase connection
SPRING_DATASOURCE_URL=jdbc:postgresql://[host]:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=[your-password]

# Hibernate
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQL10Dialect
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_BATCH_SIZE=20

# Connection pooling (HikariCP)
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=10
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=2
```

---

## Spring Boot Dependencies

Add to `pom.xml`:

```xml
<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Hibernate with UUID type support -->
<dependency>
    <groupId>com.vladmihalcea</groupId>
    <artifactId>hibernate-types-60</artifactId>
    <version>2.21.1</version>
</dependency>

<!-- JSONB support for Hibernate -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-community-dialects</artifactId>
</dependency>
```

---

## Common Queries via JPA

### Find products by category
```java
List<Product> products = productRepository.findByCategory("smartphone");
```

### Find products by price range
```java
List<Product> affordable = productRepository.findByPriceBetween(
  BigDecimal.valueOf(20000), 
  BigDecimal.valueOf(50000)
);
```

### Get top-rated products
```java
List<Product> topRated = productRepository.findByRatingGreaterThanEqual(
  BigDecimal.valueOf(4.5)
);
```

### Get user's wishlist
```java
List<WishlistItem> wishlist = wishlistRepository.findByUserId(userId);
```

### Get product reviews
```java
List<Review> reviews = reviewRepository.findByProductId(productId);
```

---

## Row-Level Security (RLS)

The database includes RLS policies to control access:

- **Products:** Public read access (no authentication required for MVP)
- **Reviews:** Public read/write access
- **Wishlist:** Public access (implement app-level user validation)
- **Users:** Public access (implement app-level validation)
- **User Preferences:** Public access

⚠️ **For production:** Implement proper RLS policies and authentication before going live.

---

## Performance Considerations

### Indexes
All frequently queried columns are indexed:
- Products by category, brand, price, rating
- Reviews by product and rating
- Wishlist by user and product
- Users by email

### JSONB Performance
- JSONB queries use GIN indexes (implied by default)
- Use operators: `->>` (text) or `->` (JSON value)
- Avoid complex JSONB queries on large datasets

### Connection Pooling
- HikariCP configured with 10 max connections
- Suitable for hackathon and MVP loads
- Scale up for production traffic

---

## Troubleshooting

### UUID Extension Error
**Error:** `type "uuid" does not exist`

**Solution:** The `uuid-ossp` extension is created in schema.sql. Ensure schema.sql runs first.

### JSONB Query Issues
**Error:** `column "specifications" is of type jsonb but expression is of type text`

**Solution:** Cast to jsonb in queries: `WHERE specifications @> 'key'::jsonb`

### Foreign Key Constraint Violation
**Error:** `ERROR: insert or update on table "reviews" violates foreign key constraint`

**Solution:** Ensure product exists before adding reviews. Seed data loads products first.

### Connection Pool Exhaustion
**Error:** `Unable to acquire a new Connection from the pool`

**Solution:** Increase `MAXIMUM_POOL_SIZE` or check for connection leaks in code.

---

## Not Implemented (By Design)

- ❌ `conversations`, `chat_messages` — Not needed for MVP
- ❌ `price_history` — No price tracking yet
- ❌ `product_comparisons` — Comparison logic in backend
- ❌ `embeddings`, `pgvector` — No vector search yet
- ❌ `recommendation_history` — Recommendations calculated per request
- ❌ External product sync — Using curated seed data
- ❌ Supabase Storage — Using external image URLs
- ❌ Edge Functions — Using Spring Boot backend
- ❌ Redis, Elasticsearch — Overkill for MVP

---

## Files in This Directory

| File | Purpose |
|---|---|
| `schema.sql` | PostgreSQL schema (5 tables, indexes, RLS) |
| `seed.sql` | 10 products + 21 reviews + 2 users |
| `README.md` | This file — Database documentation |

---

## Next Steps

1. ✅ Database layer complete
2. → Backend engineer: Connect Spring Boot to Supabase
3. → Implement REST APIs (GET /api/products, etc.)
4. → Integrate Gemini API for requirement extraction
5. → Frontend receives data from backend APIs

All database-related work is done. Ready for backend integration! 🚀
