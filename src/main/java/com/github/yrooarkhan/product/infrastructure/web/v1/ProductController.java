package com.github.yrooarkhan.product.infrastructure.web.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.yrooarkhan.product.domain.entity.Product;
import com.github.yrooarkhan.product.domain.usecase.ProductUsecase;
import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ProductInsert;
import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ProductSearchParams;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    
    @Autowired
    private ProductUsecase productService;

    @GetMapping
    public Page<Product> getAll(ProductSearchParams searchParams) {
        return productService.retrieveAllInPages(searchParams);
    }
    
    @PostMapping
    public ResponseEntity<Object> insertProduct(@Valid @RequestBody ProductInsert productInsertDTO) {
        productService.insert(productInsertDTO);
        return ResponseEntity.noContent().build();
    }

}
