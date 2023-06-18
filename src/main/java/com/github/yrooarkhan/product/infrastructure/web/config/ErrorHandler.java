package com.github.yrooarkhan.product.infrastructure.web.config;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.yrooarkhan.product.domain.BusinessException;
import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ApiErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    private static final String UNFORMATED_REQUEST_BODY = "O corpo de resposta está indevidamente formatado.";
    private static final String INVALID_REQUEST_BODY = "Não foi possível realizar a leitura do corpo de requisição recebido.";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequestBody(HttpMessageNotReadableException exception) {
        ApiErrorResponse response = new ApiErrorResponse(INVALID_REQUEST_BODY);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Stream<ObjectError> exceptionErrors = exception.getAllErrors().stream();
        Stream<ObjectError> fieldErrors = exceptionErrors.filter(FieldError.class::isInstance);
        List<String> fieldsAndMessages = fieldErrors.map(convertToReadableString()).collect(toList());
        
        ApiErrorResponse response = new ApiErrorResponse(UNFORMATED_REQUEST_BODY);
        response.addInfo(fieldsAndMessages);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException exception) {
        ApiErrorResponse response = new ApiErrorResponse(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    
    private Function<ObjectError, String> convertToReadableString() {
        return error -> {
            FieldError fieldError = (FieldError) error;
            return fieldError.getField() + " :: " + fieldError.getDefaultMessage();
        };
    }

}
