import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

export default function SignupPage() {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    shippingAddress: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8081/api/accounts/customer", form);

      if (res.data.success) {
        // ✅ Save account info to localStorage
        const userData = {
          username: form.username,
          email: form.email,
          firstName: form.firstName,
          lastName: form.lastName,
          accountType: "customer",
        };

        localStorage.setItem("user", JSON.stringify(userData));
        localStorage.setItem("accountType", "customer");

        window.dispatchEvent(new Event("storage"));

        toast.success("Account created successfully!");
        navigate("/");

        setTimeout(() => {
          window.dispatchEvent(new Event("storage"));
        }, 100);
      } else {
        toast.error(res.data.message || "Signup failed");
        console.error("Signup error:", res.data.message);
      }
    } catch (err) {
      if (err.response) {
        // Server responded with an error status
        console.error("Signup error:", err.response.data);
        toast.error(err.response.data.message || "Server error during signup");
      } else if (err.request) {
        // No response received
        console.error("No response received:", err.request);
        toast.error("No response from backend. Is it running?");
      } else {
        // Something else went wrong
        console.error("Unexpected error:", err.message);
        toast.error("Unexpected error: " + err.message);
      }
    }
  };

  return (
    <div className="auth-container">
      <h2>Sign Up</h2>
      <form onSubmit={handleSignup} className="auth-form">
        <input
          name="username"
          placeholder="Username"
          onChange={handleChange}
          required
        />
        <input
          name="email"
          type="email"
          placeholder="Email"
          onChange={handleChange}
          required
        />
        <input
          name="password"
          type="password"
          placeholder="Password"
          onChange={handleChange}
          required
        />
        <input
          name="firstName"
          placeholder="First Name"
          onChange={handleChange}
          required
        />
        <input
          name="lastName"
          placeholder="Last Name"
          onChange={handleChange}
          required
        />
        <input
          name="shippingAddress"
          placeholder="Shipping Address"
          onChange={handleChange}
        />
        <button type="submit">Create Account</button>
      </form>
      <p>
        Already have an account? <a href="/login">Login</a>
      </p>
    </div>
  );
}
