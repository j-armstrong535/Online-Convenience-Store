import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import api from "../services/api";
import { addProductToCart } from "../services/cart";
import { Link, useNavigate } from "react-router-dom";
import { getProductImageUrl, handleImageError } from "../utils/imageUtils";
import "../styles/main.css";

export default function HomePage() {
  const [products, setProducts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    api.get("/products").then(res => setProducts(res.data));
  }, []);

  const handleAddToCart = async (product) => {
    try {
      await addProductToCart(product);
      toast.success(`🛍️ ${product.name} added to cart!`);
    } catch (error) {
      console.error("Error adding to cart:", error);
      alert("Failed to add to cart. Please try again.");
    }
  };

  const handleCategoryClick = (categoryName) => {
    navigate(`/categories/${categoryName.toLowerCase()}`);
  };

  return (
    <div className="store-home">
      {/* Hero Section */}
      <section className="hero-premium">
        <div className="hero-content-premium">
          <h1>Your Everyday Essentials, Delivered Fast</h1>
          <p>Shop smarter with unbeatable prices and same-day delivery.</p>
          <Link to="/products" className="hero-button">Start Shopping</Link>
        </div>
      </section>

      {/* Featured Products */}
      <section className="featured-premium">
        <h2>Featured Products</h2>
        <div className="product-grid-premium">
          {products.slice(0, 4).map((p) => (
            <div className="product-card-premium" key={p.id}>
              <img
                src={getProductImageUrl(p)}
                alt={p.name}
                style={{ width: "100%", height: "120px", objectFit: "cover", marginBottom: "0.5rem" }}
                onError={(e) => handleImageError(e, p)}
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
          <p>Get your order delivered the same day in most areas. Delivered by our trusted couriers.</p>
        </div>
        <div className="trust-item">
          <h3>🔒 Secure Checkout</h3>
          <p>Your payments are safe with encrypted transactions. Shop with confidence.</p>
        </div>
        <div className="trust-item">
          <h3>⭐ Premium Quality</h3>
          <p>We stock only trusted, high-quality everyday products. Satisfaction guaranteed, or your money back.</p>
        </div>
      </section>

      {/* About Section */}
      <section className="about-premium">
        <h2>Why Shop With Us?</h2>
        <p>
          We’re more than a convenience store — we’re your local shop online.
          From groceries and snacks to personal care, we bring quality and value to your doorstep.
          Enjoy everyday low prices, fast delivery if you need it, and a seamless shopping experience.
          If you're coming in store, our friendly staff are here to help you find what you need quickly.
        </p>
      </section>

      {/* Footer */}
      <footer className="footer-premium">
        <p>© 2025 Online Convenience Store | Everyday Value • Fast Delivery • Secure Payments</p>
      </footer>
    </div>
  );
}
