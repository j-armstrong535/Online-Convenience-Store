// import React, { useEffect, useState } from "react";
// import api from "../services/api";
// import "../styles/main.css";

// export default function ProductsPage() {
//   const [products, setProducts] = useState([]);
//   const [loading, setLoading] = useState(true); // Loading state
//   const [error, setError] = useState(null);     // Error state

//   useEffect(() => {
//     api.get("/products")
//       .then(res => {
//         setProducts(res.data);
//         setLoading(false);
//       })
//       .catch(err => {
//         console.error("Error fetching products:", err);
//         setError("Failed to load products.");
//         setLoading(false);
//       });
//   }, []);

//   const handleAddToCart = (product) => {
//     const storedCart = JSON.parse(localStorage.getItem("cart")) || [];
//     const existing = storedCart.find(item => item.id === product.id);

//     if (existing) {
//       existing.quantity += 1;
//     } else {
//       storedCart.push({
//         id: product.id,
//         name: product.name,
//         price: product.price,
//         imageUrl: `https://res.cloudinary.com/dtglrc8my/image/upload/${product.id}.jpg`,
//         quantity: 1,
//       });
//     }

//     localStorage.setItem("cart", JSON.stringify(storedCart));
//     alert(`${product.name} added to cart!`);
//   };

//   if (loading) {
//     return <p style={{ textAlign: "center", marginTop: "2rem" }}>Loading products...</p>;
//   }

//   if (error) {
//     return <p style={{ textAlign: "center", marginTop: "2rem", color: "red" }}>{error}</p>;
//   }

//   return (
//     <div className="store-products-page">
//       <h1 className="page-title">All Products</h1>
//       <div className="product-grid-premium">
//         {products.map((p) => (
//           <div className="product-card-premium" key={p.id}>
//             <img
//               src={`https://res.cloudinary.com/dtglrc8my/image/upload/${p.id}.jpg`}
//               alt={p.name}
//               style={{ width: "100%", height: "150px", objectFit: "cover", marginBottom: "0.5rem" }}
//               onError={(e) => { e.target.src = "/placeholder.png"; }}
//             />
//             <h3>{p.name}</h3>
//             <p>${p.price.toFixed(2)}</p>
//             <button onClick={() => handleAddToCart(p)}>Add to Cart</button>
//           </div>
//         ))}
//       </div>
//     </div>
//   );
// }
/////////////////////////////////////////////////////////
// import React, { useState } from "react"; 
// import ProductList from "../components/ProductList"; 
// import ProductForm from "../components/ProductForm"; 

// export default function ProductsPage() { const [refresh, setRefresh] = useState(false); 
//   return ( <div> <ProductForm onAdd={() => setRefresh(!refresh)} /> <ProductList key={refresh} /> </div> ); }

import React, { useEffect, useState } from "react";
import api from "../services/api";
import "../styles/main.css"; // ✅ use a separate CSS file for clarity

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
    alert(`${product.name} added to cart!`);
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
