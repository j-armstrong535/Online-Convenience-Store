import React, { useEffect, useState } from "react";
import api from "../services/api";
import { Link } from "react-router-dom";
import "../styles/main.css";

export default function HomePage() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    api.get("/products").then(res => setProducts(res.data));
  }, []);

  // ✅ Add to Cart function — works with localStorage + backend
  const handleAddToCart = async (product) => {
    try {
      // Optional: still post to backend if needed
      await api.post("/cart/add", product).catch(() => {});

      // Get current cart from localStorage
      const storedCart = JSON.parse(localStorage.getItem("cart")) || [];

      // Check if product already exists
      const existingItem = storedCart.find((item) => item.id === product.id);

      if (existingItem) {
        existingItem.quantity += 1;
      } else {
        storedCart.push({
          id: product.id,
          name: product.name,
          price: product.price,
          imageUrl: `https://res.cloudinary.com/dtglrc8my/image/upload/${product.id}.jpg`,
          quantity: 1,
        });
      }

      // Save updated cart to localStorage
      localStorage.setItem("cart", JSON.stringify(storedCart));

      alert(`${product.name} added to cart!`);
    } catch (error) {
      console.error("Error adding to cart:", error);
      alert("Failed to add to cart. Please try again.");
    }
  };

  return (
    <div className="store-home">
      {/* Hero Section */}
      <section className="hero-premium">
        <div className="hero-content-premium">
          <h1>Your Everyday Essentials, Delivered Fast</h1>
          <p>Shop smarter with unbeatable prices and same-day delivery.</p>
          <Link to="/shop" className="hero-button">Start Shopping</Link>
        </div>
      </section>

      {/* Category Highlights */}
      <section className="category-highlight">
        <h2>Explore Categories</h2>
        <div className="category-grid-premium">
          <div className="category-card-premium"><span>Groceries</span></div>
          <div className="category-card-premium"><span>Drinks</span></div>
          <div className="category-card-premium"><span>Snacks</span></div>
          <div className="category-card-premium"><span>Personal Care</span></div>
        </div>
      </section>

      {/* Featured Products */}
      <section className="featured-premium">
        <h2>Featured Products</h2>
        <div className="product-grid-premium">
          {products.slice(0, 4).map((p) => (
            <div className="product-card-premium" key={p.id}>
              <img
                src={`https://res.cloudinary.com/dtglrc8my/image/upload/${p.id}.jpg`}
                alt={p.name}
                style={{ width: "100%", height: "120px", objectFit: "cover", marginBottom: "0.5rem" }}
                onError={e => { e.target.src = '/placeholder.png'; }}
              />
              <h3>{p.name}</h3>
              <p>${p.price.toFixed(2)}</p>
              <button onClick={() => handleAddToCart(p)}>Add to Cart</button>
            </div>
          ))}
        </div>
      </section>

      {/* Trust Badges */}
      <section className="trust-premium">
        <div className="trust-item">
          <h3>🚚 Fast Delivery</h3>
          <p>Get your order delivered the same day in most areas.</p>
        </div>
        <div className="trust-item">
          <h3>🔒 Secure Checkout</h3>
          <p>Your payments are safe with encrypted transactions.</p>
        </div>
        <div className="trust-item">
          <h3>⭐ Premium Quality</h3>
          <p>We stock only trusted, high-quality everyday products.</p>
        </div>
      </section>

      {/* About Section */}
      <section className="about-premium">
        <h2>Why Shop With Us?</h2>
        <p>
          We’re more than a convenience store — we’re your local shop online.
          From groceries and snacks to personal care, we bring quality and value to your doorstep.
        </p>
      </section>

      {/* Footer */}
      <footer className="footer-premium">
        <p>© 2025 Online Convenience Store | Everyday Value • Fast Delivery • Secure Payments</p>
      </footer>
    </div>
  );
}
