package com.smartshop.ai;

import com.smartshop.dto.common.InterpretedRequirements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FallbackRequirementExtractor {

    private static final Logger log = LoggerFactory.getLogger(FallbackRequirementExtractor.class);

    private static final Map<String, String> CATEGORY_MAP = new LinkedHashMap<>();
    private static final Map<String, String> PRIORITY_KEYWORDS = new LinkedHashMap<>();
    private static final List<String> KNOWN_BRANDS = List.of(
            "apple", "samsung", "oneplus", "xiaomi", "sony", "dell", "hp",
            "lenovo", "asus", "boat", "noise", "realme", "google", "nothing",
            "acer", "motorola", "bose", "sennheiser", "jbl", "oppo", "vivo"
    );

    static {
        CATEGORY_MAP.put("smartphones?", "smartphone");
        CATEGORY_MAP.put("phones?", "smartphone");
        CATEGORY_MAP.put("mobiles?", "smartphone");
        CATEGORY_MAP.put("laptops?", "laptop");
        CATEGORY_MAP.put("notebooks?", "laptop");
        CATEGORY_MAP.put("macbooks?", "laptop");
        CATEGORY_MAP.put("headphones?", "headphones");
        CATEGORY_MAP.put("earphones?", "headphones");
        CATEGORY_MAP.put("earbuds?", "headphones");
        CATEGORY_MAP.put("airpods?", "headphones");
        CATEGORY_MAP.put("audio", "headphones");
        CATEGORY_MAP.put("smartwatch(es)?", "smartwatch");
        CATEGORY_MAP.put("watch(es)?", "smartwatch");
        CATEGORY_MAP.put("tablets?", "tablet");
        CATEGORY_MAP.put("ipads?", "tablet");
        CATEGORY_MAP.put("televisions?|tvs?", "television");

        PRIORITY_KEYWORDS.put("cameras?", "camera");
        PRIORITY_KEYWORDS.put("batter(y|ies)", "battery");
        PRIORITY_KEYWORDS.put("displays?|screens?", "display");
        PRIORITY_KEYWORDS.put("gam(ing|e|er)", "gaming");
        PRIORITY_KEYWORDS.put("performances?|fast|speed", "performance");
        PRIORITY_KEYWORDS.put("cod(ing|e)|programm(ing|er)|software", "coding");
        PRIORITY_KEYWORDS.put("noise cancellation|anc|active noise", "anc");
        PRIORITY_KEYWORDS.put("sound|audio|bass|music", "audio quality");
        PRIORITY_KEYWORDS.put("lightweight|compact|portable|light", "portability");
        PRIORITY_KEYWORDS.put("storage|ram|memory", "storage");
        PRIORITY_KEYWORDS.put("design|looks?|aesthetic", "design");
        PRIORITY_KEYWORDS.put("value|budget|affordable|cheap", "value");
    }

    public InterpretedRequirements extract(String query) {
        log.info("Running heuristic fallback requirement extraction for: '{}'", query);
        if (query == null || query.trim().isEmpty()) {
            return new InterpretedRequirements("all", null, null, List.of(), List.of(), List.of(), List.of(), null);
        }

        String lower = query.toLowerCase();

        // 1. Extract category
        String category = extractCategory(lower);

        // 2. Extract budget (maxPrice / minPrice)
        BigDecimal maxPrice = extractMaxPrice(lower);
        BigDecimal minPrice = extractMinPrice(lower);

        // 3. Extract preferred & excluded brands
        List<String> preferredBrands = extractBrands(lower);
        List<String> excludedBrands = new ArrayList<>();

        // 4. Extract priorities & features
        List<String> priorities = extractPriorities(lower);
        List<String> requiredFeatures = extractRequiredFeatures(lower);

        // 5. Extract use case
        String useCase = extractUseCase(lower);

        return new InterpretedRequirements(
                category,
                minPrice,
                maxPrice,
                preferredBrands,
                excludedBrands,
                priorities,
                requiredFeatures,
                useCase
        );
    }

    private String extractCategory(String text) {
        for (Map.Entry<String, String> entry : CATEGORY_MAP.entrySet()) {
            Pattern pattern = Pattern.compile("\\b" + entry.getKey() + "\\b", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(text).find()) {
                return entry.getValue();
            }
        }
        return "smartphone"; // Default to most common category if unspecified
    }

    private BigDecimal extractMaxPrice(String text) {
        // Match patterns like "under ₹30,000", "below 70000", "under 30k", "within 25000", "max 40000", "less than 50000", "for ₹1"
        List<Pattern> patterns = List.of(
                Pattern.compile("(?:under|below|within|less than|max|up to|budget of|for|around)\\s*(?:rs\\.?|inr|₹)?\\s*([0-9]+(?:,[0-9]+)*(?:\\.[0-9]+)?)\\s*(k|lakh)?", Pattern.CASE_INSENSITIVE),
                Pattern.compile("(?:rs\\.?|inr|₹)\\s*([0-9]+(?:,[0-9]+)*(?:\\.[0-9]+)?)\\s*(k|lakh)?", Pattern.CASE_INSENSITIVE)
        );

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String numStr = matcher.group(1).replace(",", "");
                String multiplier = matcher.group(2);
                try {
                    double val = Double.parseDouble(numStr);
                    if (multiplier != null) {
                        if ("k".equalsIgnoreCase(multiplier)) val *= 1000;
                        else if ("lakh".equalsIgnoreCase(multiplier)) val *= 100000;
                    }
                    if (val > 0) {
                        return BigDecimal.valueOf(val);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return null;
    }

    private BigDecimal extractMinPrice(String text) {
        Pattern pattern = Pattern.compile("(?:above|more than|at least|min|minimum)\\s*(?:rs\\.?|inr|₹)?\\s*([0-9]+(?:,[0-9]+)*)\\s*(k|lakh)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String numStr = matcher.group(1).replace(",", "");
            String multiplier = matcher.group(2);
            try {
                double val = Double.parseDouble(numStr);
                if (multiplier != null) {
                    if ("k".equalsIgnoreCase(multiplier)) val *= 1000;
                    else if ("lakh".equalsIgnoreCase(multiplier)) val *= 100000;
                }
                if (val > 0) {
                    return BigDecimal.valueOf(val);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    private List<String> extractBrands(String text) {
        List<String> brands = new ArrayList<>();
        for (String brand : KNOWN_BRANDS) {
            if (Pattern.compile("\\b" + Pattern.quote(brand) + "\\b", Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                brands.add(brand);
            }
        }
        return brands;
    }

    private List<String> extractPriorities(String text) {
        Set<String> priorities = new LinkedHashSet<>();
        for (Map.Entry<String, String> entry : PRIORITY_KEYWORDS.entrySet()) {
            Pattern pattern = Pattern.compile("\\b(?:" + entry.getKey() + ")\\b", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(text).find()) {
                priorities.add(entry.getValue());
            }
        }
        return new ArrayList<>(priorities);
    }

    private List<String> extractRequiredFeatures(String text) {
        List<String> features = new ArrayList<>();
        if (text.contains("wireless") || text.contains("bluetooth")) features.add("wireless");
        if (text.contains("5g")) features.add("5g");
        if (text.contains("amoled") || text.contains("oled")) features.add("amoled");
        if (text.contains("waterproof") || text.contains("water resistant")) features.add("waterproof");
        if (text.contains("fast charging") || text.contains("quick charge")) features.add("fast charging");
        return features;
    }

    private String extractUseCase(String text) {
        if (text.contains("coding") || text.contains("programming") || text.contains("developer") || text.contains("software")) {
            return "coding";
        }
        if (text.contains("gaming") || text.contains("games")) {
            return "gaming";
        }
        if (text.contains("office") || text.contains("work") || text.contains("business")) {
            return "office work";
        }
        if (text.contains("photography") || text.contains("vlog") || text.contains("photos")) {
            return "photography";
        }
        if (text.contains("gym") || text.contains("running") || text.contains("workout") || text.contains("sports")) {
            return "fitness";
        }
        if (text.contains("study") || text.contains("student") || text.contains("college")) {
            return "study";
        }
        return null;
    }
}
