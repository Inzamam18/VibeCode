package com.smartshop.ai;

import com.smartshop.ai.dto.GeminiApiRequest;
import com.smartshop.ai.dto.GeminiApiResponse;
import com.smartshop.exception.GeminiServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-1.5-flash}")
    private String model;

    @Value("${gemini.api.base-url:https://generativelanguage.googleapis.com/v1beta/models}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isApiKeyConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.contains("your_gemini_api_key");
    }

    public String generateContent(String prompt) {
        if (!isApiKeyConfigured()) {
            log.warn("Gemini API key is not configured or is placeholder. Will use heuristic fallback.");
            throw new GeminiServiceException("Gemini API key not configured");
        }

        String url = String.format("%s/%s:generateContent?key=%s", baseUrl, model, apiKey);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            GeminiApiRequest requestPayload = GeminiApiRequest.of(prompt);
            HttpEntity<GeminiApiRequest> entity = new HttpEntity<>(requestPayload, headers);

            log.info("Sending request to Google Gemini API (model: {})", model);
            ResponseEntity<GeminiApiResponse> response = restTemplate.postForEntity(url, entity, GeminiApiResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String text = response.getBody().getFirstCandidateText();
                if (text != null && !text.isBlank()) {
                    return text;
                }
            }
            throw new GeminiServiceException("Empty response body from Gemini API");

        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Gemini API rate limit exceeded (429). Falling back to heuristic extraction.");
            throw new GeminiServiceException("Gemini rate limit exceeded", e);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Gemini API HTTP error (status: {}). Falling back to heuristic extraction.", e.getStatusCode());
            throw new GeminiServiceException("Gemini API HTTP error: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            log.warn("Gemini API network timeout/connection failure. Falling back to heuristic extraction.");
            throw new GeminiServiceException("Gemini API connection timeout", e);
        } catch (Exception e) {
            log.error("Unexpected error during Gemini API call: {}", e.getMessage());
            throw new GeminiServiceException("Gemini API invocation failed", e);
        }
    }
}
