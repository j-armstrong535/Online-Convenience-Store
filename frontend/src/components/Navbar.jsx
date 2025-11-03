import React, { useCallback, useEffect, useMemo, useState } from "react";
import { NavLink, useLocation, useNavigate } from "react-router-dom";
import { FaBars, FaShoppingCart, FaTimes } from "react-icons/fa";
import { fetchCartItems, readCachedCart } from "../services/cart";
import "../styles/main.css";

const ACCOUNT_TYPE_KEY = "accountType";
const DEFAULT_ACCOUNT_TYPE = "CustomerAccount";

export default function Navbar() {
  const [cartCount, setCartCount] = useState(0);
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [accountType, setAccountType] = useState(DEFAULT_ACCOUNT_TYPE);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const updateCartInfo = () => {
      const storedCart = readCachedCart();
      const totalItems = storedCart.reduce(
        (sum, item) => sum + (item?.quantity || 0),
        0
      );
      setCartCount(totalItems);
    };

    updateCartInfo();
    fetchCartItems().catch(() => {
      /* swallow errors for nav badge */
    });
    window.addEventListener("storage", updateCartInfo);
    return () => window.removeEventListener("storage", updateCartInfo);
  }, []);

  useEffect(() => {
    const storedAccountType =
      localStorage.getItem(ACCOUNT_TYPE_KEY) || DEFAULT_ACCOUNT_TYPE;
    setAccountType(storedAccountType);

    const handleStorageChange = (event) => {
      if (event.key === ACCOUNT_TYPE_KEY) {
        setAccountType(event.newValue || DEFAULT_ACCOUNT_TYPE);
      }
    };

    window.addEventListener("storage", handleStorageChange);
    return () => window.removeEventListener("storage", handleStorageChange);
  }, []);

  useEffect(() => {
    setDrawerOpen(false);
  }, [location]);

  const navItems = useMemo(() => {
    const baseItems = [
      { label: "Home", path: "/" },
      { label: "All Products", path: "/products" },
      { label: "Categories", path: "/categories" },
      { label: "Profile", path: "/profile" },
      { label: "Transaction History", path: "/transactions" },
    ];

    if (accountType === "StoreAccount") {
      return [...baseItems, { label: "Analytics", path: "/analytics" }];
    }
    return baseItems;
  }, [accountType]);

  const toggleDrawer = useCallback(() => {
    setDrawerOpen((open) => !open);
  }, []);

  const closeDrawer = useCallback(() => {
    setDrawerOpen(false);
  }, []);

  const handleNavigate = useCallback(
    (path) => {
      navigate(path);
      closeDrawer();
    },
    [closeDrawer, navigate]
  );

  return (
    <>
      <nav className="navbar appbar dark-theme">
        <button
          type="button"
          className="drawer-toggle"
          onClick={toggleDrawer}
          aria-label="Open navigation menu"
        >
          <FaBars />
        </button>

        <button
          type="button"
          className="navbar-title"
          onClick={() => handleNavigate("/")}
        >
          Online Convenience Store
        </button>

        <button
          type="button"
          className="navbar-cart-link"
          onClick={() => handleNavigate("/cart")}
          aria-label="Go to cart"
        >
          <FaShoppingCart className="navbar-cart-icon" />
          {cartCount > 0 && (
            <span className="navbar-cart-badge">{cartCount}</span>
          )}
        </button>
      </nav>

      <aside className={`nav-drawer ${drawerOpen ? "open" : ""}`}>
        <div className="nav-drawer-header">
          <h2>Menu</h2>
          <button
            type="button"
            className="drawer-close"
            onClick={closeDrawer}
            aria-label="Close navigation menu"
          >
            <FaTimes />
          </button>
        </div>

        <ul className="nav-drawer-list">
          {navItems.map((item) => (
            <li key={item.path} className="nav-drawer-item">
              <NavLink
                to={item.path}
                className={({ isActive }) =>
                  `nav-drawer-link ${isActive ? "active" : ""}`
                }
                onClick={closeDrawer}
              >
                {item.label}
              </NavLink>
            </li>
          ))}
        </ul>
      </aside>

      {drawerOpen && (
        <button
          type="button"
          className="nav-drawer-overlay"
          onClick={closeDrawer}
          aria-label="Close navigation overlay"
        />
      )}
    </>
  );
}
