-- ============================================================
-- ShopWise AI - Seed Products Data
-- ============================================================
-- Run this after all 5 migration scripts
-- Paste into Supabase SQL Editor and run

-- SMARTPHONES (Budget to Premium)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
-- Budget Smartphone
('Redmi Note 13', 'Xiaomi', 'smartphone', 'Great all-rounder with excellent battery life', 12999, 10, 'https://via.placeholder.com/400x300?text=Redmi+Note+13', 4.3, 1245, 
  '{"processor": "MediaTek Helio G99", "ram": 6, "storage": 128, "display": "6.67 inch IPS", "battery": 5000, "camera_main": "50MP", "camera_front": "8MP", "os": "Android 13"}'::jsonb,
  '["Fast charging", "Large display", "Good battery", "Budget friendly"]'::jsonb,
  '{"cpu_score": 85, "gaming": 75, "battery_hours": 18, "camera_score": 78}'::jsonb,
  ARRAY['Excellent battery life', 'Large 120Hz display', 'Good performance for price', 'Fast charging']::TEXT[],
  ARRAY['Mid-range camera', 'Plastic back', 'Limited RAM']::TEXT[]
);

-- Mid-range Smartphone
('Samsung Galaxy A54', 'Samsung', 'smartphone', 'Reliable mid-range with great display and camera', 28999, 5, 'https://via.placeholder.com/400x300?text=Galaxy+A54', 4.5, 3420,
  '{"processor": "Exynos 1280", "ram": 8, "storage": 128, "display": "6.4 inch AMOLED", "battery": 5000, "camera_main": "50MP", "camera_front": "32MP", "os": "Android 13"}'::jsonb,
  '["AMOLED display", "Good camera", "IP67 rating", "5G support"]'::jsonb,
  '{"cpu_score": 88, "gaming": 80, "battery_hours": 17, "camera_score": 85}'::jsonb,
  ARRAY['Beautiful AMOLED display', 'Reliable Samsung', 'IP67 dust/water resistant', 'Good all-around camera']::TEXT[],
  ARRAY['Battery degradation with time', 'Plastic body', 'Moderate performance']::TEXT[]
);

-- Premium Smartphone
('iPhone 15 Pro Max', 'Apple', 'smartphone', 'Latest flagship with exceptional camera and performance', 139999, 0, 'https://via.placeholder.com/400x300?text=iPhone+15+Pro+Max', 4.8, 5678,
  '{"processor": "A17 Pro", "ram": 8, "storage": 256, "display": "6.7 inch OLED", "battery": 4685, "camera_main": "48MP", "camera_front": "12MP", "os": "iOS 17"}'::jsonb,
  '["Titanium design", "ProMotion 120Hz", "Action button", "USB-C"]'::jsonb,
  '{"cpu_score": 98, "gaming": 95, "battery_hours": 16, "camera_score": 96}'::jsonb,
  ARRAY['Exceptional camera system', 'Ultra-fast processor', 'Beautiful display', 'Premium build quality']::TEXT[],
  ARRAY['Very expensive', 'No charger in box', 'Limited customization']::TEXT[]
);

-- Camera-focused Smartphone
('Google Pixel 8 Pro', 'Google', 'smartphone', 'AI-powered camera excellence, pure Android', 84999, 8, 'https://via.placeholder.com/400x300?text=Pixel+8+Pro', 4.7, 2890,
  '{"processor": "Google Tensor G3", "ram": 12, "storage": 256, "display": "6.7 inch OLED", "battery": 5050, "camera_main": "50MP", "camera_front": "42MP", "os": "Android 14"}'::jsonb,
  '["AI magic eraser", "Best-in-class night mode", "7 years updates", "7 years support"]'::jsonb,
  '{"cpu_score": 92, "gaming": 85, "battery_hours": 17, "camera_score": 98}'::jsonb,
  ARRAY['Outstanding AI-powered camera', 'Stock Android', 'Long software support', 'Great night mode']::TEXT[],
  ARRAY['Expensive', 'Modest battery life', 'Heating in intense tasks']::TEXT[]
);

