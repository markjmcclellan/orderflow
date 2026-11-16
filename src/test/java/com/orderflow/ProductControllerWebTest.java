package com.orderflow;

import com.orderflow.exception.GlobalExceptionHandler;
import com.orderflow.product.Product;
import com.orderflow.product.ProductController;
import com.orderflow.product.ProductRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
class ProductControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    @Test
    void createProduct_negativePrice_returns400() throws Exception {
        String json = """
                {
                    "sku": "TEST-001",
                    "name": "Test Product",
                    "price": -25.00
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productRepository);
    }

    @Test
    void createProduct_duplicateSku_returns409() throws Exception {
        String json = """
                {
                    "sku": "DUPLICATE-001",
                    "name": "Test Product",
                    "price": 25.00
                }
                """;

        when(productRepository.save(any(Product.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate SKU"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(
                        "Product could not be saved due to a database constraint."
                ));
    }
}