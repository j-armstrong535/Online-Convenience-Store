import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import api from "../services/api";
import { addProductToCart } from "../services/cart";
import { getProductImageUrl, handleImageError } from "../utils/imageUtils";
import "../styles/main.css";


export default function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [sortOption, setSortOption] = useState("");

  useEffect(() => {
    async function fetchProducts() {
      try {
        const res = await api.get("/products");
        setProducts(res.data);
        setFilteredProducts(res.data);
      } catch (err) {
        setError("Failed to load products. Please check backend connection.");
      } finally {
        setLoading(false);
      }
    }
    fetchProducts();
  }, []);

  // Filter + Sort logic
  useEffect(() => {
    let updated = [...products];

    // Filter by search
    if (searchQuery) {
      updated = updated.filter((p) =>
        p.name.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // Sort by selected option
    if (sortOption === "priceLowHigh") updated.sort((a, b) => a.price - b.price);
    else if (sortOption === "priceHighLow") updated.sort((a, b) => b.price - a.price);
    else if (sortOption === "nameAZ") updated.sort((a, b) => a.name.localeCompare(b.name));
    else if (sortOption === "nameZA") updated.sort((a, b) => b.name.localeCompare(a.name));

    setFilteredProducts(updated);
  }, [searchQuery, sortOption, products]);

  const handleAddToCart = async (product) => {
    try {
      await addProductToCart(product);
      toast.success(`🛒 ${product.name} added to cart!`, {
        position: "top-right",
        autoClose: 2000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        theme: "colored",
      });
    } catch (error) {
      console.error("Failed to add product to cart", error);
      toast.error("Failed to add product to cart. Please try again.");
    }
  };

  if (loading) return <p className="loading">Loading products...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="products-page">
      <h1 className="page-title">All Products</h1>

      {/* Search + Sort Controls */}
      <div className="product-controls">
        <input
          type="text"
          placeholder="Search products..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="product-search"
        />

        <select
          value={sortOption}
          onChange={(e) => setSortOption(e.target.value)}
          className="product-sort"
        >
          <option value="">Sort by</option>
          <option value="priceLowHigh">Price: Low to High</option>
          <option value="priceHighLow">Price: High to Low</option>
          <option value="nameAZ">Name: A to Z</option>
          <option value="nameZA">Name: Z to A</option>
        </select>
      </div>

      {/* Product Grid */}
      <div className="product-grid">
        {filteredProducts.length > 0 ? (
          filteredProducts.map((p) => (
            <div className="product-card" key={p.id}>
              <img
                src={getProductImageUrl(p)}
                alt={p.name}
                onError={(e) => handleImageError(e, p)}
              />
              <div className="product-info">
                <h3>{p.name}</h3>
                <p className="price">${p.price.toFixed(2)}</p>
                <button onClick={() => handleAddToCart(p)}>Add to Cart</button>
              </div>
            </div>
          ))
        ) : (
          <p className="no-results">No products found.</p>
        )}
      </div>
    </div>
  );
}
