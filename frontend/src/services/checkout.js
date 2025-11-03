import api from "./api";
import { getCustomerAccountId, setCustomerAccountId } from "./user";
import { fetchCartItems, clearCart, readCachedCart } from "./cart";

export function getCachedCartSummary() {
  const cart = readCachedCart();
  const subtotal = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
  return { items: cart, subtotal };
}

export async function loadCartForCheckout() {
  const items = await fetchCartItems();
  const subtotal = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  return { items, subtotal };
}

export async function submitCheckout(payload) {
  const accountId = getCustomerAccountId();
  const requestBody = accountId
    ? { ...payload, customerAccountId: accountId }
    : payload;

  const response = await api.post("/checkout", requestBody);

  // Refresh local cart cache
  await clearCart();

  // If the backend returned an account id in headers (future-proofing)
  if (response?.headers?.["x-account-id"]) {
    setCustomerAccountId(response.headers["x-account-id"]);
  }

  return response.data;
}
