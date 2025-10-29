import React, { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import { FaShoppingCart } from "react-icons/fa";
import "../styles/main.css";

export default function Navbar() {
  const [cartCount, setCartCount] = useState(0);
  const location = useLocation();

  useEffect(() => {
    const updateCartInfo = () => {
      const storedCart = JSON.parse(localStorage.getItem("cart")) || [];
      const totalItems = storedCart.reduce((sum, item) => sum + item.quantity, 0);
      setCartCount(totalItems);
    };

    updateCartInfo();
    window.addEventListener("storage", updateCartInfo);
    return () => window.removeEventListener("storage", updateCartInfo);
  }, [location]);

  return (
    <nav className="navbar dark-theme">
      <h2 className="navbar-title">Online Convenience Store</h2>
      <ul>
        <li><Link to="/">Home</Link></li>
        <li><Link to="/products">Products</Link></li>

        <li className="navbar-cart">
          <Link to="/cart" className="navbar-cart-link">
            <FaShoppingCart className="navbar-cart-icon" />
            {cartCount > 0 && (
              <span className="navbar-cart-badge">{cartCount}</span>
            )}
          </Link>
        </li>
      </ul>
    </nav>
  );
}
