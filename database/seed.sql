-- ============================================================
-- ShopWise AI - Seed Data
-- PostgreSQL/Supabase SQL
-- Run this AFTER schema.sql
-- ============================================================

-- ============================================================
-- CREATE TEST USERS
-- ============================================================
INSERT INTO users (email, name) VALUES
('alice@example.com', 'Alice Johnson'),
('bob@example.com', 'Bob Smith');

-- Get user IDs for foreign keys
-- (In real usage, you'd retrieve these from the INSERT above)
-- For this script, we'll insert preferences for the created users

INSERT INTO user_preferences (user_id, preferences)
SELECT id, '{"budget_range": {"min": 20000, "max": 100000}, "preferred_brands": ["Apple", "Samsung"], "priority_features": ["camera", "performance"]}'::jsonb
FROM users WHERE email = 'alice@example.com';

INSERT INTO user_preferences (user_id, preferences)
SELECT id, '{"budget_range": {"min": 30000, "max": 150000}, "preferred_brands": ["Dell", "Lenovo", "ASUS"], "priority_features": ["performance", "battery"]}'::jsonb
FROM users WHERE email = 'bob@example.com';

-- ============================================================
-- SEED PRODUCTS - 10 Total
-- ============================================================

-- SMARTPHONES (3)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('iPhone 15 Pro Max', 'Apple', 'smartphone', 'Latest flagship with exceptional camera and performance', 139999, 0, 'https://images.unsplash.com/photo-1592286927505-1def25115558?w=400&q=80', 4.8, 2345, 
  '{"processor": "A17 Pro", "ram": 8, "storage": 256, "display": "6.7 inch OLED", "battery": 4685, "camera_main": "48MP", "camera_front": "12MP", "os": "iOS 17"}'::jsonb,
  '["Titanium design", "Action button", "Excellent camera", "USB-C"]'::jsonb,
  '{"cpu_score": 98, "gaming": 95, "battery_hours": 16, "camera_score": 96}'::jsonb,
  '["Exceptional camera", "Ultra-fast processor", "Premium design"]'::jsonb,
  '["Very expensive", "Limited customization", "No charger in box"]'::jsonb
);

INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('Samsung Galaxy A54', 'Samsung', 'smartphone', 'Reliable mid-range with great display and camera', 28999, 5, 'https://images.unsplash.com/photo-1511707267537-b85faf00021e?w=400&q=80', 4.5, 1876, 
  '{"processor": "Exynos 1280", "ram": 8, "storage": 128, "display": "6.4 inch AMOLED", "battery": 5000, "camera_main": "50MP", "camera_front": "32MP", "os": "Android 13"}'::jsonb,
  '["AMOLED display", "Good camera", "IP67 rating", "5G support"]'::jsonb,
  '{"cpu_score": 88, "gaming": 80, "battery_hours": 17, "camera_score": 85}'::jsonb,
  '["Beautiful display", "Reliable brand", "Good battery"]'::jsonb,
  '["Mid-range processor", "Not latest Android", "Plastic body"]'::jsonb
);

INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('Google Pixel 8 Pro', 'Google', 'smartphone', 'AI-powered camera excellence, pure Android', 84999, 8, 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=400&q=80', 4.7, 2103, 
  '{"processor": "Google Tensor G3", "ram": 12, "storage": 256, "display": "6.7 inch OLED", "battery": 5050, "camera_main": "50MP", "camera_front": "42MP", "os": "Android 14"}'::jsonb,
  '["AI magic eraser", "Best night mode", "7 years updates", "Stock Android"]'::jsonb,
  '{"cpu_score": 92, "gaming": 85, "battery_hours": 17, "camera_score": 98}'::jsonb,
  '["Outstanding camera", "Long software support", "Pure Android"]'::jsonb,
  '["Expensive", "Heating issues", "Limited availability"]'::jsonb
);

-- LAPTOPS (2)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('Dell XPS 14', 'Dell', 'laptop', 'Premium ultrabook with excellent display and performance', 94999, 0, 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400&q=80', 4.6, 1543, 
  '{"processor": "Intel Core i7-13th Gen", "ram": 16, "storage": 1024, "storage_type": "SSD", "display": "14 inch OLED", "gpu": "Intel Iris Xe", "battery_life": "12 hours", "weight": "1.65 kg", "os": "Windows 11"}'::jsonb,
  '["Premium OLED display", "Lightweight", "Thunderbolt 4", "Excellent design"]'::jsonb,
  '{"cpu_score": 88, "multitask": 92, "battery_hours": 12, "gaming": 55}'::jsonb,
  '["Stunning display", "Premium build", "Long battery life"]'::jsonb,
  '["Expensive", "Limited upgrade options", "Thermal throttling"]'::jsonb
);

INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('ASUS ROG Zephyrus G14', 'ASUS', 'laptop', 'Portable gaming powerhouse with RTX GPU', 129999, 5, 'https://images.unsplash.com/photo-1588872657840-218e412ee62e?w=400&q=80', 4.7, 2234, 
  '{"processor": "Intel Core i9-13th Gen", "ram": 32, "storage": 1024, "storage_type": "SSD", "display": "14 inch FHD 165Hz", "gpu": "NVIDIA RTX 4050", "battery_life": "6 hours", "weight": "1.9 kg", "os": "Windows 11"}'::jsonb,
  '["165Hz gaming display", "RTX 4050 GPU", "Compact gaming", "High refresh rate"]'::jsonb,
  '{"cpu_score": 95, "multitask": 94, "battery_hours": 6, "gaming": 92}'::jsonb,
  '["Excellent for gaming", "Fast display", "Portable rig"]'::jsonb,
  '["Short battery", "Runs hot", "Very expensive"]'::jsonb
);

-- HEADPHONES (2)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('Sony WH-1000XM5', 'Sony', 'headphones', 'Industry-leading noise cancellation headphones', 27999, 0, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&q=80', 4.8, 3456, 
  '{"type": "over-ear", "driver_size": "40mm", "frequency_response": "20Hz-20kHz", "impedance": 32, "battery_life": "30 hours", "noise_cancellation": true, "connectivity": "Bluetooth 5.3"}'::jsonb,
  '["Best-in-class ANC", "Premium sound", "30-hour battery", "Multipoint connection"]'::jsonb,
  '{"sound_quality": 95, "bass": 88, "clarity": 94, "comfort": 92}'::jsonb,
  '["Best noise cancellation", "Exceptional sound", "Very comfortable", "Long battery"]'::jsonb,
  '["Very expensive", "Plastic earcups", "No physical buttons"]'::jsonb
);

INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('Apple AirPods Pro 2', 'Apple', 'headphones', 'Premium wireless earbuds with spatial audio', 26999, 0, 'https://images.unsplash.com/photo-1606841837239-c5a1a3a0d4d0?w=400&q=80', 4.7, 2891, 
  '{"type": "true wireless", "driver_size": "custom", "frequency_response": "20Hz-20kHz", "impedance": "N/A", "battery_life": "6 hours", "noise_cancellation": true, "connectivity": "Bluetooth 5.3"}'::jsonb,
  '["Active noise cancellation", "Spatial audio", "Adaptive audio", "Seamless integration"]'::jsonb,
  '{"sound_quality": 93, "bass": 86, "clarity": 95, "comfort": 90}'::jsonb,
  '["Best ANC earbuds", "Spatial audio", "Perfect for Apple", "Excellent sound"]'::jsonb,
  '["Expensive", "Poor non-Apple experience", "Easy to lose"]'::jsonb
);

-- SMARTWATCH (1)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('Apple Watch Ultra', 'Apple', 'smartwatch', 'Rugged smartwatch for outdoor adventures', 89999, 0, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&q=80', 4.6, 1234, 
  '{"display": "1.92 inch Retina LTPO OLED", "processor": "S9 SiP", "storage": 32, "battery_life": "36 hours", "water_resistance": "100m", "case_material": "Titanium", "os": "watchOS 9"}'::jsonb,
  '["Rugged titanium", "100m water resistant", "Action button", "Long battery", "Sports focused"]'::jsonb,
  '{"performance": 90, "battery_hours": 36, "durability": 95}'::jsonb,
  '["Very durable", "Excellent for sports", "Amazing battery", "Outdoor focused"]'::jsonb,
  '["Very expensive", "Limited to Apple ecosystem", "Overkill for daily use"]'::jsonb
);

-- TABLET (1)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('iPad Pro 12.9', 'Apple', 'tablet', 'Powerful tablet for creative professionals', 119999, 0, 'https://images.unsplash.com/photo-1561154464-b534a97ba26e?w=400&q=80', 4.7, 2567, 
  '{"processor": "Apple M2", "ram": 8, "storage": 256, "display": "12.9 inch Liquid Retina XDR", "refresh_rate": "120Hz", "cameras": "12MP main + 12MP ultrawide", "connectivity": "Wi-Fi 6E", "os": "iPadOS 17"}'::jsonb,
  '["M2 processor", "XDR display", "120Hz ProMotion", "Apple Pencil support", "Magic Keyboard compatible"]'::jsonb,
  '{"cpu_score": 94, "display_quality": 98, "battery_hours": 10, "creative_performance": 96}'::jsonb,
  '["Powerful M2 chip", "Stunning display", "Great for creation", "Long battery"]'::jsonb,
  '["Very expensive", "iPadOS limitations", "Needs accessories", "Not a laptop replacement"]'::jsonb
);

-- CAMERA (1)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
('Sony A6700', 'Sony', 'camera', 'Advanced mirrorless camera for content creators', 149999, 0, 'https://images.unsplash.com/photo-1612198188060-c7c2a3b66eae?w=400&q=80', 4.8, 876, 
  '{"sensor": "APS-C 26.1MP Exmor R", "processor": "BIONZ XR", "viewfinder": "0.59 inch OLED", "shutter_speed": "1/8000 sec", "video": "4K 120fps", "autofocus": "759 contrast AF points", "weight": "565g", "connectivity": "USB-C, Wi-Fi 6E"}'::jsonb,
  '["26MP APS-C sensor", "4K 120fps video", "Advanced autofocus", "Compact and light", "Weather sealed"]'::jsonb,
  '{"photo_quality": 96, "video_quality": 95, "autofocus_speed": 94, "build_quality": 92}'::jsonb,
  '["Excellent image quality", "Great video capabilities", "Advanced autofocus", "Compact design"]'::jsonb,
  '["Expensive", "Steep learning curve", "Lens ecosystem cost", "Battery life average"]'::jsonb
);

-- ============================================================
-- SEED REVIEWS - Multiple reviews per product
-- ============================================================

-- iPhone 15 Pro Max reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Best iPhone yet!', 'The camera is absolutely incredible. Night mode is a game changer. Worth every penny.' 
FROM products WHERE name = 'iPhone 15 Pro Max' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Perfect phone', 'Premium build, amazing performance, and the titanium design is beautiful.' 
FROM products WHERE name = 'iPhone 15 Pro Max' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 4, 'Great but pricey', 'Excellent phone but the price is hard to justify compared to competitors.' 
FROM products WHERE name = 'iPhone 15 Pro Max' LIMIT 1;

-- Samsung Galaxy A54 reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Best mid-range', 'Great AMOLED display, good camera, and reliable. Perfect value for money.' 
FROM products WHERE name = 'Samsung Galaxy A54' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 4, 'Solid choice', 'Good all-around phone. Battery lasts all day. Not as fast as flagships but very reliable.' 
FROM products WHERE name = 'Samsung Galaxy A54' LIMIT 1;

-- Google Pixel 8 Pro reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Camera perfection', 'The AI features are mind-blowing. Night mode is better than any other phone. Pure Android is great too.' 
FROM products WHERE name = 'Google Pixel 8 Pro' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Exceptional all-rounder', 'Fast processor, beautiful display, and the software experience is unmatched.' 
FROM products WHERE name = 'Google Pixel 8 Pro' LIMIT 1;

-- Dell XPS 14 reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Premium ultrabook', 'The OLED display is stunning. Perfect for work and content creation. Very lightweight.' 
FROM products WHERE name = 'Dell XPS 14' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 4, 'Excellent build quality', 'Premium feel, great keyboard, but gets a bit warm under heavy load.' 
FROM products WHERE name = 'Dell XPS 14' LIMIT 1;

-- ASUS ROG Zephyrus G14 reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Gaming beast', 'Incredible performance for gaming. RTX 4050 handles everything. Great for streaming too.' 
FROM products WHERE name = 'ASUS ROG Zephyrus G14' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Portable powerhouse', 'Best gaming laptop for travel. Fast and compact. Only downside is battery life while gaming.' 
FROM products WHERE name = 'ASUS ROG Zephyrus G14' LIMIT 1;

-- Sony WH-1000XM5 reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Noise cancellation king', 'Best noise cancelling headphones I have ever used. Sound quality is excellent.' 
FROM products WHERE name = 'Sony WH-1000XM5' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Perfect for travel', 'Amazing ANC, comfortable for long wear, excellent bass. Worth the investment.' 
FROM products WHERE name = 'Sony WH-1000XM5' LIMIT 1;

-- Apple AirPods Pro 2 reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Best wireless earbuds', 'Spatial audio is incredible. ANC works great. Perfect if you use Apple devices.' 
FROM products WHERE name = 'Apple AirPods Pro 2' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 4, 'Excellent but pricey', 'Very good earbuds but limited functionality outside Apple ecosystem.' 
FROM products WHERE name = 'Apple AirPods Pro 2' LIMIT 1;

-- Apple Watch Ultra reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Built to last', 'Rugged and durable. Perfect for outdoor activities. Battery lasts forever.' 
FROM products WHERE name = 'Apple Watch Ultra' LIMIT 1;

-- iPad Pro 12.9 reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Creative powerhouse', 'XDR display is phenomenal. M2 handles all creative apps. Great with Apple Pencil.' 
FROM products WHERE name = 'iPad Pro 12.9' LIMIT 1;

-- Sony A6700 reviews
INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Creator\'s dream', 'Exceptional image quality. 4K 120fps is fantastic for video. Autofocus is flawless.' 
FROM products WHERE name = 'Sony A6700' LIMIT 1;

INSERT INTO reviews (product_id, rating, title, content) 
SELECT id, 5, 'Professional results', 'Build quality is amazing. BIONZ XR processor is fast. Perfect for content creators.' 
FROM products WHERE name = 'Sony A6700' LIMIT 1;

-- ============================================================
-- VERIFY SEED DATA
-- ============================================================
SELECT 'Users' as table_name, COUNT(*) as count FROM users
UNION ALL
SELECT 'User Preferences', COUNT(*) FROM user_preferences
UNION ALL
SELECT 'Products', COUNT(*) FROM products
UNION ALL
SELECT 'Reviews', COUNT(*) FROM reviews
UNION ALL
SELECT 'Wishlist Items', COUNT(*) FROM wishlist_items;