-- Budget Gaming Smartphone
('OnePlus 12', 'OnePlus', 'smartphone', 'Fast performance, excellent for gaming', 45999, 12, 'https://via.placeholder.com/400x300?text=OnePlus+12', 4.6, 4123,
  '{"processor": "Snapdragon 8 Gen 3", "ram": 12, "storage": 256, "display": "6.82 inch AMOLED", "battery": 5400, "camera_main": "50MP", "camera_front": "16MP", "os": "Android 14"}'::jsonb,
  '["Snapdragon 8 Gen 3", "120Hz AMOLED", "Fast charging", "Gaming focus"]'::jsonb,
  '{"cpu_score": 96, "gaming": 94, "battery_hours": 18, "camera_score": 82}'::jsonb,
  ARRAY['Top-tier performance', 'Smooth 120Hz display', 'Great for gaming', 'Fast charging']::TEXT[],
  ARRAY['Average camera', 'No SD card slot', 'OxygenOS bloat']::TEXT[]
);

-- LAPTOPS (Budget to High-end)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
-- Budget Laptop
('ASUS VivoBook 14', 'ASUS', 'laptop', 'Perfect for students and everyday work', 34999, 15, 'https://via.placeholder.com/400x300?text=ASUS+VivoBook+14', 4.2, 890,
  '{"processor": "Intel Core i5-11th Gen", "ram": 8, "storage": 512, "storage_type": "SSD", "display": "14 inch FHD", "gpu": "Intel Iris Xe", "battery_life": "8 hours", "weight": "1.55 kg", "os": "Windows 11"}'::jsonb,
  '["Lightweight", "Long battery", "FHD display", "Fast SSD"]'::jsonb,
  '{"cpu_score": 72, "multitask": 78, "battery_hours": 8, "gaming": 35}'::jsonb,
  ARRAY['Very lightweight and portable', 'Good battery life', 'Budget friendly', 'Good for productivity']::TEXT[],
  ARRAY['Entry-level performance', 'Plastic build', 'Limited upgrade options', 'Thermal throttling under load']::TEXT[]
);

-- Mid-range Laptop
('Lenovo ThinkBook 14', 'Lenovo', 'laptop', 'Reliable workhorse for professionals', 54999, 10, 'https://via.placeholder.com/400x300?text=ThinkBook+14', 4.4, 1234,
  '{"processor": "Intel Core i7-12th Gen", "ram": 16, "storage": 512, "storage_type": "SSD", "display": "14 inch IPS FHD", "gpu": "Intel Iris Xe", "battery_life": "10 hours", "weight": "1.7 kg", "os": "Windows 11 Pro"}'::jsonb,
  '["Business class", "Durable design", "Professional keyboard", "Upgrade friendly"]'::jsonb,
  '{"cpu_score": 84, "multitask": 90, "battery_hours": 10, "gaming": 45}'::jsonb,
  ARRAY['Excellent keyboard', 'Durable build quality', 'Good performance', 'Business-focused']::TEXT[],
  ARRAY['Average speakers', 'Not for gaming', 'Modest storage for price']::TEXT[]
);

-- Performance Laptop
('Dell XPS 14', 'Dell', 'laptop', 'Premium ultrabook with excellent display', 94999, 0, 'https://via.placeholder.com/400x300?text=Dell+XPS+14', 4.6, 2145,
  '{"processor": "Intel Core i7-13th Gen", "ram": 16, "storage": 1024, "storage_type": "SSD", "display": "14 inch OLED", "gpu": "Intel Iris Xe", "battery_life": "12 hours", "weight": "1.65 kg", "os": "Windows 11"}'::jsonb,
  '["Premium OLED display", "Lightweight", "Thunderbolt 4", "Excellent design"]'::jsonb,
  '{"cpu_score": 88, "multitask": 92, "battery_hours": 12, "gaming": 55}'::jsonb,
  ARRAY['Stunning OLED display', 'Premium design', 'Good performance', 'Long battery life']::TEXT[],
  ARRAY['Expensive', 'Limited upgrade options', 'Can run hot under load']::TEXT[]
);

