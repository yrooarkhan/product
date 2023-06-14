package com.github.yrooarkhan.product.infrastructure.web.v1.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_EMPTY)
public class ApiErrorResponse {
    
    private static final String DEFAULT_ERROR_MESSAGE = "Ocorreu um erro inesperado.";
    
    private final LocalDateTime date = LocalDateTime.now();
    private final String developerMessage;
    private final String clientMessage;
    private List<String> additionalInfo = new ArrayList<>();
    
    public ApiErrorResponse(String message) {
        this.clientMessage = message;
        this.developerMessage = message;
    }
    
    public ApiErrorResponse(Map<String, Object> errorAttributes) {
        this.clientMessage = DEFAULT_ERROR_MESSAGE;
        this.developerMessage = (String) errorAttributes.getOrDefault("message", DEFAULT_ERROR_MESSAGE);
    }
    
    public void addInfo(List<String> additionalInfo) {
        this.additionalInfo.addAll(additionalInfo);
    }
    
    public Map<String, Object> toAttributeMap() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("date", date);
        attributes.put("developerMessage", developerMessage);
        attributes.put("clientMessage", clientMessage);
        return attributes;
    }

}
