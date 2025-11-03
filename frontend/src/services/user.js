const CART_USER_KEY = "cartUserId";
const CUSTOMER_ACCOUNT_KEY = "customerAccountId";

function dispatchStorageEvent() {
  if (typeof window !== "undefined") {
    window.dispatchEvent(new Event("storage"));
  }
}

function createGuestId() {
  if (typeof crypto !== "undefined" && crypto.randomUUID) {
    return `guest-${crypto.randomUUID()}`;
  }
  return `guest-${Date.now()}-${Math.floor(Math.random() * 1000)}`;
}

export function getOrCreateCartUserId() {
  let id = localStorage.getItem(CART_USER_KEY);
  if (!id) {
    id = createGuestId();
    localStorage.setItem(CART_USER_KEY, id);
    dispatchStorageEvent();
  }
  return id;
}

export function getCustomerAccountId() {
  return localStorage.getItem(CUSTOMER_ACCOUNT_KEY) || null;
}

export function setCustomerAccountId(accountId) {
  if (!accountId) return;
  localStorage.setItem(CUSTOMER_ACCOUNT_KEY, accountId);
  dispatchStorageEvent();
}

export function clearLocalAccount() {
  localStorage.removeItem(CUSTOMER_ACCOUNT_KEY);
  dispatchStorageEvent();
}
