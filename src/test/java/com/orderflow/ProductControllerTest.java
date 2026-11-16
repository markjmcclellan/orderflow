package com.orderflow;

import com.orderflow.product.Product;
import com.orderflow.product.ProductController;
import com.orderflow.product.ProductRepository;
import com.orderflow.product.CreateProductRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @Test
    void getProductById_existingProduct_returns200() {
        Product product = new Product(
                "TEST-001",
                "Test Product",
                new BigDecimal("25.00")
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ResponseEntity<?> response =
                productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(product, response.getBody());
    }

    @Test
    void getProductById_missingProduct_returns404() {
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response =
                productController.getProductById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                Map.of("error", "Product not found with ID: 999"),
                response.getBody()
        );
    }

    @Test
    void createProduct_validProduct_returns201() {
        CreateProductRequest request = new CreateProductRequest(
                "NEW-001",
                "New Product",
                new BigDecimal("30.00")
        );

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Product> response =
                productController.createProduct(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NEW-001", response.getBody().getSku());
        assertEquals("New Product", response.getBody().getName());
        assertEquals(
                0,
                new BigDecimal("30.00").compareTo(response.getBody().getPrice())
        );

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_existingProduct_returns200() {
        Product product = new Product(
                "OLD-001",
                "Old Product",
                new BigDecimal("25.00")
        );

        CreateProductRequest request = new CreateProductRequest(
                "UPDATED-001",
                "Updated Product",
                new BigDecimal("35.00")
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.saveAndFlush(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response =
                productController.updateProduct(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(product, response.getBody());

        assertEquals("UPDATED-001", product.getSku());
        assertEquals("Updated Product", product.getName());
        assertEquals(0, new BigDecimal("35.00")
                .compareTo(product.getPrice()));

        verify(productRepository).saveAndFlush(product);
    }

    @Test
    void updateProduct_missingProduct_returns404() {
        CreateProductRequest request = new CreateProductRequest(
                "TEST-001",
                "Test Product",
                new BigDecimal("25.00")
        );

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response =
                productController.updateProduct(999L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                Map.of("error", "Product not found with ID: 999"),
                response.getBody()
        );

        verify(productRepository, never())
                .saveAndFlush(any(Product.class));
    }
}