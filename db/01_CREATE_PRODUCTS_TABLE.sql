-- ============================================================
-- ShopWise AI - SQL Migration 1: Create Products Table
-- ============================================================
-- Run this first in your Supabase SQL Editor

CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  brand VARCHAR(100) NOT NULL,
  category VARCHAR(50) NOT NULL,
  description TEXT,
  price DECIMAL(10, 2) NOT NULL,
  discount_percentage DECIMAL(5, 2) DEFAULT 0,
  image_url VARCHAR(500),
  rating DECIMAL(3, 2) CHECK (rating >= 0 AND rating <= 5),
  review_count INTEGER DEFAULT 0,
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

-- Enable RLS (Row-Level Security) for API access
ALTER TABLE products ENABLE ROW LEVEL SECURITY;

-- Allow public read access
CREATE POLICY "Allow public read access" ON products
  FOR SELECT USING (true);
