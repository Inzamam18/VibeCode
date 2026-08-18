package com.smartshop.recommendation;

import com.smartshop.dto.common.InterpretedRequirements;
import com.smartshop.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ExplanationGenerator {

    private final Locale inLocale = new Locale("en", "IN");

    public String generateRecommendationReason(Product product, InterpretedRequirements reqs, double suitabilityScore) {
        if (product == null) {
            return "Recommended product.";
        }

        StringBuilder reason = new StringBuilder();
        boolean fitsBudget = true;
        Double maxBudget = reqs != null && reqs.getMaxPrice() != null ? reqs.getMaxPrice().doubleValue() : null;
        double price = product.getPrice() != null ? product.getPrice().doubleValue() : 0.0;

        if (maxBudget != null && price > maxBudget) {
            fitsBudget = false;
        }

        if (suitabilityScore >= 85.0) {
            reason.append("Strong match ");
        } else if (suitabilityScore >= 70.0) {
            reason.append("Great value match ");
        } else {
            reason.append("Closest available match ");
        }

        List<String> matchedPriorities = findMatchedPriorities(product, reqs);

        if (fitsBudget && maxBudget != null) {
            reason.append(String.format("fitting your ₹%s budget", formatCurrency(maxBudget)));
            if (!matchedPriorities.isEmpty()) {
                reason.append(" with standout ").append(String.join(" and ", matchedPriorities));
            }
        } else if (!fitsBudget && maxBudget != null) {
            reason.append(String.format("for your requirements, priced at ₹%s (above your ₹%s target)",
                    formatCurrency(price), formatCurrency(maxBudget)));
            if (!matchedPriorities.isEmpty()) {
                reason.append(" but delivering premium ").append(String.join(" and ", matchedPriorities));
            }
        } else {
            if (!matchedPriorities.isEmpty()) {
                reason.append("delivering exceptional ").append(String.join(" and ", matchedPriorities));
            } else {
                reason.append("offering balanced performance and high user satisfaction");
            }
        }

        if (product.getRating() != null && product.getRating().doubleValue() >= 4.3) {
            reason.append(String.format(" (rated %.1f/5 stars)", product.getRating().doubleValue()));
        }

        reason.append(".");
        return reason.toString();
    }

    public List<String> extractStrengths(Product product, InterpretedRequirements reqs) {
        List<String> strengths = new ArrayList<>();
        if (product == null) return strengths;

        // 1. From Pros if available
        if (product.getPros() != null && !product.getPros().trim().isEmpty()) {
            String[] prosList = product.getPros().split(",|;|\n");
            for (String p : prosList) {
                String clean = p.trim().replaceAll("^\\[|\\]$|\"", "");
                if (!clean.isEmpty() && strengths.size() < 3) {
                    strengths.add(clean);
                }
            }
        }

        // 2. High rating strength
        if (product.getRating() != null && product.getRating().doubleValue() >= 4.2) {
            int reviews = product.getReviewCount() != null ? product.getReviewCount() : 0;
            if (reviews > 0) {
                strengths.add(String.format("High user rating of %.1f/5 based on %d reviews", product.getRating().doubleValue(), reviews));
            } else {
                strengths.add(String.format("Excellent rating of %.1f/5 stars", product.getRating().doubleValue()));
            }
        }

        // 3. Discount strength
        if (product.getDiscount() != null && product.getDiscount().doubleValue() >= 10.0) {
            strengths.add(String.format("%.0f%% discount off standard retail price", product.getDiscount().doubleValue()));
        }

        // Fallback generic strengths if empty
        if (strengths.isEmpty() && product.getDescription() != null) {
            strengths.add(product.getDescription().length() > 80 ? product.getDescription().substring(0, 80) + "..." : product.getDescription());
        }

        return strengths;
    }

    public List<String> extractTradeoffs(Product product, InterpretedRequirements reqs) {
        List<String> tradeoffs = new ArrayList<>();
        if (product == null) return tradeoffs;

        // 1. Budget tradeoff
        if (reqs != null && reqs.getMaxPrice() != null && product.getPrice() != null) {
            double price = product.getPrice().doubleValue();
            double maxPrice = reqs.getMaxPrice().doubleValue();
            if (price > maxPrice) {
                tradeoffs.add(String.format("Exceeds target budget by ₹%s", formatCurrency(price - maxPrice)));
            }
        }

        // 2. From Cons if available
        if (product.getCons() != null && !product.getCons().trim().isEmpty()) {
            String[] consList = product.getCons().split(",|;|\n");
            for (String c : consList) {
                String clean = c.trim().replaceAll("^\\[|\\]$|\"", "");
                if (!clean.isEmpty() && tradeoffs.size() < 2) {
                    tradeoffs.add(clean);
                }
            }
        }

        // 3. Availability tradeoff
        if (product.getAvailability() != null && !product.getAvailability()) {
            tradeoffs.add("Currently limited stock availability");
        }

        if (tradeoffs.isEmpty()) {
            tradeoffs.add("Standard warranty and specifications for this category");
        }

        return tradeoffs;
    }

    private List<String> findMatchedPriorities(Product product, InterpretedRequirements reqs) {
        List<String> matched = new ArrayList<>();
        if (reqs == null || reqs.getPriorities() == null) return matched;

        String combined = (product.getName() + " " + product.getDescription() + " " + product.getFeatures() + " " + product.getSpecifications()).toLowerCase();
        for (String priority : reqs.getPriorities()) {
            if (combined.contains(priority.toLowerCase())) {
                matched.add(priority);
            }
        }
        return matched;
    }

    private String formatCurrency(double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(inLocale);
        nf.setMaximumFractionDigits(0);
        return nf.format(amount);
    }
}
