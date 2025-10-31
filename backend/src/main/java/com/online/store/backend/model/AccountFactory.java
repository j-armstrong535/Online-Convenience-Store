package com.online.store.backend.model;

/**
 * Factory class for creating different types of Account objects
 * Implements the Factory Method pattern
 */
public class AccountFactory {

    /**
     * Creates an Account based on the specified type
     * 
     * @param accountType The type of account to create
     * @param username    The username for the account
     * @param email       The email for the account
     * @return A new Account instance of the specified type
     * @throws IllegalArgumentException if accountType is null or unsupported
     */
    public static Account createAccount(AccountType accountType, String username, String email) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }

        switch (accountType) {
            case CUSTOMER:
                return new CustomerAccount(username, email);
            case STORE_MANAGER:
                return new StoreAccount(username, email, "manager");
            case STORE_ADMIN:
                return new StoreAccount(username, email, "administrator");
            default:
                throw new IllegalArgumentException("Unsupported account type: " + accountType);
        }
    }

    /**
     * Creates an Account based on a string type identifier
     * 
     * @param accountTypeString The string representation of the account type
     * @param username          The username for the account
     * @param email             The email for the account
     * @return A new Account instance of the specified type
     */
    public static Account createAccount(String accountTypeString, String username, String email) {
        AccountType accountType = AccountType.fromString(accountTypeString);
        return createAccount(accountType, username, email);
    }

    /**
     * Builder class for creating accounts with additional parameters
     */
    public static class AccountBuilder {
        private AccountType accountType;
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;

        // Store account specific fields
        private String department;
        private String accessLevel;

        // Customer account specific fields
        private String shippingAddress;
        private String paymentMethod;

        public AccountBuilder(AccountType accountType, String username, String email) {
            this.accountType = accountType;
            this.username = username;
            this.email = email;
        }

        public AccountBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AccountBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AccountBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AccountBuilder department(String department) {
            this.department = department;
            return this;
        }

        public AccountBuilder accessLevel(String accessLevel) {
            this.accessLevel = accessLevel;
            return this;
        }

        public AccountBuilder shippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public AccountBuilder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        /**
         * Builds and returns the configured Account instance
         * 
         * @return A fully configured Account instance
         */
        public Account build() {
            Account account = createAccount(accountType, username, email);

            // Set common fields
            account.setPassword(password);
            account.setFirstName(firstName);
            account.setLastName(lastName);

            // Set type-specific fields
            if (account instanceof StoreAccount) {
                StoreAccount storeAccount = (StoreAccount) account;
                if (department != null)
                    storeAccount.setDepartment(department);
                if (accessLevel != null)
                    storeAccount.setAccessLevel(accessLevel);
            } else if (account instanceof CustomerAccount) {
                CustomerAccount customerAccount = (CustomerAccount) account;
                if (shippingAddress != null)
                    customerAccount.setShippingAddress(shippingAddress);
                if (paymentMethod != null)
                    customerAccount.setPaymentMethod(paymentMethod);
            }

            return account;
        }
    }

    /**
     * Creates a new AccountBuilder instance
     * 
     * @param accountType The type of account to build
     * @param username    The username for the account
     * @param email       The email for the account
     * @return A new AccountBuilder instance
     */
    public static AccountBuilder builder(AccountType accountType, String username, String email) {
        return new AccountBuilder(accountType, username, email);
    }

    /**
     * Creates a new AccountBuilder instance from string type
     * 
     * @param accountTypeString The string representation of the account type
     * @param username          The username for the account
     * @param email             The email for the account
     * @return A new AccountBuilder instance
     */
    public static AccountBuilder builder(String accountTypeString, String username, String email) {
        AccountType accountType = AccountType.fromString(accountTypeString);
        return new AccountBuilder(accountType, username, email);
    }
}