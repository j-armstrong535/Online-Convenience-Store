import React, { useEffect, useState } from "react";
import api from "../services/api";
import { getProductImageUrl, handleImageError } from "../utils/imageUtils";

export default function ProductList() {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("");
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");

  // Fetch products from backend with filters
  const fetchProducts = async (filters = {}) => {
    try {
      const res = await api.get("/products", {
        params: {
          search: filters.search || undefined,
          category: filters.category || undefined,
          minPrice: filters.minPrice || undefined,
          maxPrice: filters.maxPrice || undefined,
        },
      });
      setProducts(res.data);
    } catch (err) {
      console.error("Error fetching products:", err);
      setProducts([]);
    }
  };

  // Fetch all products on load
  useEffect(() => {
    fetchProducts();
  }, []);

  // Handle filter button click
  const handleApply = () => {
    fetchProducts({ search, category, minPrice, maxPrice });
  };

  return (
    <div className="product-list" style={{ padding: "1rem" }}>
      <h2 style={{ textAlign: "center", marginBottom: "1rem" }}>All Products</h2>

      {/* Search & Filters Section */}
      <div
        className="filters"
        style={{
          marginBottom: "1rem",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          flexWrap: "wrap",
          gap: "0.75rem",
          backgroundColor: "#000",
          color: "#FFD100",
          padding: "1rem",
          borderRadius: "10px",
        }}
      >
        {/* Search Bar */}
        <input
          type="text"
          placeholder="Search for a product..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleApply()}
          style={{
            padding: "0.6rem 0.9rem",
            borderRadius: "8px",
            border: "1px solid #FFD100",
            width: "250px",
            outline: "none",
            backgroundColor: "#fff",
            color: "#000",
          }}
        />

        {/* Category Filter */}
        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          style={{
            padding: "0.6rem",
            borderRadius: "8px",
            border: "1px solid #FFD100",
            backgroundColor: "#fff",
            color: "#000",
          }}
        >
          <option value="">All Categories</option>
          <option value="Food">Food</option>
          <option value="Drinks">Drinks</option>
          <option value="Electronics">Electronics</option>
        </select>

        {/* Min/Max Price */}
        <input
          type="number"
          placeholder="Min Price"
          value={minPrice}
          onChange={(e) => setMinPrice(e.target.value)}
          style={{
            padding: "0.6rem",
            borderRadius: "8px",
            border: "1px solid #FFD100",
            width: "100px",
          }}
        />

        <input
          type="number"
          placeholder="Max Price"
          value={maxPrice}
          onChange={(e) => setMaxPrice(e.target.value)}
          style={{
            padding: "0.6rem",
            borderRadius: "8px",
            border: "1px solid #FFD100",
            width: "100px",
          }}
        />

        {/* Apply Button */}
        <button
          onClick={handleApply}
          style={{
            padding: "0.6rem 1.2rem",
            borderRadius: "8px",
            backgroundColor: "#FFD100",
            border: "none",
            color: "#000",
            fontWeight: "600",
            cursor: "pointer",
            transition: "0.3s",
          }}
        >
          Apply Filters
        </button>
      </div>

      {/* Product Grid */}
      <div
        className="grid"
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fill, minmax(180px, 1fr))",
          gap: "1rem",
        }}
      >
        {products.length === 0 ? (
          <p style={{ textAlign: "center" }}>No products found.</p>
        ) : (
          products.map((p) => (
            <div
              className="card"
              key={p._id}
              style={{
                border: "1px solid #ccc",
                borderRadius: "8px",
                padding: "0.8rem",
                textAlign: "center",
                backgroundColor: "#fff",
              }}
            >
              <img
                src={getProductImageUrl(p)}
                alt={p.name}
                style={{
                  width: "100%",
                  height: "120px",
                  objectFit: "cover",
                  marginBottom: "0.5rem",
                  borderRadius: "6px",
                }}
                onError={(e) => handleImageError(e, p)}
              />
              <h3 style={{ color: "#000" }}>{p.name}</h3>
              <p style={{ color: "#555" }}>{p.category}</p>
              <p style={{ color: "#D80027", fontWeight: "bold" }}>
                ${p.price.toFixed(2)}
              </p>
              <p style={{ color: p.inStock ? "#333" : "#D80027" }}>
                {p.inStock ? `${p.inStock} in stock` : "Out of stock"}
              </p>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
