-- ============================================================
-- ShopWise AI - SQL Migration 4: Create Reviews Table
-- ============================================================
-- Run this fourth in your Supabase SQL Editor

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

ALTER TABLE reviews ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow public access" ON reviews
  FOR ALL USING (true);
