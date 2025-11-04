import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "../styles/main.css";

export default function StoreSignupPage() {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    department: "",
    accessLevel: "manager",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8081/api/accounts/manager", form);

      if (res.data.success) {
        toast.success("Store account created successfully!");
        navigate("/store-login");
      } else {
        toast.error(res.data.message || "Signup failed");
      }
    } catch (err) {
      console.error("Store signup error:", err);
      if (err.response) {
        toast.error(err.response.data.message || "Error from server");
      } else if (err.request) {
        toast.error("No response from backend. Is it running?");
      } else {
        toast.error("Unexpected error: " + err.message);
      }
    }
  };

  return (
    <div className="auth-container store-auth">
      <div className="auth-card">
        <h2 className="auth-title">Store Staff Signup</h2>
        <p className="auth-subtitle">Create a manager or administrator account</p>

        <form onSubmit={handleSignup} className="auth-form">
          <div className="input-group">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              name="username"
              placeholder="Username"
              value={form.username}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              placeholder="store@example.com"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              placeholder="Enter password"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label htmlFor="firstName">First Name</label>
            <input
              id="firstName"
              name="firstName"
              placeholder="First Name"
              value={form.firstName}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label htmlFor="lastName">Last Name</label>
            <input
              id="lastName"
              name="lastName"
              placeholder="Last Name"
              value={form.lastName}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label htmlFor="department">Department</label>
            <input
              id="department"
              name="department"
              placeholder="e.g. Sales, Inventory"
              value={form.department}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label htmlFor="accessLevel">Access Level</label>
            <select
              id="accessLevel"
              name="accessLevel"
              value={form.accessLevel}
              onChange={handleChange}
              required
            >
              <option value="manager">Store Manager</option>
              <option value="administrator">Store Administrator</option>
            </select>
          </div>

          <button type="submit" className="auth-btn">
            Create Store Account
          </button>
        </form>

        <p className="auth-switch">
          Already have a store account? <a href="/store-login">Login here</a>
        </p>

        <p className="auth-switch">
          Back to <a href="/login">Customer Login</a>
        </p>
      </div>
    </div>
  );
}
