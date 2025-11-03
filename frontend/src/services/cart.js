import api from "./api";
import { getOrCreateCartUserId } from "./user";

const STORAGE_KEY = "cart";

function mapCartItems(cart) {
  if (!cart || !Array.isArray(cart.items)) {
    return [];
  }

  return cart.items.map((item) => {
    const product = item.product || {};
    const id = product.id;
    const name = product.name || "Unnamed product";
    const price = typeof product.price === "number" ? product.price : 0;
    const quantity = item.quantity || 0;
    const imageUrl =
      product.imageUrl ||
      `https://res.cloudinary.com/dtglrc8my/image/upload/${id}.jpg`;

    return {
      id,
      name,
      price,
      quantity,
      imageUrl,
      product,
    };
  });
}

function writeCartToStorage(items) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
  if (typeof window !== "undefined") {
    window.dispatchEvent(new Event("storage"));
  }
}

function normaliseCartResponse(response) {
  const items = mapCartItems(response);
  writeCartToStorage(items);
  return items;
}

export async function fetchCartItems() {
  const userId = getOrCreateCartUserId();
  const res = await api.get("/cart", { params: { userId } });
  return normaliseCartResponse(res.data);
}

export async function addProductToCart(product) {
  const userId = getOrCreateCartUserId();
  const res = await api.post("/cart/add", product, { params: { userId } });
  return normaliseCartResponse(res.data);
}

export async function removeProductFromCart(productId) {
  const userId = getOrCreateCartUserId();
  const res = await api.delete(`/cart/remove/${productId}`, {
    params: { userId },
  });
  return normaliseCartResponse(res.data);
}

export async function setProductQuantity(product, quantity) {
  if (!product?.id) {
    return fetchCartItems();
    }

  const safeQuantity = Math.max(0, quantity);
  const userId = getOrCreateCartUserId();

  // Remove existing entry
  let res = await api.delete(`/cart/remove/${product.id}`, {
    params: { userId },
  });

  if (safeQuantity > 0) {
    for (let i = 0; i < safeQuantity; i += 1) {
      res = await api.post("/cart/add", product, { params: { userId } });
    }
  }

  return normaliseCartResponse(res?.data);
}

export async function clearCart() {
  const userId = getOrCreateCartUserId();
  const res = await api.delete("/cart/clear", { params: { userId } });
  // Controller returns void, so use empty array
  if (!res || !res.data) {
    writeCartToStorage([]);
    return [];
  }
  return normaliseCartResponse(res.data);
}

export function readCachedCart() {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    return stored ? JSON.parse(stored) : [];
  } catch (error) {
    console.error("Failed to parse cached cart", error);
    return [];
  }
}
