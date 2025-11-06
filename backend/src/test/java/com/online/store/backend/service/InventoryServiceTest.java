package com.online.store.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online.store.backend.model.Cart;
import com.online.store.backend.model.Inventory;
import com.online.store.backend.model.InventoryEvent;
import com.online.store.backend.model.InventoryObserver;
import com.online.store.backend.model.Product;
import com.online.store.backend.model.ProductCategory;
import com.online.store.backend.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() throws Exception {
        resetInventorySingleton();
        inventoryService = new InventoryService(productRepository);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void registerCategory_allowsCategoryNormalization() {
        ProductCategory category = new ProductCategory("Snacks", "desc", false);
        inventoryService.registerCategory(category);

        Product product = product("SKU-1", "Chips", "snacks");
        inventoryService.addProductToInventory(product, 5);

        assertEquals("Snacks", product.getCategory());
        assertEquals(5, Inventory.getInstance().getStockLevel("SKU-1"));
    }

    @Test
    void addProductToInventory_persistsAndNotifiesObserver() {
        TestObserver observer = new TestObserver();
        inventoryService.registerObserver(observer);

        Product product = product("SKU-2", "Juice", "Beverages");
        inventoryService.addProductToInventory(product, 3);

        assertNotNull(observer.lastEvent);
        assertEquals("SKU-2", observer.lastEvent.getProductId());
        assertEquals(3, observer.lastEvent.getDelta());
        assertEquals(3, observer.lastEvent.getNewQuantity());
        verify(productRepository).save(product);
    }

    @Test
    void restockProduct_increasesStockWhenPresent() {
        Product product = product("SKU-3", "Tea", "Drinks");
        inventoryService.addProductToInventory(product, 2);
        when(productRepository.findById("SKU-3")).thenReturn(Optional.of(product));

        inventoryService.restockProduct("SKU-3", 4);

        assertEquals(6, Inventory.getInstance().getStockLevel("SKU-3"));
        verify(productRepository, times(2)).save(product);
    }

    @Test
    void restockProduct_ignoresMissingProduct() {
        when(productRepository.findById("missing")).thenReturn(Optional.empty());

        inventoryService.restockProduct("missing", 5);

        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void reduceStock_decrementsAndPersistsWhenEnoughStock() {
        Product product = product("SKU-4", "Bread", "Bakery");
        inventoryService.addProductToInventory(product, 5);
        when(productRepository.findById("SKU-4")).thenReturn(Optional.of(product));

        boolean success = inventoryService.reduceStock("SKU-4", 3);

        assertTrue(success);
        assertEquals(2, Inventory.getInstance().getStockLevel("SKU-4"));
        verify(productRepository, times(2)).save(product);
    }

    @Test
    void reduceStock_returnsFalseWhenInsufficientStock() {
        Product product = product("SKU-5", "Butter", "Dairy");
        inventoryService.addProductToInventory(product, 2);
        when(productRepository.findById("SKU-5")).thenReturn(Optional.of(product));

        boolean success = inventoryService.reduceStock("SKU-5", 5);

        assertFalse(success);
        assertEquals(2, Inventory.getInstance().getStockLevel("SKU-5"));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void applyOrder_reducesEachItemInCart() {
        Product apples = product("SKU-6", "Apples", "Produce");
        Product oranges = product("SKU-7", "Oranges", "Produce");
        inventoryService.addProductToInventory(apples, 10);
        inventoryService.addProductToInventory(oranges, 8);

        when(productRepository.findById("SKU-6")).thenReturn(Optional.of(apples));
        when(productRepository.findById("SKU-7")).thenReturn(Optional.of(oranges));

        Cart cart = new Cart("customer");
        cart.addProduct(apples);
        cart.addProduct(apples);
        cart.addProduct(oranges);

        inventoryService.applyOrder(cart);

        assertEquals(8, Inventory.getInstance().getStockLevel("SKU-6"));
        assertEquals(7, Inventory.getInstance().getStockLevel("SKU-7"));
        verify(productRepository, times(2)).save(apples);
        verify(productRepository, times(2)).save(oranges);
    }

    @Test
    void getStockLevel_returnsNullWhenUnknown() {
        assertEquals(null, inventoryService.getStockLevel("unknown"));
    }

    private void resetInventorySingleton() throws Exception {
        Inventory inventory = Inventory.getInstance();
        clearMap(inventory, "productStock");
        clearMap(inventory, "categoryIndex");
        Field observersField = Inventory.class.getDeclaredField("observers");
        observersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<InventoryObserver> observers = (List<InventoryObserver>) observersField.get(inventory);
        observers.clear();
    }

    private void clearMap(Inventory inventory, String fieldName) throws Exception {
        Field field = Inventory.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, ?> map = (Map<String, ?>) field.get(inventory);
        map.clear();
    }

    private Product product(String id, String name, String category) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(5.0);
        return product;
    }

    private static class TestObserver implements InventoryObserver {
        InventoryEvent lastEvent;

        @Override
        public void onInventoryChanged(InventoryEvent event) {
            this.lastEvent = event;
        }
    }
}
