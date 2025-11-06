package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() throws Exception {
        inventory = Inventory.getInstance();
        clearInventory();
    }

    @Test
    void registerCategory_normalizesNamesForProducts() {
        inventory.registerCategory(new ProductCategory("Snacks", "desc", false));
        Product product = product("SKU-1", "snacks");

        inventory.addProduct(product, 5);

        assertEquals("Snacks", product.getCategory());
        assertEquals(5, inventory.getStockLevel("SKU-1"));
    }

    @Test
    void addProduct_notifiesObservers() {
        TestObserver observer = new TestObserver();
        inventory.registerObserver(observer);

        inventory.addProduct(product("SKU-2", "Drinks"), 3);

        assertNotNull(observer.event);
        assertEquals("SKU-2", observer.event.getProductId());
        assertEquals(3, observer.event.getNewQuantity());
    }

    @Test
    void restockProduct_increasesQuantity() {
        Product product = product("SKU-3", "Bakery");
        inventory.addProduct(product, 2);

        inventory.restockProduct("SKU-3", 4);

        assertEquals(6, inventory.getStockLevel("SKU-3"));
    }

    @Test
    void reduceStock_decreasesWhenEnough() {
        Product product = product("SKU-4", "Bakery");
        inventory.addProduct(product, 5);

        assertTrue(inventory.reduceStock("SKU-4", 3));
        assertEquals(2, inventory.getStockLevel("SKU-4"));
    }

    @Test
    void reduceStock_returnsFalseWhenInsufficient() {
        Product product = product("SKU-5", "Bakery");
        inventory.addProduct(product, 2);

        assertFalse(inventory.reduceStock("SKU-5", 5));
        assertEquals(2, inventory.getStockLevel("SKU-5"));
    }

    @Test
    void unregisterObserver_stopsNotifications() {
        TestObserver observer = new TestObserver();
        inventory.registerObserver(observer);
        inventory.unregisterObserver(observer);

        inventory.addProduct(product("SKU-6", "Produce"), 1);

        assertNull(observer.event);
    }

    @Test
    void getStockLevel_returnsNullForUnknownProduct() {
        assertNull(inventory.getStockLevel("missing"));
    }

    private Product product(String id, String category) {
        Product product = new Product();
        product.setId(id);
        product.setName("Product " + id);
        product.setCategory(category);
        return product;
    }

    private void clearInventory() throws Exception {
        resetField("productStock");
        resetField("categoryIndex");
        Field observersField = Inventory.class.getDeclaredField("observers");
        observersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<InventoryObserver> observers = (List<InventoryObserver>) observersField.get(inventory);
        observers.clear();
    }

    private void resetField(String fieldName) throws Exception {
        Field field = Inventory.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, ?> map = (Map<String, ?>) field.get(inventory);
        map.clear();
    }

    private static class TestObserver implements InventoryObserver {
        InventoryEvent event;

        @Override
        public void onInventoryChanged(InventoryEvent event) {
            this.event = event;
        }
    }
}
