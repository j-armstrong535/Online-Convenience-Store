import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import HomePage from "./pages/HomePage";
import CartPage from "./pages/CartPage";
import "./styles/main.css";

function App() {
  return (
    <Router>
      <Navbar />
      <main style={{ paddingTop: "2rem" }}>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/products" element={<HomePage />} />
        </Routes>
      </main>
    </Router>
  );
}

export default App;
