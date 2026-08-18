-- ============================================================
-- ShopWise AI - SQL Migration 5: Create Wishlist Table
-- ============================================================
-- Run this fifth in your Supabase SQL Editor

CREATE TABLE wishlist_items (
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  session_id VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT unique_wishlist UNIQUE (session_id, product_id)
);

CREATE INDEX idx_wishlist_session ON wishlist_items(session_id);

ALTER TABLE wishlist_items ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow public access" ON wishlist_items
  FOR ALL USING (true);
