package com.github.yrooarkhan.product.infrastructure.persistent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ProductInsert;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@Document("product")
public class ProductData {

    @MongoId
    private String id;

    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private String name;
    private String brand;
    private BigDecimal value;
    private Integer inStock;
    private Integer reserved;
    
    public ProductData(ProductInsert productInsertDTO) {
        this.name = productInsertDTO.getName();
        this.brand = productInsertDTO.getBrand();
        this.value = productInsertDTO.getValue();
        this.inStock = productInsertDTO.getInitialStock();
        this.reserved = 0;
    }

}
