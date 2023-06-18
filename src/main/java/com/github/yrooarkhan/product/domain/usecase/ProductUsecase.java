package com.github.yrooarkhan.product.domain.usecase;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.yrooarkhan.product.domain.KnownError;
import com.github.yrooarkhan.product.domain.entity.Product;
import com.github.yrooarkhan.product.infrastructure.persistent.ProductData;
import com.github.yrooarkhan.product.infrastructure.persistent.ProductRepository;
import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ProductInsert;
import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ProductSearchParams;

@Service
public class ProductUsecase {
    
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> retrieveAllInPages(ProductSearchParams searchParams) {
        Pageable pageable = searchParams.toPageable();
        Page<ProductData> productsDataPage = productRepository.findAllWithStock(pageable);
        List<ProductData> productsData = productsDataPage.getContent();
        List<Product> products = productsData.stream().map(Product::new).collect(toList());
        return new PageImpl<>(products, pageable, productsDataPage.getSize());
    }

    public void insert(ProductInsert productInsertDTO) {
        if (productRepository.existsByName(productInsertDTO.getName())) {
            throw KnownError.ITEM_NAME_ALREADY_USED_BY_BRAND.toBusinessException();
        }
        
        ProductData productToInsert = new ProductData(productInsertDTO);
        productRepository.save(productToInsert);
    }

}
