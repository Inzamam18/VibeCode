package com.smartshop.recommendation;

import com.smartshop.dto.common.InterpretedRequirements;
import com.smartshop.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SuitabilityScorer {

    public double calculateSuitabilityScore(Product product, InterpretedRequirements reqs) {
        if (product == null) {
            return 0.0;
        }

        double priceFitScore = calculatePriceFit(product, reqs);
        double featureMatchScore = calculateFeatureMatch(product, reqs);
        double specMatchScore = calculateSpecMatch(product, reqs);
        double performanceScore = calculatePerformanceScore(product, reqs);
        double ratingScore = calculateRatingScore(product);
        double discountScore = calculateDiscountScore(product);

        // Adjust weights if user has explicit priorities
        double featureWeight = ScoringWeights.FEATURE_MATCH_WEIGHT;
        double specWeight = ScoringWeights.SPEC_MATCH_WEIGHT;
        double priceWeight = ScoringWeights.PRICE_FIT_WEIGHT;

        if (reqs != null && reqs.getPriorities() != null && !reqs.getPriorities().isEmpty()) {
            featureWeight += 0.05;
            specWeight += 0.05;
            priceWeight = Math.max(0.15, priceWeight - 0.10);
        }

        double totalWeightedScore = (priceFitScore * priceWeight)
                + (featureMatchScore * featureWeight)
                + (specMatchScore * specWeight)
                + (performanceScore * ScoringWeights.PERFORMANCE_WEIGHT)
                + (ratingScore * ScoringWeights.RATING_WEIGHT)
                + (discountScore * ScoringWeights.DISCOUNT_WEIGHT);

        // Brand preference modifier
        if (reqs != null && product.getBrand() != null) {
            String brand = product.getBrand().toLowerCase();
            if (reqs.getPreferredBrands() != null && reqs.getPreferredBrands().stream().anyMatch(b -> b.equalsIgnoreCase(brand))) {
                totalWeightedScore += 5.0; // Bonus for preferred brand
            }
            if (reqs.getExcludedBrands() != null && reqs.getExcludedBrands().stream().anyMatch(b -> b.equalsIgnoreCase(brand))) {
                totalWeightedScore -= 30.0; // Heavy penalty for excluded brand
            }
        }

        // Apply budget constraint damping if severely over budget
        if (reqs != null && reqs.getMaxPrice() != null && product.getPrice() != null) {
            double price = product.getPrice().doubleValue();
            double maxPrice = reqs.getMaxPrice().doubleValue();
            if (price > maxPrice) {
                double overagePercent = (price - maxPrice) / maxPrice;
                if (overagePercent > 0.50) {
                    double damping = Math.max(0.15, 1.0 - (overagePercent * 0.25));
                    totalWeightedScore *= damping;
                }
            }
        }

        // Clamp between 0.0 and 100.0 and round to 1 decimal place
        double clampedScore = Math.max(0.0, Math.min(100.0, totalWeightedScore));
        return Math.round(clampedScore * 10.0) / 10.0;
    }

    public double calculatePriceFit(Product product, InterpretedRequirements reqs) {
        if (reqs == null || product.getPrice() == null) {
            return 80.0;
        }

        double price = product.getPrice().doubleValue();
        Double maxPrice = reqs.getMaxPrice() != null ? reqs.getMaxPrice().doubleValue() : null;
        Double minPrice = reqs.getMinPrice() != null ? reqs.getMinPrice().doubleValue() : null;

        if (maxPrice == null && minPrice == null) {
            return 85.0; // Neutral budget score
        }

        if (maxPrice != null) {
            if (price <= maxPrice) {
                // Within budget
                double ratio = price / maxPrice;
                if (ratio >= 0.70 && ratio <= 1.0) {
                    return 100.0; // Sweet spot in target budget
                } else if (ratio < 0.70 && ratio >= 0.30) {
                    return 92.0; // Great value below budget
                } else {
                    return 85.0; // Much cheaper than budget
                }
            } else {
                // Over budget penalty
                double overagePercent = (price - maxPrice) / maxPrice;
                if (overagePercent <= 0.10) {
                    return 70.0; // 10% over budget
                } else if (overagePercent <= 0.25) {
                    return 45.0; // 25% over budget
                } else if (overagePercent <= 0.50) {
                    return 20.0; // 50% over budget
                } else {
                    return 0.0; // Far over budget (e.g. ₹1 query)
                }
            }
        }

        if (minPrice != null && price < minPrice) {
            return 60.0;
        }

        return 85.0;
    }

    public double calculateFeatureMatch(Product product, InterpretedRequirements reqs) {
        if (reqs == null) {
            return 75.0;
        }

        List<String> priorities = reqs.getPriorities();
        List<String> requiredFeatures = reqs.getRequiredFeatures();

        if ((priorities == null || priorities.isEmpty()) && (requiredFeatures == null || requiredFeatures.isEmpty())) {
            return 80.0;
        }

        String productText = combineProductText(product).toLowerCase();
        int totalCriteria = 0;
        int matchedCriteria = 0;

        if (priorities != null) {
            for (String priority : priorities) {
                totalCriteria += 2; // Priorities have double weight
                if (matchesKeyword(productText, priority)) {
                    matchedCriteria += 2;
                }
            }
        }

        if (requiredFeatures != null) {
            for (String feature : requiredFeatures) {
                totalCriteria += 1;
                if (matchesKeyword(productText, feature)) {
                    matchedCriteria += 1;
                }
            }
        }

        if (totalCriteria == 0) {
            return 80.0;
        }

        double ratio = (double) matchedCriteria / totalCriteria;
        return 40.0 + (ratio * 60.0); // Base 40 + up to 60 based on match ratio
    }

    public double calculateSpecMatch(Product product, InterpretedRequirements reqs) {
        if (reqs == null || (reqs.getUseCase() == null && (reqs.getPriorities() == null || reqs.getPriorities().isEmpty()))) {
            return 80.0;
        }

        String specs = product.getSpecifications() != null ? product.getSpecifications().toLowerCase() : "";
        String desc = product.getDescription() != null ? product.getDescription().toLowerCase() : "";
        String combined = specs + " " + desc;

        double score = 70.0;

        if (reqs.getUseCase() != null) {
            String useCase = reqs.getUseCase().toLowerCase();
            if (useCase.contains("coding") || useCase.contains("programming")) {
                if (combined.contains("16gb") || combined.contains("32gb") || combined.contains("i7") || combined.contains("i5") || combined.contains("ryzen") || combined.contains("m1") || combined.contains("m2") || combined.contains("m3") || combined.contains("ssd")) {
                    score += 25.0;
                } else if (combined.contains("8gb")) {
                    score += 15.0;
                }
            } else if (useCase.contains("gaming")) {
                if (combined.contains("rtx") || combined.contains("gtx") || combined.contains("radeon") || combined.contains("120hz") || combined.contains("144hz") || combined.contains("165hz")) {
                    score += 25.0;
                }
            } else if (useCase.contains("photography") || useCase.contains("vlog")) {
                if (combined.contains("sony") || combined.contains("ois") || combined.contains("50mp") || combined.contains("108mp") || combined.contains("4k") || combined.contains("telephoto")) {
                    score += 25.0;
                }
            }
        }

        if (reqs.getPriorities() != null) {
            for (String priority : reqs.getPriorities()) {
                if (matchesKeyword(combined, priority)) {
                    score += 5.0;
                }
            }
        }

        return Math.min(100.0, score);
    }

    public double calculatePerformanceScore(Product product, InterpretedRequirements reqs) {
        String perf = product.getPerformance() != null ? product.getPerformance().toLowerCase() : "";
        String desc = product.getDescription() != null ? product.getDescription().toLowerCase() : "";
        String combined = perf + " " + desc;

        if (combined.contains("flagship") || combined.contains("excellent") || combined.contains("blazing") || combined.contains("snapdragon 8") || combined.contains("apple silicon") || combined.contains("m2") || combined.contains("m3") || combined.contains("rtx 40")) {
            return 95.0;
        }
        if (combined.contains("high performance") || combined.contains("smooth") || combined.contains("fast") || combined.contains("snapdragon 7") || combined.contains("dimensity 8") || combined.contains("i7") || combined.contains("ryzen 7")) {
            return 85.0;
        }
        if (combined.contains("mid-range") || combined.contains("good") || combined.contains("reliable") || combined.contains("i5") || combined.contains("ryzen 5")) {
            return 75.0;
        }
        return 70.0;
    }

    public double calculateRatingScore(Product product) {
        if (product.getRating() == null) {
            return 70.0;
        }
        double rating = product.getRating().doubleValue();
        double scaledRating = (rating / 5.0) * 100.0;

        // Review count confidence adjustment
        int reviews = product.getReviewCount() != null ? product.getReviewCount() : 0;
        if (reviews > 100) {
            return scaledRating;
        } else if (reviews > 20) {
            return scaledRating * 0.95 + 5.0;
        } else {
            return scaledRating * 0.90 + 7.0;
        }
    }

    public double calculateDiscountScore(Product product) {
        if (product.getDiscount() == null) {
            return 50.0;
        }
        double discount = product.getDiscount().doubleValue();
        if (discount <= 0) return 40.0;
        if (discount >= 40) return 100.0;
        return 40.0 + (discount * 1.5);
    }

    private boolean matchesKeyword(String text, String keyword) {
        if (keyword == null || keyword.isEmpty()) return false;
        String k = keyword.toLowerCase().trim();
        if (k.equals("battery")) {
            return text.contains("battery") || text.contains("mah") || text.contains("backup") || text.contains("long-lasting");
        }
        if (k.equals("camera")) {
            return text.contains("camera") || text.contains("mp") || text.contains("sensor") || text.contains("lens") || text.contains("photo") || text.contains("ois");
        }
        if (k.equals("display") || k.equals("screen")) {
            return text.contains("display") || text.contains("screen") || text.contains("amoled") || text.contains("oled") || text.contains("hz") || text.contains("retina");
        }
        if (k.equals("anc") || k.equals("noise cancellation")) {
            return text.contains("anc") || text.contains("noise cancel") || text.contains("active noise");
        }
        if (k.equals("coding")) {
            return text.contains("ram") || text.contains("processor") || text.contains("ssd") || text.contains("cpu") || text.contains("keyboard") || text.contains("terminal");
        }
        if (k.equals("gaming")) {
            return text.contains("gpu") || text.contains("gaming") || text.contains("graphics") || text.contains("fps") || text.contains("refresh rate");
        }
        return text.contains(k);
    }

    private String combineProductText(Product product) {
        StringBuilder sb = new StringBuilder();
        if (product.getName() != null) sb.append(product.getName()).append(" ");
        if (product.getDescription() != null) sb.append(product.getDescription()).append(" ");
        if (product.getFeatures() != null) sb.append(product.getFeatures()).append(" ");
        if (product.getSpecifications() != null) sb.append(product.getSpecifications()).append(" ");
        if (product.getPros() != null) sb.append(product.getPros()).append(" ");
        if (product.getPerformance() != null) sb.append(product.getPerformance()).append(" ");
        return sb.toString();
    }
}
