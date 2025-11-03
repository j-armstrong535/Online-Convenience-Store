import React, { useEffect, useMemo, useState } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import {
  getCachedCartSummary,
  loadCartForCheckout,
  submitCheckout,
} from "../services/checkout";
import "../styles/main.css";

const initialForm = {
  name: "",
  email: "",
  address: "",
  cardNumber: "",
  expiry: "",
  cvv: "",
  paymentMethod: "CARD",
  fulfilmentMethod: "PICKUP",
};

export default function PaymentPage() {
  const [form, setForm] = useState(initialForm);
  const [cartItems, setCartItems] = useState([]);
  const [subtotal, setSubtotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const cached = getCachedCartSummary();
    if (cached.items.length > 0) {
      setCartItems(cached.items);
      setSubtotal(cached.subtotal);
    }

    async function hydrateCart() {
      try {
        const { items, subtotal: total } = await loadCartForCheckout();
        setCartItems(items);
        setSubtotal(total);
      } catch (err) {
        console.error("Failed to load cart for checkout", err);
        if (cached.items.length === 0) {
          setError("We couldn't load your cart. Please try again.");
        }
      } finally {
        setLoading(false);
      }
    }

    hydrateCart();
  }, []);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  };

  const taxEstimate = useMemo(() => subtotal * 0.1, [subtotal]);
  const deliveryFee = useMemo(
    () => (form.fulfilmentMethod === "DELIVERY" ? 7.5 : 0),
    [form.fulfilmentMethod]
  );
  const totalEstimate = subtotal + taxEstimate + deliveryFee;

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (cartItems.length === 0) {
      toast.error("🛒 Your cart is empty. Please add items before checking out.");
      return;
    }

    if (!form.name || !form.email) {
      toast.error("⚠️ Please provide your name and email before paying.");
      return;
    }

    const [firstName, ...rest] = form.name.trim().split(" ");
    const lastName = rest.length > 0 ? rest.join(" ") : firstName;
    const payload = {
      username: form.email.toLowerCase(),
      email: form.email.toLowerCase(),
      firstName,
      lastName,
      shippingAddress:
        form.fulfilmentMethod === "DELIVERY" ? form.address : "",
      paymentMethod: form.paymentMethod,
      fulfilmentMethod: form.fulfilmentMethod,
      accountType: "CUSTOMER",
    };

    try {
      const receipt = await submitCheckout(payload);
      toast.success(
        `💳 Payment successful! Receipt ${receipt.receiptId} generated.`,
        { autoClose: 2500 }
      );
      setCartItems([]);
      setSubtotal(0);
      setForm(initialForm);
      setTimeout(() => navigate("/"), 2600);
    } catch (err) {
      console.error("Checkout failed", err);
      toast.error("Payment failed. Please try again.");
    }
  };

  return (
    <div className="payment-page">
      <h1>Secure Payment</h1>

      {loading && <p className="loading">Preparing checkout...</p>}
      {error && <p className="error">{error}</p>}

      {cartItems.length > 0 && (
        <section className="payment-summary">
          <h2>Order Summary</h2>
          <ul>
            {cartItems.map((item) => (
              <li key={item.id}>
                {item.name} × {item.quantity}
              </li>
            ))}
          </ul>
          <div className="summary-row">Subtotal: ${subtotal.toFixed(2)}</div>
          <div className="summary-row">Tax (est. 10%): ${taxEstimate.toFixed(2)}</div>
          {deliveryFee > 0 && (
            <div className="summary-row">Delivery: ${deliveryFee.toFixed(2)}</div>
          )}
          <div className="summary-total">Total (est.): ${totalEstimate.toFixed(2)}</div>
        </section>
      )}

      <form className="payment-form" onSubmit={handleSubmit}>
        <label>
          Full Name
          <input
            type="text"
            name="name"
            placeholder="Jane Customer"
            value={form.name}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Email Address
          <input
            type="email"
            name="email"
            placeholder="jane@example.com"
            value={form.email}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Delivery Address (required for delivery)
          <input
            type="text"
            name="address"
            placeholder="123 Main St, Melbourne"
            value={form.address}
            onChange={handleChange}
            required={form.fulfilmentMethod === "DELIVERY"}
          />
        </label>

        <div className="payment-row">
          <label>
            Fulfilment Method
            <select
              name="fulfilmentMethod"
              value={form.fulfilmentMethod}
              onChange={handleChange}
            >
              <option value="PICKUP">Pickup</option>
              <option value="DELIVERY">Delivery</option>
            </select>
          </label>
          <label>
            Payment Method
            <select
              name="paymentMethod"
              value={form.paymentMethod}
              onChange={handleChange}
            >
              <option value="CARD">Card</option>
              <option value="PAYPAL">PayPal</option>
              <option value="CASH">Cash on Delivery</option>
            </select>
          </label>
        </div>

        <label>
          Card Number
          <input
            type="text"
            name="cardNumber"
            placeholder="1234 5678 9012 3456"
            maxLength="19"
            value={form.cardNumber}
            onChange={handleChange}
            required={form.paymentMethod === "CARD"}
          />
        </label>

        <div className="payment-row">
          <label>
            Expiry Date
            <input
              type="text"
              name="expiry"
              placeholder="MM/YY"
              maxLength="5"
              value={form.expiry}
              onChange={handleChange}
              required={form.paymentMethod === "CARD"}
            />
          </label>
          <label>
            CVV
            <input
              type="password"
              name="cvv"
              placeholder="123"
              maxLength="4"
              value={form.cvv}
              onChange={handleChange}
              required={form.paymentMethod === "CARD"}
            />
          </label>
        </div>

        <button type="submit" className="pay-btn">
          Pay Now
        </button>
      </form>
    </div>
  );
}
