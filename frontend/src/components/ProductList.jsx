import React, { useEffect, useState } from "react";
import api from "../services/api";

export default function ProductList() {
  const [products, setProducts] = useState([]);

  const fetchProducts = async () => {
    const res = await api.get("/products");
    setProducts(res.data);
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  return (
    <div className="product-list">
      <h2>Available Products</h2>
      <div className="grid">
        {products.map((p) => (
          <div className="card" key={p.id}>
            <h3>{p.name}</h3>
            <p>{p.category}</p>
            <p>${p.price.toFixed(2)}</p>
            <p>{p.stock} in stock</p>
          </div>
        ))}
      </div>
    </div>
  );
}
