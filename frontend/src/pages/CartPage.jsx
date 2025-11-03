import React, { useState, useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import {
  fetchCartItems,
  removeProductFromCart,
  setProductQuantity,
} from "../services/cart";
import "../styles/cart.css";

export default function CartPage() {
  const [cart, setCart] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadCart() {
      try {
        const items = await fetchCartItems();
        setCart(items);
      } catch (err) {
        console.error("Failed to load cart", err);
        setError("Unable to load cart. Please try again later.");
      } finally {
        setLoading(false);
      }
    }

    loadCart();
  }, []);

  const removeFromCart = async (item) => {
    try {
      const items = await removeProductFromCart(item.id);
      setCart(items);
    } catch (err) {
      console.error("Failed to remove product from cart", err);
      setError("Failed to remove item. Please try again.");
    }
  };

  const updateQuantity = async (item, change) => {
    const nextQuantity = Math.max(1, item.quantity + change);
    try {
      const items = await setProductQuantity(item.product, nextQuantity);
      setCart(items);
    } catch (err) {
      console.error("Failed to update cart quantity", err);
      setError("Failed to update item quantity. Please try again.");
    }
  };

  // Calculate total price
  const totalPrice = useMemo(
    () => cart.reduce((sum, item) => sum + item.price * item.quantity, 0),
    [cart]
  );
  // Navigation to payment page
  const navigate = useNavigate();

  return (
    <div className="cart-container">
      <h1>Your Shopping Cart</h1>

      {loading ? (
        <p className="empty">Loading your cart...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : cart.length === 0 ? (
        <p className="empty">Your cart is empty — start shopping!</p>
      ) : (
        <div className="cart-items">
          {cart.map((item) => (
            <div className="cart-item" key={item.id}>
              <img
                src={item.imageUrl || "/placeholder.png"}
                alt={item.name}
                className="cart-image"
              />
              <div className="cart-details">
                <h3>{item.name}</h3>
                <p>${item.price.toFixed(2)}</p>
                <div className="quantity-controls">
                  <button onClick={() => updateQuantity(item, -1)}>-</button>
                  <span>{item.quantity}</span>
                  <button onClick={() => updateQuantity(item, 1)}>+</button>
                </div>
                <button
                  className="remove-btn"
                  onClick={() => removeFromCart(item)}
                >
                  Remove
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {!loading && cart.length > 0 && (
        <div className="cart-summary">
          <h2>Total: ${totalPrice.toFixed(2)}</h2>
          <button className="checkout-btn" onClick={() => navigate("/payment")}>Proceed to Checkout</button>
        </div>
      )}
    </div>
  );
}
