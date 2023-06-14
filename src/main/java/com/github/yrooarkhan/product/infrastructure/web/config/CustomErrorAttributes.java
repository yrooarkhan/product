package com.github.yrooarkhan.product.infrastructure.web.config;

import java.util.Arrays;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.github.yrooarkhan.product.domain.KnownError;
import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ApiErrorResponse;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    
    private static final String[] hiddenAttributes = { "errors", "path", "trace" };

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Arrays.stream(hiddenAttributes).forEach(errorAttributes::remove);
        
        if (!KnownError.acknowledge(errorAttributes.get("message").toString())) {
            errorAttributes.remove("message");
        }
        
        return new ApiErrorResponse(errorAttributes).toAttributeMap();
    }
    
}