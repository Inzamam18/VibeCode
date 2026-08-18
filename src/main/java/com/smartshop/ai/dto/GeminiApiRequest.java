package com.smartshop.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiApiRequest {

    private List<Content> contents;
    private GenerationConfig generationConfig;

    public GeminiApiRequest() {
    }

    public GeminiApiRequest(List<Content> contents, GenerationConfig generationConfig) {
        this.contents = contents;
        this.generationConfig = generationConfig;
    }

    public static GeminiApiRequest of(String prompt) {
        Content content = new Content("user", List.of(new Part(prompt)));
        GenerationConfig config = new GenerationConfig("application/json", 0.1);
        return new GeminiApiRequest(List.of(content), config);
    }

    // Getters and Setters
    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public GenerationConfig getGenerationConfig() {
        return generationConfig;
    }

    public void setGenerationConfig(GenerationConfig generationConfig) {
        this.generationConfig = generationConfig;
    }

    public static class Content {
        private String role;
        private List<Part> parts;

        public Content() {
        }

        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        private String text;

        public Part() {
        }

        public Part(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class GenerationConfig {
        private String responseMimeType;
        private Double temperature;

        public GenerationConfig() {
        }

        public GenerationConfig(String responseMimeType, Double temperature) {
            this.responseMimeType = responseMimeType;
            this.temperature = temperature;
        }

        public String getResponseMimeType() {
            return responseMimeType;
        }

        public void setResponseMimeType(String responseMimeType) {
            this.responseMimeType = responseMimeType;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }
    }
}
