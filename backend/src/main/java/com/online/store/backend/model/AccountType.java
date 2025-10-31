package com.online.store.backend.model;

/**
 * Enum representing different types of accounts that can be created
 */
public enum AccountType {
    CUSTOMER("customer"),
    STORE_MANAGER("store_manager"),
    STORE_ADMIN("store_admin");

    private final String type;

    AccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static AccountType fromString(String type) {
        for (AccountType accountType : AccountType.values()) {
            if (accountType.type.equalsIgnoreCase(type)) {
                return accountType;
            }
        }
        throw new IllegalArgumentException("Unknown account type: " + type);
    }
}