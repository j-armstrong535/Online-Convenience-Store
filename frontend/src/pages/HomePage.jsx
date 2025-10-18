import React from "react";
import { Link } from "react-router-dom";
import "../styles/main.css";

export default function HomePage() {
  return (
    <div className="home-container">
      {/* Hero Section */}
      <section className="hero">
        <div className="hero-content">
          <h1>Welcome to the Online Convenience Store 🛍️</h1>
          <p>Your one-stop shop for everyday essentials — fast, simple, and secure.</p>
          <Link to="/products" className="shop-button">
            Start Shopping
          </Link>
        </div>
      </section>

      {/* Featured Categories */}
      <section className="categories">
        <h2>Shop by Category</h2>
        <div className="category-grid">
          <div className="category-card">
            <img src="https://images.unsplash.com/photo-1585238342020-96629f60cdd0" alt="Snacks" />
            <h3>Snacks</h3>
          </div>
          <div className="category-card">
            <img src="https://images.unsplash.com/photo-1571687949920-e9b53af1cfb3" alt="Drinks" />
            <h3>Drinks</h3>
          </div>
          <div className="category-card">
            <img src="https://images.unsplash.com/photo-1615486366433-8b7b2adf06d1" alt="Household" />
            <h3>Household</h3>
          </div>
          <div className="category-card">
            <img src="https://images.unsplash.com/photo-1613482184972-bcd3b3ac8c2b" alt="Personal Care" />
            <h3>Personal Care</h3>
          </div>
        </div>
      </section>

      {/* About Section */}
      <section className="about">
        <h2>Why Shop With Us?</h2>
        <p>
          We’re dedicated to bringing you quality essentials at affordable prices. Whether it’s a quick snack,
          household item, or your favourite drink, our goal is to make your shopping simple and enjoyable.
        </p>
      </section>
    </div>
  );
}
