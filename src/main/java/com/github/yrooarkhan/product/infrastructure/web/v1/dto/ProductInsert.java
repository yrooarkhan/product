package com.github.yrooarkhan.product.infrastructure.web.v1.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInsert {
    
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String brand;
    
    @NotNull
    @Positive
    @Max(1000000)
    private BigDecimal value;
    
    @NotNull
    @Positive
    @Max(10000000)
    private Integer initialStock;

}
