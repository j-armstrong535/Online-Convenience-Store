import React from "react";
import { Link } from "react-router-dom";
import "../styles/main.css";

export default function HomePage() {
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
          <div className="product-card-premium">
            <div className="product-img-placeholder"></div>
            <h3>Product Name</h3>
            <p>$???</p>
            <button>Add to Cart</button>
          </div>
          <div className="product-card-premium">
            <div className="product-img-placeholder"></div>
            <h3>Product Name</h3>
            <p>$???</p>
            <button>Add to Cart</button>
          </div>
          <div className="product-card-premium">
            <div className="product-img-placeholder"></div>
            <h3>Product Name</h3>
            <p>$???</p>
            <button>Add to Cart</button>
          </div>
          <div className="product-card-premium">
            <div className="product-img-placeholder"></div>
            <h3>Product Name</h3>
            <p>$???</p>
            <button>Add to Cart</button>
          </div>
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