-- Gaming Laptop
('ASUS ROG Zephyrus G14', 'ASUS', 'laptop', 'Portable gaming powerhouse', 129999, 5, 'https://via.placeholder.com/400x300?text=ROG+Zephyrus+G14', 4.7, 3456,
  '{"processor": "Intel Core i9-13th Gen", "ram": 32, "storage": 1024, "storage_type": "SSD", "display": "14 inch FHD 165Hz", "gpu": "NVIDIA RTX 4050", "battery_life": "6 hours", "weight": "1.9 kg", "os": "Windows 11"}'::jsonb,
  '["165Hz gaming display", "RTX 4050 GPU", "High refresh rate", "Compact gaming"]'::jsonb,
  '{"cpu_score": 95, "multitask": 94, "battery_hours": 6, "gaming": 92}'::jsonb,
  ARRAY['Excellent for gaming', 'Fast 165Hz display', 'Portable gaming rig', 'High performance']::TEXT[],
  ARRAY['Short battery life', 'Runs hot and loud', 'Expensive', 'Limited ports']::TEXT[]
);

-- Workstation Laptop
('MacBook Pro 16 M3 Max', 'Apple', 'laptop', 'Professional-grade performance with macOS', 199999, 0, 'https://via.placeholder.com/400x300?text=MacBook+Pro+16', 4.8, 4567,
  '{"processor": "Apple M3 Max", "ram": 36, "storage": 1024, "storage_type": "SSD", "display": "16 inch Liquid Retina XDR", "gpu": "18-core GPU", "battery_life": "18 hours", "weight": "2.15 kg", "os": "macOS Sonoma"}'::jsonb,
  '["XDR display", "ProMotion 120Hz", "Excellent trackpad", "Long battery"]'::jsonb,
  '{"cpu_score": 98, "multitask": 98, "battery_hours": 18, "gaming": 60}'::jsonb,
  ARRAY['Exceptional display', 'Outstanding build quality', 'Best battery life', 'Professional ecosystem']::TEXT[],
  ARRAY['Very expensive', 'Limited game compatibility', 'Proprietary ecosystem', 'No upgrade options']::TEXT[]
);

-- HEADPHONES (Budget to Premium)
INSERT INTO products (name, brand, category, description, price, discount_percentage, image_url, rating, review_count, specifications, features, performance, pros, cons) VALUES
-- Budget Headphones
('Redmi Buds 5', 'Xiaomi', 'headphones', 'Affordable truly wireless earbuds with decent sound', 1999, 20, 'https://via.placeholder.com/400x300?text=Redmi+Buds+5', 4.1, 567,
  '{"type": "true wireless", "driver_size": "6mm", "frequency_response": "20Hz-20kHz", "impedance": 32, "battery_life": "8 hours", "noise_cancellation": false, "connectivity": "Bluetooth 5.3"}'::jsonb,
  '["Lightweight", "IPX5 water resistant", "Good battery", "Budget friendly"]'::jsonb,
  '{"sound_quality": 72, "bass": 68, "clarity": 75, "comfort": 80}'::jsonb,
  ARRAY['Very affordable', 'Good battery life', 'Water resistant', 'Lightweight']::TEXT[],
  ARRAY['No active noise cancellation', 'Average sound quality', 'Limited features']::TEXT[]
);

