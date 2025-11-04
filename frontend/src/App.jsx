import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import Navbar from "./components/Navbar";
import HomePage from "./pages/HomePage";
import CartPage from "./pages/CartPage";
import ProductsPage from "./pages/ProductsPage";
import PaymentPage from "./pages/PaymentPage";
import ReceiptPage from "./pages/ReceiptPage";
import CategoriesPage from "./pages/CategoriesPage";
import CategoryProductsPage from "./pages/CategoryProductsPage";
import ProfilePage from "./pages/ProfilePage";
import TransactionHistoryPage from "./pages/TransactionHistoryPage";
import AnalyticsPage from "./pages/AnalyticsPage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import "./styles/main.css";

function App() {
  // --- Global search state ---
  const [searchTerm, setSearchTerm] = useState("");

  return (
    <Router>
      {/* Pass searchTerm and setSearchTerm to Navbar */}
      <Navbar searchTerm={searchTerm} setSearchTerm={setSearchTerm} />

      <main style={{ paddingTop: "0rem" }}>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route
            path="/products"
            element={<ProductsPage searchTerm={searchTerm} />}
          />
          <Route path="/payment" element={<PaymentPage />} />
          <Route path="/receipt" element={<ReceiptPage />} />
          <Route path="/categories" element={<CategoriesPage />} />
          <Route
            path="/categories/:categoryName"
            element={<CategoryProductsPage searchTerm={searchTerm} />}
          />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/transactions" element={<TransactionHistoryPage />} />
          <Route path="/analytics" element={<AnalyticsPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
        </Routes>
      </main>

      <ToastContainer
        position="top-right"
        autoClose={2000}
        hideProgressBar={false}
        closeOnClick
        pauseOnFocusLoss
        pauseOnHover
        theme="colored"
      />
    </Router>
  );
}

export default App;
