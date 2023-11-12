package com.cmj.security.domain;

import lombok.Data;

import java.util.Map;

@Data
public class HealthResponse {
    private String status;
    private Map<String, Component> components;

    // Getter and Setter methods

    @Data
    public static class Component {
        private String status;
        private Map<String, Object> details;

        // Getter and Setter methods
    }
}