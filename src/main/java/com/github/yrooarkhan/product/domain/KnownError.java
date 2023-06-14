package com.github.yrooarkhan.product.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum KnownError {
    
    ITEM_NAME_ALREADY_USED_BY_BRAND("Já existe um item cadastrado para esta marca com um mesmo nome.");
    
    private String message;
    
    private static final Map<String, KnownError> quickSearchOfErrors = new HashMap<>(); 
    
    static { 
        KnownError[] listOfErrors = KnownError.values();
        Arrays.stream(listOfErrors).forEach(error -> quickSearchOfErrors.put(error.message, error));
    }

    private KnownError(String message) {
        this.message = message;
    }
    
    public static boolean acknowledge(String message) {
        return quickSearchOfErrors.containsKey(message);
    }
    
    public BusinessException toBusinessException() {
        return new BusinessException(this.message);
    }
    
}
