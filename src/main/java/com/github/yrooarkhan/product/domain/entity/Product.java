package com.github.yrooarkhan.product.domain.entity;

import java.math.BigDecimal;

import com.github.yrooarkhan.product.infrastructure.persistent.ProductData;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Product {
    
    private String name;
    private String brand;
    private BigDecimal value;
    private Integer inStock;
    private Integer reserved;
    
    public Product(ProductData productData) {
        this.name = productData.getName();
        this.brand = productData.getBrand();
        this.value = productData.getValue();
        this.inStock = productData.getInStock();
        this.reserved = productData.getReserved();
    }

}
