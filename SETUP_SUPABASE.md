# Supabase Setup Guide for ShopWise AI

Step-by-step instructions to create and configure your Supabase PostgreSQL database.

## Step 1: Create a Supabase Project

1. Go to [supabase.com](https://supabase.com) and sign up (or log in)
2. Click **"New Project"**
3. Fill in:
   - **Project Name:** `shopwise-ai` (or your preference)
   - **Database Password:** Create a strong password and **save it** (you'll need it)
   - **Region:** Choose closest to your location (e.g., `us-east-1` for US)
4. Click **"Create new project"** and wait 2-3 minutes for provisioning

## Step 2: Access Your Database

1. In the Supabase dashboard, go to **Settings** → **Database**
2. You'll see the connection details:
   - **Host:** `[project-id].supabase.co`
   - **Port:** `5432`
   - **User:** `postgres`
   - **Password:** The one you created
   - **Database:** `postgres`

Copy these for later use in your backend environment variables.

## Step 3: Run SQL Migrations

### Option A: Using Supabase SQL Editor (Easiest)

1. In the Supabase dashboard, go to **SQL Editor**
2. Click **"New Query"**
3. Copy and paste the SQL from `01_CREATE_PRODUCTS_TABLE.sql` (see below)
4. Click **"Run"** and wait for success
5. Repeat for each SQL file in order

### Option B: Using Command Line (psql)

```bash
# Install PostgreSQL client if you don't have it
# macOS: brew install postgresql
# Windows: choco install postgresql or download from postgresql.org
# Ubuntu: apt-get install postgresql-client

# Connect to your Supabase database
psql -h [host] -U postgres -d postgres -p 5432

# Paste the SQL from each migration file and press Enter
# You'll be prompted for your database password
```

## Step 4: SQL Migration Scripts

Copy and run these scripts in your Supabase SQL Editor in order.

### `01_CREATE_PRODUCTS_TABLE.sql`

```sql
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
```

### `02_CREATE_SEARCHES_TABLE.sql`

```sql
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
```

### `03_CREATE_RECOMMENDATIONS_TABLE.sql`

```sql
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
```

### `04_CREATE_REVIEWS_TABLE.sql`

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

ALTER TABLE reviews ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow public access" ON reviews
  FOR ALL USING (true);
```

### `05_CREATE_WISHLIST_TABLE.sql`

```sql
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
```

## Step 5: Verify Tables Were Created

In Supabase SQL Editor, run:

```sql
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public';
```

You should see:
- `products`
- `searches`
- `recommendations`
- `reviews`
- `wishlist_items`

## Step 6: Seed Sample Products

Run the script from `SEED_PRODUCTS.sql` (see below) to populate the database with realistic product data.

## Step 7: Test Queries

Try these queries in the Supabase SQL Editor to ensure everything works:

```sql
-- Count products
SELECT COUNT(*) as total_products FROM products;

-- Show first 5 products
SELECT id, name, brand, category, price, rating FROM products LIMIT 5;

-- Show products by category
SELECT name, price, rating FROM products WHERE category = 'smartphone' LIMIT 3;

-- Show products filtered by price
SELECT name, price FROM products WHERE price < 50000 ORDER BY price DESC;
```

## Step 8: Configure Backend Environment Variables

In your backend `.env` file, add:

```env
SUPABASE_URL=https://[project-id].supabase.co
SUPABASE_KEY=[your-anon-key]
SUPABASE_DB_HOST=[project-id].db.supabase.co
SUPABASE_DB_PORT=5432
SUPABASE_DB_USER=postgres
SUPABASE_DB_PASSWORD=[database-password]
SUPABASE_DB_NAME=postgres
```

**Where to find these:**
- **SUPABASE_URL**: Dashboard → Settings → API → URL
- **SUPABASE_KEY**: Dashboard → Settings → API → Anon Key (use this for client-side)
- **SUPABASE_DB_*****: Dashboard → Settings → Database (connection info)

## Step 9: Configure Frontend Environment Variables (Optional)

If your frontend needs to access Supabase directly (for wishlist, etc.):

```env
VITE_SUPABASE_URL=https://[project-id].supabase.co
VITE_SUPABASE_ANON_KEY=[your-anon-key]
```

## Troubleshooting

### Connection Refused
- Check you're using the correct host (must be `db.supabase.co`, not the API URL)
- Ensure your database password is correct
- Check if your IP is allowed (Supabase allows all by default, but verify firewall)

### RLS Policy Errors
- Make sure all `CREATE POLICY` statements ran successfully
- Tables need RLS enabled before policies can be created
- Public policies allow anyone to read/write (fine for MVP, lock down later)

### Foreign Key Constraint Errors
- Ensure `searches` table exists before `recommendations`
- Ensure `products` table exists before `recommendations`
- Delete test recommendations/searches before deleting a product

### Timeouts
- Large seed data inserts might timeout in the SQL Editor
- Split into smaller batches if needed
- Or use the backend to insert products programmatically

## Next Steps

1. ✅ Create Supabase project and run migrations
2. ✅ Seed sample product data
3. → Build backend Spring Boot app connected to this database
4. → Implement Gemini requirement extraction API
5. → Implement recommendation ranking logic
6. → Wire frontend to backend APIs

Your database is now ready for backend integration!
