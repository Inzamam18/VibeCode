# ShopWise AI — Database Query Reference

Quick SQL queries for common operations. Useful for backend development and debugging.

## Product Queries

### Get all products with basic info
```sql
SELECT id, name, brand, category, price, rating, review_count 
FROM products 
ORDER BY created_at DESC;
```

### Get products by category
```sql
SELECT id, name, brand, category, price, rating 
FROM products 
WHERE category = 'smartphone'
ORDER BY rating DESC;
```

### Find affordable products
```sql
SELECT id, name, brand, price, price - (price * discount_percentage / 100) as final_price
FROM products 
WHERE price < 50000 
ORDER BY final_price ASC;
```

### Find highly rated products
```sql
SELECT id, name, brand, rating, review_count 
FROM products 
WHERE rating >= 4.5 AND review_count > 100
ORDER BY rating DESC;
```

### Search by price range
```sql
SELECT id, name, brand, category, price, rating 
FROM products 
WHERE price BETWEEN 30000 AND 70000
ORDER BY price ASC;
```

### Search by specifications (example: smartphones with 8GB+ RAM)
```sql
SELECT id, name, brand, price 
FROM products 
WHERE category = 'smartphone'
  AND (specifications->>'ram')::int >= 8
ORDER BY price ASC;
```

### Search by features
```sql
SELECT id, name, brand, price, features 
FROM products 
WHERE category = 'smartphone'
  AND features @> '"5G support"'::jsonb
ORDER BY rating DESC;
```

### Get product details (with all specs)
```sql
SELECT id, name, brand, category, price, rating, review_count,
       description, specifications, features, performance, pros, cons
FROM products 
WHERE id = 1;
```

### Count products by category
```sql
SELECT category, COUNT(*) as count 
FROM products 
GROUP BY category;
```

### Find products with discount
```sql
SELECT id, name, brand, price, discount_percentage,
       ROUND(price * (100 - discount_percentage) / 100, 2) as discounted_price
FROM products 
WHERE discount_percentage > 0
ORDER BY discount_percentage DESC;
```

## Search & Recommendation Queries

### Get all searches
```sql
SELECT id, query, status, created_at 
FROM searches 
ORDER BY created_at DESC;
```

### Get recommendations for a search
```sql
SELECT r.id, r.product_id, p.name, p.price, r.match_percentage, r.rank_type, r.explanation
FROM recommendations r
JOIN products p ON r.product_id = p.id
WHERE r.search_id = 1
ORDER BY r.match_percentage DESC;
```

### Get top recommendations (all rank types)
```sql
SELECT r.rank_type, p.name, p.price, r.match_percentage, r.explanation
FROM recommendations r
JOIN products p ON r.product_id = p.id
WHERE r.search_id = 1 AND r.rank_type IN ('OVERALL', 'VALUE', 'PERFORMANCE')
ORDER BY r.rank_type, r.match_percentage DESC;
```

### Get product recommendation statistics
```sql
SELECT p.id, p.name, COUNT(*) as times_recommended, AVG(r.match_percentage)::int as avg_score
FROM recommendations r
JOIN products p ON r.product_id = p.id
GROUP BY p.id, p.name
ORDER BY times_recommended DESC;
```

## Wishlist Queries

### Get user's wishlist (by session ID)
```sql
SELECT w.id, p.id as product_id, p.name, p.brand, p.price, p.rating
FROM wishlist_items w
JOIN products p ON w.product_id = p.id
WHERE w.session_id = 'session-123'
ORDER BY w.created_at DESC;
```

### Check if product is in wishlist
```sql
SELECT COUNT(*) as in_wishlist
FROM wishlist_items 
WHERE session_id = 'session-123' AND product_id = 5;
```

### Count wishlist items by product
```sql
SELECT product_id, COUNT(*) as wishlist_count
FROM wishlist_items 
GROUP BY product_id
ORDER BY wishlist_count DESC;
```

## Analytics Queries

### Most recommended products (last 7 days)
```sql
SELECT p.id, p.name, COUNT(*) as recommendation_count, AVG(r.match_percentage)::int as avg_score
FROM recommendations r
JOIN products p ON r.product_id = p.id
JOIN searches s ON r.search_id = s.id
WHERE s.created_at >= CURRENT_DATE - INTERVAL '7 days'
GROUP BY p.id, p.name
ORDER BY recommendation_count DESC
LIMIT 10;
```

### Average match percentage by rank type
```sql
SELECT rank_type, COUNT(*) as count, AVG(match_percentage)::int as avg_percentage
FROM recommendations 
GROUP BY rank_type
ORDER BY rank_type;
```

### Products that appear in most searches
```sql
SELECT p.id, p.name, p.category, COUNT(DISTINCT r.search_id) as search_count
FROM recommendations r
JOIN products p ON r.product_id = p.id
GROUP BY p.id, p.name, p.category
ORDER BY search_count DESC;
```

### Popular searches
```sql
SELECT query, COUNT(*) as count, MAX(created_at) as last_search
FROM searches 
WHERE status = 'completed'
GROUP BY query
ORDER BY count DESC
LIMIT 10;
```

## Maintenance Queries

### Check table sizes
```sql
SELECT schemaname, tablename, 
       pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables 
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

### Count rows in each table
```sql
SELECT 
  (SELECT COUNT(*) FROM products) as products,
  (SELECT COUNT(*) FROM searches) as searches,
  (SELECT COUNT(*) FROM recommendations) as recommendations,
  (SELECT COUNT(*) FROM reviews) as reviews,
  (SELECT COUNT(*) FROM wishlist_items) as wishlist_items;
```

### Find orphaned recommendations (product deleted)
```sql
SELECT r.* 
FROM recommendations r
LEFT JOIN products p ON r.product_id = p.id
WHERE p.id IS NULL;
```

### Reset tables (for testing)
```sql
-- Delete all recommendations first (has foreign keys)
DELETE FROM recommendations;
DELETE FROM searches;
DELETE FROM wishlist_items;
DELETE FROM reviews;
DELETE FROM products;

-- Reset sequences
ALTER SEQUENCE products_id_seq RESTART WITH 1;
ALTER SEQUENCE searches_id_seq RESTART WITH 1;
ALTER SEQUENCE recommendations_id_seq RESTART WITH 1;
ALTER SEQUENCE reviews_id_seq RESTART WITH 1;
ALTER SEQUENCE wishlist_items_id_seq RESTART WITH 1;
```

## Tips for Backend Development

1. **Test with real data**: Always test APIs with actual products from the database
2. **Use JSONB operators**: 
   - `->>` for text extraction: `specifications->>'processor'`
   - `->` for JSON value: `specifications->'ram'`
   - `@>` for containment: `features @> '"5G support"'::jsonb`
3. **Always validate foreign keys**: Check product_id exists before inserting recommendations
4. **Handle edge cases**: Empty results, null values, invalid JSON
5. **Index frequently filtered columns**: category, price, rating already indexed
6. **Monitor Gemini output**: Validate that extracted_requirements match expected structure

## Debugging Gemini Extraction

View the last few searches with extracted requirements:
```sql
SELECT id, query, status, extracted_requirements, created_at 
FROM searches 
ORDER BY created_at DESC 
LIMIT 5;
```

Check for failed searches:
```sql
SELECT id, query, error_message, created_at 
FROM searches 
WHERE status = 'failed'
ORDER BY created_at DESC;
```

View recommendations breakdown:
```sql
SELECT id, search_id, product_id, match_percentage, score_breakdown 
FROM recommendations 
ORDER BY created_at DESC 
LIMIT 10;
```
