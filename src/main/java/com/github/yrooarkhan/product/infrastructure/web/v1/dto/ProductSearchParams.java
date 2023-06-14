package com.github.yrooarkhan.product.infrastructure.web.v1.dto;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class ProductSearchParams {

    private static final Integer MINIMUM_PAGEABLE_PAGE = 0;
    private static final Integer MAX_PAGE_SIZE = 100;
    private static final Integer MIN_PAGE_SIZE = 1;
    private static final Integer INITIAL_PAGE = 0;
    private static final Integer INITIAL_SIZE = 10;
    
    private Optional<Integer> page = Optional.empty();
    private Optional<Integer> size = Optional.empty();
    
    public Pageable toPageable() {
        Integer pageablePage = page.orElse(INITIAL_PAGE);
        Integer pageableSize = size.orElse(INITIAL_SIZE);
        
        if (pageablePage < MINIMUM_PAGEABLE_PAGE) pageablePage = MINIMUM_PAGEABLE_PAGE;
        if (pageableSize > MAX_PAGE_SIZE) pageableSize = MAX_PAGE_SIZE;
        if (pageableSize < MIN_PAGE_SIZE) pageableSize = MIN_PAGE_SIZE;
        
        return PageRequest.of(pageablePage, pageableSize);
    }

}
