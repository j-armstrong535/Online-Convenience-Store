import React, { useState } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import "../styles/main.css";

export default function PaymentPage() {
  const [form, setForm] = useState({
    name: "",
    cardNumber: "",
    expiry: "",
    cvv: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!form.name || !form.cardNumber || !form.expiry || !form.cvv) {
      toast.error("⚠️ Please fill out all fields before paying.");
      return;
    }

    toast.success("💳 Payment Successful! Thank you for your purchase.", {
      autoClose: 2500,
    });

    localStorage.removeItem("cart");

    setForm({ name: "", cardNumber: "", expiry: "", cvv: "" });

    // Redirect back to homepage after 2.5s
    setTimeout(() => navigate("/"), 2600);
  };

  return (
    <div className="payment-page">
      <h1>Secure Payment</h1>
      <form className="payment-form" onSubmit={handleSubmit}>
        <label>
          Cardholder Name
          <input
            type="text"
            name="name"
            placeholder="John Doe"
            value={form.name}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Card Number
          <input
            type="text"
            name="cardNumber"
            placeholder="1234 5678 9012 3456"
            maxLength="19"
            value={form.cardNumber}
            onChange={handleChange}
            required
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
              required
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
              required
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
