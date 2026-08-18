# ShopWise AI — Database Design & Setup

This document outlines the complete Supabase PostgreSQL schema for the ShopWise AI hackathon project.

## Overview

The database stores:
1. **Products** — Catalog of laptops, smartphones, and headphones with detailed specs
2. **Searches** — User natural-language queries and extracted requirements
3. **Recommendations** — AI-ranked products with explanations and match scores
4. **Reviews** — User reviews and ratings for products (for social proof)

## Database Tables

### 1. `products` Table

Stores the product catalog with flexible specifications using JSONB.

```sql
CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  brand VARCHAR(100) NOT NULL,
  category VARCHAR(50) NOT NULL,  -- 'laptop', 'smartphone', 'headphones'
  description TEXT,
  price DECIMAL(10, 2) NOT NULL,
  discount_percentage DECIMAL(5, 2) DEFAULT 0,
  image_url VARCHAR(500),
  rating DECIMAL(3, 2) CHECK (rating >= 0 AND rating <= 5),
  review_count INTEGER DEFAULT 0,
  
  -- Flexible specs as JSONB for easy filtering
  specifications JSONB DEFAULT '{}',  
  features JSONB DEFAULT '{}',
  performance JSONB DEFAULT '{}',
  pros TEXT[] DEFAULT ARRAY[]::TEXT[],
  cons TEXT[] DEFAULT ARRAY[]::TEXT[],
  
  availability BOOLEAN DEFAULT true,
  in_stock_quantity INTEGER DEFAULT 0,
  
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT valid_category CHECK (category IN ('laptop', 'smartphone', 'headphones'))
);

CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_brand ON products(brand);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_rating ON products(rating);
```

**Example Specifications (JSONB):**

For smartphones:
```json
{
  "processor": "Qualcomm Snapdragon 8 Gen 3",
  "ram": 12,
  "storage": 256,
  "display": "6.7 inch AMOLED",
  "battery": 5000,
  "camera_main": "50MP",
  "camera_front": "12MP",
  "os": "Android 14"
}
```

For laptops:
```json
{
  "processor": "Intel Core i7-13700K",
  "ram": 16,
  "storage": 512,
  "storage_type": "SSD",
  "display": "16 inch FHD",
  "gpu": "NVIDIA RTX 4060",
  "battery_life": "8 hours",
  "weight": "2.1 kg"
}
```

For headphones:
```json
{
  "type": "over-ear",
  "driver_size": "40mm",
  "frequency_response": "20Hz-20kHz",
  "impedance": 32,
  "battery_life": "30 hours",
  "noise_cancellation": true,
  "connectivity": "Bluetooth 5.3"
}
```

---

### 2. `searches` Table

Stores each user's natural-language query and the structured requirements extracted by Gemini.

```sql
CREATE TABLE searches (
  id BIGSERIAL PRIMARY KEY,
  query TEXT NOT NULL,  -- Original user input
  
  -- Structured requirements extracted by Gemini AI
  extracted_requirements JSONB DEFAULT '{}',
  
  -- Example structure:
  -- {
  --   "category": "smartphone",
  --   "maxPrice": 30000,
  --   "minPrice": 15000,
  --   "priorities": ["camera", "battery"],
  --   "requiredFeatures": ["fast_charging", "high_refresh_rate"],
  --   "budget_flexibility": "tight",
  --   "use_case": "photography"
  -- }
  
  status VARCHAR(20) DEFAULT 'completed',  -- 'processing', 'completed', 'failed'
  error_message TEXT,  -- If status is 'failed', store error
  
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_searches_created_at ON searches(created_at);
CREATE INDEX idx_searches_status ON searches(status);
```

---

### 3. `recommendations` Table

Stores the ranked product recommendations for each search.

