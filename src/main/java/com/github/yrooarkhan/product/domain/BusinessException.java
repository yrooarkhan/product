package com.github.yrooarkhan.product.domain;

public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public BusinessException(String exceptionMessage) {
        super(exceptionMessage);
    }

}