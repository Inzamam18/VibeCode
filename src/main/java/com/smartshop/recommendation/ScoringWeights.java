package com.smartshop.recommendation;

public class ScoringWeights {

    public static final double PRICE_FIT_WEIGHT = 0.25;
    public static final double FEATURE_MATCH_WEIGHT = 0.25;
    public static final double SPEC_MATCH_WEIGHT = 0.20;
    public static final double PERFORMANCE_WEIGHT = 0.15;
    public static final double RATING_WEIGHT = 0.10;
    public static final double DISCOUNT_WEIGHT = 0.05;

    private ScoringWeights() {
    }
}
