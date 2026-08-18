-- ============================================================
-- ShopWise AI - SQL Migration 2: Create Searches Table
-- ============================================================
-- Run this second in your Supabase SQL Editor

CREATE TABLE searches (
  id BIGSERIAL PRIMARY KEY,
  query TEXT NOT NULL,
  extracted_requirements JSONB DEFAULT '{}',
  status VARCHAR(20) DEFAULT 'completed',
  error_message TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_searches_created_at ON searches(created_at);
CREATE INDEX idx_searches_status ON searches(status);

ALTER TABLE searches ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow public access" ON searches
  FOR ALL USING (true);
