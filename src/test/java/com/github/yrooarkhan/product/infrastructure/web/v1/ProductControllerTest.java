package com.github.yrooarkhan.product.infrastructure.web.v1;

import static java.nio.charset.Charset.forName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.yrooarkhan.product.domain.entity.Product;
import com.github.yrooarkhan.product.domain.usecase.ProductUsecase;
import com.github.yrooarkhan.product.infrastructure.web.config.CustomErrorAttributes;
import com.github.yrooarkhan.product.infrastructure.web.config.ErrorHandler;
import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ApiErrorResponse;
import com.github.yrooarkhan.product.infrastructure.web.v1.dto.ProductInsert;

@EnableWebMvc
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ProductController.class, ErrorHandler.class, CustomErrorAttributes.class })
class ProductControllerTest {

    private static final String INVALID_REQUEST_BODY = "Não foi possível realizar a leitura do corpo de requisição recebido.";
    private static final String INVALID_PRODUCT = "O corpo de resposta está indevidamente formatado.";
    private static final String EXAMPLE_ADDITIONAL_INFO = "initialStock :: must not be null";
    private static final String PRODUCT_BRAND = "Foo";
    private static final String PRODUCT_NAME = "Moo";
    private static final String BASE_URI = "/v1/products";
    private static final String UTF_8 = "UTF-8";

    private static final BigDecimal PRODUCT_VALUE = BigDecimal.valueOf(22.5);
    
    private static final int PRODUCT_RESERVED = 3;
    private static final int PRODUCT_IN_STOCK = 10;
    private static final int PRODUCT_AMOUNT = 1;

    @MockBean
    private ProductUsecase productUsecase;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private ObjectMapper mapper;
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("GET list of Products (/v1/products)")
    void givenAProductIsAlreadyPersisted_whenMakingAGetRequest_thenTheProductShouldBeReturned() throws Exception {
        doReturn(mockProducts()).when(productUsecase).retrieveAllInPages(ArgumentMatchers.any());
        
        mockMvc.perform(get(BASE_URI)).andExpectAll(
            status().isOk(),
            jsonPath("$.content.length()").value(PRODUCT_AMOUNT),
            jsonPath("$.content[0].name").value(PRODUCT_NAME),
            jsonPath("$.content[0].brand").value(PRODUCT_BRAND),
            jsonPath("$.content[0].value").value(PRODUCT_VALUE),
            jsonPath("$.content[0].inStock").value(PRODUCT_IN_STOCK),
            jsonPath("$.content[0].reserved").value(PRODUCT_RESERVED)
       );
    }
    
    @Test
    @DisplayName("POST a valid Product (/v1/products)")
    void givenAValidProductInJson_whenMakingAPostRequest_thenTheApiShouldReturnNoContent() throws Exception {
        var productToInsert = mockProductInsert(PRODUCT_NAME, PRODUCT_BRAND, PRODUCT_IN_STOCK);
        var requestBody = mapper.writeValueAsString(productToInsert);
        var request = post(BASE_URI).contentType(APPLICATION_JSON_VALUE).content(requestBody);
        
        mockMvc.perform(request).andExpectAll(
                status().isNoContent(),
                jsonPath("$").doesNotExist()
        );
    }
    
    @Test
    @DisplayName("POST an invalid Product (/v1/products)")
    void givenAInvalidProductInJson_whenMakingAPostRequest_thenTheApiShouldReturnAnErrorWithAdditionalContentExplainingTheConstraints() throws Exception {
        var invalidProductToInsert = mockProductInsert(PRODUCT_NAME, null, null);
        var requestBody = mapper.writeValueAsString(invalidProductToInsert);
        var request = post(BASE_URI).contentType(APPLICATION_JSON_VALUE).content(requestBody);
        
        var result = mockMvc.perform(request).andExpectAll(
                status().isBadRequest()
        ).andReturn();
        
        var error = mapper.readValue(result.getResponse().getContentAsString(forName(UTF_8)), ApiErrorResponse.class);
        assertThat(error.getClientMessage(), equalTo(INVALID_PRODUCT));
        assertThat(error.getDeveloperMessage(), equalTo(INVALID_PRODUCT));
        assertThat(error.getDate(), notNullValue());
        assertThat(error.getAdditionalInfo(), not(empty()));
        assertThat(error.getAdditionalInfo().get(0), equalTo(EXAMPLE_ADDITIONAL_INFO));
    }
    
    @Test
    @DisplayName("POST an invalid JSON (/v1/products)")
    void givenAInvalidJson_whenMakingAPostRequest_thenTheApiShouldReturnAnErrorExplainingTheProblem() throws Exception {
        var invalidRequestBody = "{\"name: }";
        var request = post(BASE_URI).contentType(APPLICATION_JSON_VALUE).content(invalidRequestBody);
        
        var result = mockMvc.perform(request).andExpectAll(
                status().isBadRequest()
        ).andReturn();
        
        var error = mapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), ApiErrorResponse.class);
        assertThat(error.getClientMessage(), equalTo(INVALID_REQUEST_BODY));
        assertThat(error.getDeveloperMessage(), equalTo(INVALID_REQUEST_BODY));
        assertThat(error.getDate(), notNullValue());
        assertThat(error.getAdditionalInfo(), empty());
    }
    
    private Page<Product> mockProducts() {
        var builder = Product.builder();

        var product = builder.name(PRODUCT_NAME)
                             .brand(PRODUCT_BRAND)
                             .inStock(PRODUCT_IN_STOCK)
                             .reserved(PRODUCT_RESERVED)
                             .value(PRODUCT_VALUE)
                             .build();

        return new PageImpl<>(Collections.singletonList(product));
    }

    private ProductInsert mockProductInsert(String name, String brand, Integer initialStock) {
        var builder = ProductInsert.builder();
        var productInsert = builder.name(name).brand(brand).value(PRODUCT_VALUE).initialStock(initialStock).build();
        return productInsert;
    }

}