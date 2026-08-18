-- ============================================================
-- ShopWise AI - SQL Migration 3: Create Recommendations Table
-- ============================================================
-- Run this third in your Supabase SQL Editor

CREATE TABLE recommendations (
  id BIGSERIAL PRIMARY KEY,
  search_id BIGINT NOT NULL REFERENCES searches(id) ON DELETE CASCADE,
  product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
  match_percentage INTEGER CHECK (match_percentage >= 0 AND match_percentage <= 100),
  rank_type VARCHAR(20) NOT NULL,
  explanation TEXT,
  score_breakdown JSONB DEFAULT '{}',
  trade_offs TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT unique_search_product_ranktype UNIQUE (search_id, product_id, rank_type)
);

CREATE INDEX idx_recommendations_search ON recommendations(search_id);
CREATE INDEX idx_recommendations_product ON recommendations(product_id);
CREATE INDEX idx_recommendations_rank_type ON recommendations(rank_type);

ALTER TABLE recommendations ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow public access" ON recommendations
  FOR ALL USING (true);
