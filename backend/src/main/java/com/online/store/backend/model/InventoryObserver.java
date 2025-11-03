package com.online.store.backend.model;

/**
 * Observer interface used to keep dependent components in sync with inventory changes.
 */
public interface InventoryObserver {
    void onInventoryChanged(InventoryEvent event);
}
