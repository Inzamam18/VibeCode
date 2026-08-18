package com.smartshop.ai;

import com.smartshop.dto.common.InterpretedRequirements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RequirementExtractor {

    private static final Logger log = LoggerFactory.getLogger(RequirementExtractor.class);

    private final GeminiService geminiService;
    private final GeminiResponseParser geminiResponseParser;
    private final FallbackRequirementExtractor fallbackExtractor;

    public RequirementExtractor(GeminiService geminiService,
                                GeminiResponseParser geminiResponseParser,
                                FallbackRequirementExtractor fallbackExtractor) {
        this.geminiService = geminiService;
        this.geminiResponseParser = geminiResponseParser;
        this.fallbackExtractor = fallbackExtractor;
    }

    public InterpretedRequirements extractRequirements(String query) {
        if (query == null || query.trim().isEmpty()) {
            return fallbackExtractor.extract(query);
        }

        if (geminiService.isApiKeyConfigured()) {
            try {
                String prompt = buildPrompt(query);
                String geminiResponseText = geminiService.generateContent(prompt);
                InterpretedRequirements reqs = geminiResponseParser.parse(geminiResponseText);
                log.info("Successfully extracted requirements using Gemini AI for category: '{}'", reqs.getCategory());
                return reqs;
            } catch (Exception e) {
                log.warn("Gemini AI extraction failed ({}: {}). Falling back to rule-based extractor.",
                        e.getClass().getSimpleName(), e.getMessage());
            }
        }

        // Fallback execution
        return fallbackExtractor.extract(query);
    }

    private String buildPrompt(String query) {
        return "You are an AI shopping requirement extractor for an e-commerce platform.\n"
                + "Convert the user's natural-language shopping query into structured JSON requirements.\n"
                + "Return ONLY a valid JSON object matching this schema:\n"
                + "{\n"
                + "  \"category\": \"smartphone\" | \"laptop\" | \"headphones\" | \"smartwatch\" | \"tablet\" | \"television\",\n"
                + "  \"minPrice\": null or number (plain number in INR),\n"
                + "  \"maxPrice\": null or number (plain number in INR),\n"
                + "  \"preferredBrands\": [\"brand1\", \"brand2\"],\n"
                + "  \"excludedBrands\": [],\n"
                + "  \"priorities\": [\"camera\", \"battery\", \"gaming\", \"coding\", \"anc\", \"display\", \"performance\", \"value\"],\n"
                + "  \"requiredFeatures\": [\"wireless\", \"5g\", \"amoled\", \"fast charging\"],\n"
                + "  \"useCase\": \"coding\" | \"gaming\" | \"office\" | \"photography\" | \"fitness\" | null\n"
                + "}\n"
                + "Rules:\n"
                + "1. Convert Indian currency terms (e.g. ₹30,000 -> 30000, 70k -> 70000, 1.5 lakh -> 150000).\n"
                + "2. If a specific price ceiling is mentioned ('under', 'below', 'less than', 'for'), populate maxPrice.\n"
                + "3. Identify user priorities (e.g., 'camera', 'battery', 'coding', 'anc') as an array of lowercase strings.\n"
                + "4. If query is ambiguous like 'Suggest a good phone', set category to 'smartphone' and leave prices null.\n"
                + "5. Output ONLY raw JSON, with no other text, markdown formatting or explanations.\n\n"
                + "User Query: \"" + query.replace("\"", "\\\"") + "\"";
    }
}
