-- ============================================================
-- ShopWise AI - Database Schema
-- PostgreSQL/Supabase SQL
-- Run this in Supabase SQL Editor
-- ============================================================

-- Extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- USERS TABLE
-- ============================================================
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  email VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(255),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- ============================================================
-- USER_PREFERENCES TABLE
-- ============================================================
CREATE TABLE user_preferences (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  preferences JSONB DEFAULT '{}',
  -- Example preferences:
  -- {
  --   "budget_range": {"min": 10000, "max": 50000},
  --   "preferred_brands": ["Samsung", "Apple", "Sony"],
  --   "priority_features": ["camera", "battery", "performance"],
  --   "product_categories": ["smartphone", "laptop"],
  --   "notification_preferences": {"email": true, "push": false}
  -- }
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_preferences_user_id ON user_preferences(user_id);

-- ============================================================
-- PRODUCTS TABLE
-- ============================================================
CREATE TABLE products (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255) NOT NULL,
  brand VARCHAR(100) NOT NULL,
  category VARCHAR(50) NOT NULL,
  description TEXT,
  price DECIMAL(12, 2) NOT NULL CHECK (price >= 0),
  discount_percentage DECIMAL(5, 2) DEFAULT 0 CHECK (discount_percentage >= 0 AND discount_percentage <= 100),
  image_url VARCHAR(500),
  rating DECIMAL(3, 2) CHECK (rating >= 0 AND rating <= 5),
  review_count INTEGER DEFAULT 0 CHECK (review_count >= 0),
  
  -- JSONB fields for flexible product attributes
  specifications JSONB DEFAULT '{}',
  features JSONB DEFAULT '[]',
  performance JSONB DEFAULT '{}',
  pros JSONB DEFAULT '[]',
  cons JSONB DEFAULT '[]',
  
  availability BOOLEAN DEFAULT true,
  in_stock_quantity INTEGER DEFAULT 0 CHECK (in_stock_quantity >= 0),
  
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT valid_category CHECK (category IN ('smartphone', 'laptop', 'headphones', 'smartwatch', 'tablet', 'camera'))
);

CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_brand ON products(brand);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_rating ON products(rating);
CREATE INDEX idx_products_name ON products(name);

-- ============================================================
-- REVIEWS TABLE
-- ============================================================
CREATE TABLE reviews (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
  title VARCHAR(200),
  content TEXT,
  helpful_count INTEGER DEFAULT 0 CHECK (helpful_count >= 0),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_reviews_product_id ON reviews(product_id);
CREATE INDEX idx_reviews_rating ON reviews(rating);
CREATE INDEX idx_reviews_created_at ON reviews(created_at);

-- ============================================================
-- WISHLIST_ITEMS TABLE
-- ============================================================
CREATE TABLE wishlist_items (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT unique_user_product_wishlist UNIQUE (user_id, product_id)
);

CREATE INDEX idx_wishlist_user_id ON wishlist_items(user_id);
CREATE INDEX idx_wishlist_product_id ON wishlist_items(product_id);

-- ============================================================
-- Enable Row Level Security (RLS)
-- ============================================================
-- For MVP, using permissive policies. In production, restrict access.
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_preferences ENABLE ROW LEVEL SECURITY;
ALTER TABLE products ENABLE ROW LEVEL SECURITY;
ALTER TABLE reviews ENABLE ROW LEVEL SECURITY;
ALTER TABLE wishlist_items ENABLE ROW LEVEL SECURITY;

-- Public read access for products and reviews (for MVP)
CREATE POLICY "Allow public read access on products" ON products FOR SELECT USING (true);
CREATE POLICY "Allow public read access on reviews" ON reviews FOR SELECT USING (true);

-- For MVP: allow public write to wishlist and reviews (no auth yet)
CREATE POLICY "Allow public access on wishlist_items" ON wishlist_items FOR ALL USING (true);
CREATE POLICY "Allow public access on reviews" ON reviews FOR ALL USING (true);
CREATE POLICY "Allow public access on users" ON users FOR ALL USING (true);
CREATE POLICY "Allow public access on user_preferences" ON user_preferences FOR ALL USING (true);

-- ============================================================
-- Database created successfully
-- ============================================================
-- Run seed.sql next to populate test data