-- Mid-range Headphones
('Sony WH-CH720N', 'Sony', 'headphones', 'Comfortable noise-cancelling headphones', 8999, 10, 'https://via.placeholder.com/400x300?text=Sony+WH-CH720N', 4.4, 2134,
  '{"type": "over-ear", "driver_size": "30mm", "frequency_response": "20Hz-20kHz", "impedance": 32, "battery_life": "35 hours", "noise_cancellation": true, "connectivity": "Bluetooth 5.3"}'::jsonb,
  '["Active noise cancellation", "Long battery", "Comfortable fit", "Multipoint connection"]'::jsonb,
  '{"sound_quality": 82, "bass": 80, "clarity": 84, "comfort": 88}'::jsonb,
  ARRAY['Great noise cancellation', 'Excellent battery life', 'Very comfortable', 'Great value']::TEXT[],
  ARRAY['Plastic build', 'Average microphone', 'Limited customization']::TEXT[]
);

-- Premium Headphones
('Bose QuietComfort 45', 'Bose', 'headphones', 'Industry-leading noise cancellation', 27999, 0, 'https://via.placeholder.com/400x300?text=Bose+QuietComfort+45', 4.7, 3890,
  '{"type": "over-ear", "driver_size": "40mm", "frequency_response": "20Hz-20kHz", "impedance": 32, "battery_life": "24 hours", "noise_cancellation": true, "connectivity": "Bluetooth 5.3"}'::jsonb,
  '["Best-in-class ANC", "Premium sound", "Luxe comfort", "Multipoint audio"]'::jsonb,
  '{"sound_quality": 90, "bass": 85, "clarity": 92, "comfort": 95}'::jsonb,
  ARRAY['Best noise cancellation', 'Premium sound quality', 'Extremely comfortable', 'Great for travel']::TEXT[],
  ARRAY['Expensive', 'Plastic earcups', 'No app customization']::TEXT[]
);

-- Gaming Headphones
('SteelSeries Arctis Nova 1', 'SteelSeries', 'headphones', 'Gaming headphones with exceptional microphone', 19999, 5, 'https://via.placeholder.com/400x300?text=Arctis+Nova+1', 4.5, 1567,
  '{"type": "over-ear", "driver_size": "40mm", "frequency_response": "20Hz-20kHz", "impedance": 32, "battery_life": "30 hours wireless", "noise_cancellation": false, "connectivity": "Bluetooth + USB dongle"}'::jsonb,
  '["Excellent microphone", "Gaming focus", "Comfortable long wear", "Good soundstage"]'::jsonb,
  '{"sound_quality": 85, "bass": 82, "clarity": 87, "comfort": 90}'::jsonb,
  ARRAY['Exceptional microphone', 'Very comfortable', 'Great for gaming', 'Good sound quality']::TEXT[],
  ARRAY['No active noise cancellation', 'Wired option only', 'Expensive for gaming']::TEXT[]
);

-- Premium Wireless Earbuds
('Apple AirPods Pro 2', 'Apple', 'headphones', 'Premium wireless earbuds with spatial audio', 26999, 0, 'https://via.placeholder.com/400x300?text=AirPods+Pro+2', 4.8, 5123,
  '{"type": "true wireless", "driver_size": "custom", "frequency_response": "20Hz-20kHz", "impedance": "N/A", "battery_life": "6 hours", "noise_cancellation": true, "connectivity": "Bluetooth 5.3"}'::jsonb,
  '["Active noise cancellation", "Spatial audio", "Adaptive audio", "Seamless integration"]'::jsonb,
  '{"sound_quality": 94, "bass": 88, "clarity": 96, "comfort": 92}'::jsonb,
  ARRAY['Best-in-class noise cancellation', 'Spatial audio', 'Perfect for Apple ecosystem', 'Excellent sound']::TEXT[],
  ARRAY['Expensive', 'Poor non-Apple experience', 'Easy to lose', 'Limited customization']::TEXT[]
);

-- Verify insertion
SELECT COUNT(*) as total_products FROM products;
SELECT category, COUNT(*) as count FROM products GROUP BY category;
