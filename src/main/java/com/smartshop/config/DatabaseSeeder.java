package com.smartshop.config;

import com.smartshop.entity.Product;
import com.smartshop.entity.Review;
import com.smartshop.entity.User;
import com.smartshop.entity.UserPreference;
import com.smartshop.repository.ProductRepository;
import com.smartshop.repository.ReviewRepository;
import com.smartshop.repository.UserPreferenceRepository;
import com.smartshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    public DatabaseSeeder(ProductRepository productRepository,
                          ReviewRepository reviewRepository,
                          UserRepository userRepository,
                          UserPreferenceRepository userPreferenceRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            log.info("No existing products found. Seeding local database with standard catalog and sample reviews...");
            seedData();
            log.info("Local database seeding completed successfully ({} products, {} reviews loaded).",
                    productRepository.count(), reviewRepository.count());
        } else {
            log.info("Database already contains {} products. Skipping automatic seed.", productRepository.count());
        }
    }

    private void seedData() {
        // Users
        User alice = new User();
        alice.setName("Alice Johnson");
        alice.setEmail("alice@example.com");
        alice = userRepository.save(alice);

        User bob = new User();
        bob.setName("Bob Smith");
        bob.setEmail("bob@example.com");
        bob = userRepository.save(bob);

        UserPreference alicePref = new UserPreference();
        alicePref.setUserId(alice.getId());
        alicePref.setPreferences("{\"budget_range\": {\"min\": 20000, \"max\": 40000}, \"preferred_brands\": [\"OnePlus\", \"Samsung\"], \"priority_features\": [\"camera\", \"battery\"]}");
        userPreferenceRepository.save(alicePref);

        // Products
        Product p1 = createProduct("iPhone 15 Pro Max", "Apple", "smartphone",
                "Apple flagship smartphone with titanium design and A17 Pro chip",
                159900, 5, "https://images.unsplash.com/photo-1510557880182-3d4d3cba35a5?w=400&q=80",
                4.8, 3420,
                "{\"processor\": \"A17 Pro\", \"ram\": 8, \"storage\": 256, \"display\": \"6.7 inch Super Retina XDR OLED\", \"refresh_rate\": \"120Hz\", \"cameras\": \"48MP main + 12MP ultrawide + 12MP 5x telephoto\", \"battery_mah\": 4422, \"weight\": \"221g\", \"os\": \"iOS 17\"}",
                "[\"Action button\", \"Dynamic Island\", \"USB-C with USB 3 speeds\", \"Always-On display\", \"Titanium design\"]",
                "{\"cpu_score\": 98, \"gaming_score\": 96, \"camera_score\": 99, \"battery_hours\": 14}",
                "[\"Incredible camera system\", \"Titanium build quality\", \"Excellent battery life\", \"Fast A17 Pro performance\"]",
                "[\"Extremely expensive\", \"Slow 20W charging\", \"Heavy for some users\"]",
                true, 50);

        Product p2 = createProduct("Samsung Galaxy A54", "Samsung", "smartphone",
                "Value-packed mid-range smartphone with versatile camera and long battery",
                34999, 12, "https://images.unsplash.com/photo-1580910051074-3eb694886505?w=400&q=80",
                4.3, 1850,
                "{\"processor\": \"Exynos 1380\", \"ram\": 8, \"storage\": 128, \"display\": \"6.4 inch Super AMOLED\", \"refresh_rate\": \"120Hz\", \"cameras\": \"50MP main + 12MP ultrawide + 5MP macro\", \"battery_mah\": 5000, \"weight\": \"202g\", \"os\": \"Android 13\"}",
                "[\"Super AMOLED display\", \"5000mAh battery\", \"IP67 water resistance\", \"Expandable storage up to 1TB\", \"4 years OS updates\"]",
                "{\"cpu_score\": 75, \"gaming_score\": 70, \"camera_score\": 82, \"battery_hours\": 16}",
                "[\"Great AMOLED display\", \"Solid camera for price\", \"Reliable battery life\", \"IP67 rating\"]",
                "[\"Average gaming performance\", \"Thick bezels\", \"No charger in box\"]",
                true, 120);

        Product p3 = createProduct("Google Pixel 8 Pro", "Google", "smartphone",
                "AI-powered flagship with industry-leading photography capabilities",
                106999, 8, "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=400&q=80",
                4.6, 2190,
                "{\"processor\": \"Google Tensor G3\", \"ram\": 12, \"storage\": 128, \"display\": \"6.7 inch LTPO OLED\", \"refresh_rate\": \"120Hz\", \"cameras\": \"50MP main + 48MP ultrawide + 48MP 5x telephoto\", \"battery_mah\": 5050, \"weight\": \"213g\", \"os\": \"Android 14\"}",
                "[\"Best-in-class AI camera features\", \"7 years OS updates\", \"Temperature sensor\", \"Pure Android UI\", \"Magic Audio Eraser\"]",
                "{\"cpu_score\": 88, \"gaming_score\": 80, \"camera_score\": 98, \"battery_hours\": 13}",
                "[\"Unmatched camera photo quality\", \"7 years software support\", \"Clean software experience\", \"Bright display\"]",
                "[\"Tensor G3 runs warm\", \"Slower charging speed\", \"Battery life is average\"]",
                true, 35);

        Product p4 = createProduct("OnePlus Nord 3 5G", "OnePlus", "smartphone",
                "Flagship killer mid-ranger with high-speed performance and fast charging",
                28999, 10, "https://images.unsplash.com/photo-1565849904461-04a58ad377e0?w=400&q=80",
                4.4, 980,
                "{\"processor\": \"MediaTek Dimensity 9000\", \"ram\": 16, \"storage\": 256, \"display\": \"6.74 inch Super Fluid AMOLED\", \"refresh_rate\": \"120Hz\", \"cameras\": \"50MP Sony IMX890 OIS + 8MP + 2MP\", \"battery_mah\": 5000, \"weight\": \"193g\", \"os\": \"OxygenOS 13.1\"}",
                "[\"80W SuperVOOC fast charging\", \"50MP Sony IMX890 sensor with OIS\", \"16GB RAM for multitasking\", \"Alert slider\", \"Dual stereo speakers\"]",
                "{\"cpu_score\": 90, \"gaming_score\": 88, \"camera_score\": 85, \"battery_hours\": 15}",
                "[\"Flagship level Dimensity 9000 chip\", \"Blazing 80W charging\", \"Clean OxygenOS\", \"120Hz smooth AMOLED\"]",
                "[\"Average secondary cameras\", \"Plastic frame build\", \"No IP rating\"]",
                true, 85);

        Product p5 = createProduct("Dell XPS 14", "Dell", "laptop",
                "Premium lightweight ultrabook for productivity, development, and content creation",
                169990, 6, "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=400&q=80",
                4.5, 1120,
                "{\"processor\": \"Intel Core Ultra 7 155H\", \"ram\": 32, \"storage\": 1024, \"display\": \"14.5 inch 3.2K OLED Touch\", \"gpu\": \"NVIDIA RTX 4050 6GB\", \"weight\": \"1.68kg\", \"battery_wh\": 69.5, \"os\": \"Windows 11 Home\"}",
                "[\"3.2K InfinityEdge OLED touch display\", \"Intel AI Boost NPU\", \"Glass haptic touchpad\", \"CNC machined aluminum build\", \"32GB LPDDR5X RAM\"]",
                "{\"cpu_score\": 92, \"coding_score\": 96, \"gpu_score\": 84, \"battery_hours\": 11}",
                "[\"Gorgeous 3.2K OLED screen\", \"Superb build quality\", \"Excellent performance for coding and editing\", \"Seamless haptic touchpad\"]",
                "[\"Expensive price point\", \"Limited port selection (USB-C only)\", \"Touch function keys take getting used to\"]",
                true, 25);

        Product p6 = createProduct("ASUS ROG Zephyrus G14", "ASUS", "laptop",
                "Ultraportable gaming and programming powerhouse with OLED display and RTX graphics",
                144990, 8, "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=400&q=80",
                4.7, 1450,
                "{\"processor\": \"AMD Ryzen 9 8945HS\", \"ram\": 16, \"storage\": 1024, \"display\": \"14 inch 3K 120Hz OLED ROG Nebula\", \"gpu\": \"NVIDIA RTX 4060 8GB\", \"weight\": \"1.5kg\", \"battery_wh\": 73, \"os\": \"Windows 11 Home\"}",
                "[\"3K 120Hz OLED ROG Nebula Display\", \"Ryzen AI processor\", \"RTX 4060 GPU\", \"Slash Lighting on lid\", \"Quad speaker system with Dolby Atmos\"]",
                "{\"cpu_score\": 95, \"coding_score\": 95, \"gaming_score\": 93, \"battery_hours\": 10}",
                "[\"Incredible OLED display\", \"Remarkable compact 1.5kg weight\", \"Superb gaming and coding performance\", \"Great keyboard\"]",
                "[\"Soldered RAM (non-upgradeable)\", \"Can run warm during heavy gaming\", \"No Ethernet port\"]",
                true, 40);

        Product p7 = createProduct("Sony WH-1000XM5", "Sony", "headphones",
                "Industry-leading active noise cancelling wireless over-ear headphones",
                27999, 15, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&q=80",
                4.7, 4820,
                "{\"driver_size\": \"30mm carbon fiber\", \"battery_life\": \"30 hours with ANC\", \"charging\": \"USB-C with quick charge (3 min for 3 hrs)\", \"weight\": \"250g\", \"bluetooth\": \"5.2\", \"codecs\": \"LDAC, AAC, SBC\"}",
                "[\"Industry-leading Active Noise Cancellation\", \"Auto NC Optimizer\", \"Speak-to-Chat technology\", \"Multipoint Bluetooth connection\", \"30 hours battery backup\"]",
                "{\"anc_score\": 99, \"audio_score\": 95, \"call_quality\": 92, \"battery_hours\": 30}",
                "[\"Best-in-class noise cancellation\", \"Exceptional audio quality with LDAC\", \"Extremely lightweight and comfortable\", \"Superb battery life\"]",
                "[\"Does not fold as compactly as XM4\", \"Non-waterproof design\"]",
                true, 60);

        Product p8 = createProduct("Apple AirPods Pro 2", "Apple", "headphones",
                "Active noise cancellation wireless earbuds with Transparency mode and Spatial Audio",
                24900, 7, "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=400&q=80",
                4.6, 6100,
                "{\"chip\": \"Apple H2\", \"battery_life\": \"6 hours (30 hours with case)\", \"charging\": \"MagSafe, USB-C, Apple Watch charger\", \"water_resistance\": \"IP54\", \"weight\": \"5.3g per earbud\"}",
                "[\"2x more Active Noise Cancellation\", \"Adaptive Audio & Transparency mode\", \"Personalized Spatial Audio with dynamic head tracking\", \"MagSafe Case with speaker & lanyard loop\"]",
                "{\"anc_score\": 94, \"audio_score\": 92, \"comfort_score\": 96, \"battery_hours\": 30}",
                "[\"Outstanding ANC in earbud form\", \"Seamless Apple device switching\", \"Superb transparency mode\", \"Compact pocketable case\"]",
                "[\"Full feature set exclusive to Apple ecosystem\", \"Cannot manually adjust EQ profiles\"]",
                true, 90);

        Product p9 = createProduct("Apple Watch Ultra", "Apple", "smartwatch",
                "Rugged titanium GPS + Cellular smartwatch designed for endurance and adventure",
                89999, 5, "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&q=80",
                4.6, 1234,
                "{\"display\": \"1.92 inch Retina LTPO OLED 2000 nits\", \"processor\": \"S9 SiP\", \"storage\": 32, \"battery_life\": \"36 hours (72h low power)\", \"water_resistance\": \"100m\", \"case_material\": \"Titanium\"}",
                "[\"Rugged titanium aerospace casing\", \"100m water resistant\", \"Customizable Action button\", \"Precision dual-frequency GPS\", \"Dual speakers & 86dB siren\"]",
                "{\"performance\": 90, \"battery_hours\": 36, \"durability\": 95}",
                "[\"Ultra-durable titanium construction\", \"Bright 2000 nits display in direct sunlight\", \"Multi-day battery life for Apple Watch\", \"Precision GPS tracking\"]",
                "[\"Very high price\", \"Bulky on smaller wrists\", \"iPhone required\"]",
                true, 15);

        Product p10 = createProduct("Sony A6700", "Sony", "camera",
                "Advanced APS-C mirrorless camera for content creators, vloggers, and photographers",
                149999, 10, "https://images.unsplash.com/photo-1612198188060-c7c2a3b66eae?w=400&q=80",
                4.8, 876,
                "{\"sensor\": \"APS-C 26.0MP Exmor R BSI CMOS\", \"processor\": \"BIONZ XR\", \"video\": \"4K 120p, 10-bit 4:2:2\", \"autofocus\": \"759 phase detection points with AI subject tracking\", \"weight\": \"493g\"}",
                "[\"26.0MP APS-C BSI sensor\", \"Dedicated AI processing unit for subject tracking\", \"4K 120p video recording\", \"5-axis in-body image stabilization (IBIS)\", \"Fully articulating touchscreen\"]",
                "{\"photo_quality\": 96, \"video_quality\": 95, \"autofocus_speed\": 97, \"build_quality\": 92}",
                "[\"Class-leading AI autofocus tracking\", \"Superb 4K 10-bit video quality\", \"Compact and lightweight body\", \"5-axis IBIS stabilization\"]",
                "[\"Single UHS-II SD card slot\", \"Micro HDMI port instead of full-size HDMI\"]",
                true, 20);

        List<Product> products = productRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10));

        // Sample Reviews
        for (Product prod : products) {
            Review r1 = new Review(prod.getId(), alice.getId(), 5, "Outstanding product!",
                    "Exceeded my expectations in build quality, performance, and everyday reliability. Highly recommended!");
            Review r2 = new Review(prod.getId(), bob.getId(), 4, "Great value for money",
                    "Solid features and works exactly as advertised. Very happy with this purchase.");
            reviewRepository.saveAll(List.of(r1, r2));
        }
    }

    private Product createProduct(String name, String brand, String category, String description,
                                  double price, double discount, String imageUrl, double rating, int reviewCount,
                                  String specs, String features, String performance, String pros, String cons,
                                  boolean availability, int stock) {
        Product p = new Product();
        p.setName(name);
        p.setBrand(brand);
        p.setCategory(category);
        p.setDescription(description);
        p.setPrice(BigDecimal.valueOf(price));
        p.setDiscount(BigDecimal.valueOf(discount));
        p.setImageUrl(imageUrl);
        p.setRating(BigDecimal.valueOf(rating));
        p.setReviewCount(reviewCount);
        p.setSpecifications(specs);
        p.setFeatures(features);
        p.setPerformance(performance);
        p.setPros(pros);
        p.setCons(cons);
        p.setAvailability(availability);
        p.setInStockQuantity(stock);
        return p;
    }
}
