package com.github.yrooarkhan.product.infrastructure.persistent;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductData, ObjectId> {
    
    @Query("{ inStock: { $gt: 0} }")
    Page<ProductData> findAllWithStock(Pageable pageable);

    @Query(value = "{ name: ?0 }", exists = true)
    boolean existsByName(String productName);
    
}
