package com.online.store.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StoreAccountTest {

    @Test
    void roleDeterminesAccessPermissions() {
        StoreAccount manager = new StoreAccount("mgr", "mgr@example.com", "manager");
        StoreAccount admin = new StoreAccount("adm", "adm@example.com", "administrator");
        StoreAccount staff = new StoreAccount("staff", "staff@example.com", "staff");

        assertTrue(manager.hasInventoryAccess());
        assertTrue(manager.hasReportAccess());
        assertFalse(manager.hasUserManagementAccess());

        assertTrue(admin.hasInventoryAccess());
        assertTrue(admin.hasReportAccess());
        assertTrue(admin.hasUserManagementAccess());

        assertFalse(staff.hasInventoryAccess());
        assertFalse(staff.hasReportAccess());
        assertFalse(staff.hasUserManagementAccess());
    }

    @Test
    void activityMethodsIncrementCounters() {
        StoreAccount account = new StoreAccount("mgr", "mgr@example.com", "manager");

        account.manageInventory();
        account.generateSalesReports();
        account.processCustomerReturns();
        account.manageStaffAndSuppliers();
        account.modifyProductsAndCategories();

        assertEquals(2, account.getInventoryUpdates());
        assertEquals(1, account.getReportsGenerated());
        assertEquals(1, account.getReturnsProcessed());
        assertEquals(1, account.getStaffActions());
    }
}
