import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";  
import api from "../services/api";
import "../styles/main.css";

export default function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function fetchProducts() {
      try {
        const res = await api.get("/products");
        setProducts(res.data);
      } catch (err) {
        setError("Failed to load products. Please check backend connection.");
      } finally {
        setLoading(false);
      }
    }
    fetchProducts();
  }, []);

  const handleAddToCart = (product) => {
    const storedCart = JSON.parse(localStorage.getItem("cart")) || [];
    const existing = storedCart.find((item) => item.id === product.id);

    if (existing) {
      existing.quantity += 1;
    } else {
      storedCart.push({
        ...product,
        quantity: 1,
      });
    }

    localStorage.setItem("cart", JSON.stringify(storedCart));
    window.dispatchEvent(new Event("storage"));

    toast.success(`🛒 ${product.name} added to cart!`, {
      position: "top-right",
      autoClose: 2000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      theme: "colored",
    });
  };

  if (loading) return <p className="loading">Loading products...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="products-page">
      <h1 className="page-title">All Products</h1>
      <div className="product-grid">
        {products.map((p) => (
          <div className="product-card" key={p.id}>
            <img
              src={p.imageUrl || `https://via.placeholder.com/200?text=${p.name}`}
              alt={p.name}
              onError={(e) => (e.target.src = "/placeholder.png")}
            />
            <div className="product-info">
              <h3>{p.name}</h3>
              <p className="price">${p.price.toFixed(2)}</p>
              <button onClick={() => handleAddToCart(p)}>Add to Cart</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
