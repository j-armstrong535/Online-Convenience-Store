import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "../styles/main.css";

export default function StoreLoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8081/api/accounts/login", {
        email,
        password,
      });

      if (res.data.success) {
        const userData = res.data.data;
        if (
          userData.accountType === "store_manager" ||
          userData.accountType === "store_admin"
        ) {
          localStorage.setItem("user", JSON.stringify(userData));
          localStorage.setItem("accountType", "StoreAccount");
          window.dispatchEvent(new Event("storage"));
          toast.success("Store login successful!");
          navigate("/analytics");
          setTimeout(() => window.dispatchEvent(new Event("storage")), 100);
        } else {
          toast.error("This login is for store accounts only.");
        }
      } else {
        toast.error(res.data.message || "Login failed");
      }
    } catch (err) {
      toast.error("Invalid email or password");
    }
  };

  return (
    <div className="auth-container store-auth">
      <div className="auth-card">
        <h2 className="auth-title">Store Portal Login</h2>
        <p className="auth-subtitle">Managers & Administrators Only</p>

        <form onSubmit={handleLogin} className="auth-form">
          <div className="input-group">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              placeholder="store@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="input-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="auth-btn">
            Login
          </button>
        </form>

        <p className="auth-switch">
          New store staff? <a href="/store-signup">Create a store account</a>
        </p>

        <p className="auth-switch">
          Back to <a href="/login">Customer Login</a>
        </p>
      </div>
    </div>
  );
}
