package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.online.store.backend.model.Product;
import com.online.store.backend.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        ReflectionTestUtils.setField(productService, "repo", productRepository);
    }

    @Test
    void getAllProducts_returnsRepositoryResults() {
        List<Product> products = List.of(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(products, result);
        verify(productRepository).findAll();
    }

    @Test
    void addProduct_savesEntity() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(product);

        assertEquals(product, result);
        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_delegatesToRepository() {
        productService.deleteProduct("id");
        verify(productRepository).deleteById("id");
    }

    @Test
    void updateProduct_savesUpdatedEntity() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(product);

        assertEquals(product, result);
        verify(productRepository).save(product);
    }
}
