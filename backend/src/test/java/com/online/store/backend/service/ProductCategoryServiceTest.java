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

import com.online.store.backend.model.ProductCategory;
import com.online.store.backend.repository.ProductCategoryRepository;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private InventoryService inventoryService;

    private ProductCategoryService productCategoryService;

    @BeforeEach
    void setUp() {
        productCategoryService = new ProductCategoryService(productCategoryRepository, inventoryService);
    }

    @Test
    void findAll_returnsRepositoryResults() {
        List<ProductCategory> categories = List.of(new ProductCategory(), new ProductCategory());
        when(productCategoryRepository.findAll()).thenReturn(categories);

        List<ProductCategory> result = productCategoryService.findAll();

        assertEquals(categories, result);
        verify(productCategoryRepository).findAll();
    }

    @Test
    void createCategory_savesAndRegisters() {
        ProductCategory category = new ProductCategory("Snacks", "desc", false);
        when(productCategoryRepository.save(category)).thenReturn(category);

        ProductCategory created = productCategoryService.createCategory(category);

        assertEquals(category, created);
        verify(productCategoryRepository).save(category);
        verify(inventoryService).registerCategory(category);
    }

    @Test
    void updateCategory_savesAndRegisters() {
        ProductCategory category = new ProductCategory("Snacks", "desc", false);
        when(productCategoryRepository.save(category)).thenReturn(category);

        ProductCategory updated = productCategoryService.updateCategory(category);

        assertEquals(category, updated);
        verify(productCategoryRepository).save(category);
        verify(inventoryService).registerCategory(category);
    }

    @Test
    void deleteCategory_delegatesToRepository() {
        productCategoryService.deleteCategory("id");
        verify(productCategoryRepository).deleteById("id");
    }

    @Test
    void findByName_usesRepository() {
        ProductCategory category = new ProductCategory("Snacks", "desc", false);
        when(productCategoryRepository.findByNameIgnoreCase("snacks")).thenReturn(category);

        ProductCategory found = productCategoryService.findByName("snacks");

        assertEquals(category, found);
    }
}
