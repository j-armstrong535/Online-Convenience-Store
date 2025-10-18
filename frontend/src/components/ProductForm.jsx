import React, { useState } from "react";
import api from "../services/api";

export default function ProductForm({ onAdd }) {
  const [form, setForm] = useState({ name: "", category: "", price: "", stock: "" });

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    await api.post("/products", form);
    onAdd && onAdd();
    setForm({ name: "", category: "", price: "", stock: "" });
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3>Add Product</h3>
      <input name="name" placeholder="Name" value={form.name} onChange={handleChange} />
      <input name="category" placeholder="Category" value={form.category} onChange={handleChange} />
      <input name="price" placeholder="Price" type="number" value={form.price} onChange={handleChange} />
      <input name="stock" placeholder="Stock" type="number" value={form.stock} onChange={handleChange} />
      <button type="submit">Add</button>
    </form>
  );
}