```sql
CREATE TABLE recommendations (
  id BIGSERIAL PRIMARY KEY,
  search_id BIGINT NOT NULL REFERENCES searches(id) ON DELETE CASCADE,
  product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
  
  match_percentage INTEGER CHECK (match_percentage >= 0 AND match_percentage <= 100),
  rank_type VARCHAR(20) NOT NULL,  -- 'OVERALL', 'VALUE', 'PERFORMANCE'
  
  -- AI-generated explanation of why this product matches
  explanation TEXT,
  
  -- Scoring breakdown (optional, for transparency)
  score_breakdown JSONB DEFAULT '{}',
  -- Example:
  -- {
  --   "price_fit": 85,
  --   "feature_match": 92,
  --   "rating_score": 88,
  --   "final_score": 88
  -- }
  
  trade_offs TEXT,  -- Smart trade-off detection (e.g., "Higher price but better performance")
  
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT unique_search_product_ranktype UNIQUE (search_id, product_id, rank_type)
);

CREATE INDEX idx_recommendations_search ON recommendations(search_id);
CREATE INDEX idx_recommendations_product ON recommendations(product_id);
CREATE INDEX idx_recommendations_rank_type ON recommendations(rank_type);
```

---

### 4. `reviews` Table (Optional MVP+)

For storing user reviews and building social proof.

```sql
CREATE TABLE reviews (
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  
  rating INTEGER CHECK (rating >= 1 AND rating <= 5),
  title VARCHAR(200),
  content TEXT,
  
  helpful_count INTEGER DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_reviews_product ON reviews(product_id);
```

---

### 5. `wishlist_items` Table (Optional MVP+)

For persisting user wishlist across sessions.

```sql
CREATE TABLE wishlist_items (
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  
  -- Can add user_id later when authentication is implemented
  session_id VARCHAR(255),  -- Use browser session ID for now
  
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT unique_wishlist UNIQUE (session_id, product_id)
);

CREATE INDEX idx_wishlist_session ON wishlist_items(session_id);
```

---

## Data Types Explained

| Column Type | Usage | Example |
|---|---|---|
| JSONB | Flexible structured data | specifications, performance, features |
| DECIMAL(10, 2) | Prices with 2 decimal places | 29,999.99 |
| TEXT[] | Array of strings | `['Wi-Fi 6', '5G', 'NFC']` |
| TIMESTAMP | Time tracking | created_at, updated_at |

---

## Relationships Diagram

```
products (catalog)
    ↓
    ├─→ recommendations (many)
    │       ↓
    │       └─→ searches (one)
    │
    └─→ reviews (many)

wishlist_items
    ↓
    └─→ products (many-to-one)
```

---

## Querying Patterns

### Find products by category
```sql
SELECT * FROM products WHERE category = 'smartphone' AND availability = true;
```

### Find products matching Gemini-extracted requirements
```sql
SELECT * FROM products 
WHERE category = 'smartphone'
  AND price <= 30000
  AND rating >= 4.0
ORDER BY rating DESC;
```

### Get top recommendations for a search
```sql
SELECT r.*, p.name, p.brand, p.price 
FROM recommendations r
JOIN products p ON r.product_id = p.id
WHERE r.search_id = $1 AND r.rank_type = 'OVERALL'
ORDER BY r.match_percentage DESC
LIMIT 5;
```

### Search by JSONB specifications
```sql
SELECT * FROM products 
WHERE category = 'smartphone'
  AND (specifications->>'processor') = 'Qualcomm Snapdragon 8 Gen 3'
  AND (specifications->'ram')::int >= 8;
```

---

## Indexes for Performance

| Index | Purpose |
|---|---|
| `idx_products_category` | Filter by product category |
| `idx_products_price` | Sort/filter by price |
| `idx_products_rating` | Sort by rating |
| `idx_recommendations_search` | Fetch recommendations for a search |
| `idx_recommendations_product` | Find all recommendations for a product |
| `idx_wishlist_session` | Fetch user's wishlist |

---

## Next Steps

1. **Create Supabase Project** — Set up a new PostgreSQL database in Supabase
2. **Run Migrations** — Execute the SQL scripts in Supabase SQL Editor
3. **Seed Products** — Insert sample product data (see `SEED_PRODUCTS.sql`)
4. **Test Queries** — Verify all indexes and relationships work
5. **Connect Backend** — Use Supabase connection string in Spring Boot

See `SETUP_SUPABASE.md` for step-by-step instructions.
