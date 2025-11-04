import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import api from "../services/api";
import { addProductToCart } from "../services/cart";
import { getProductImageUrl, handleImageError } from "../utils/imageUtils";
//import "../styles/main.css";
import "../styles/ProductCategory.css";

export default function CategoryProductsPage() {
    const { categoryName } = useParams();
    const navigate = useNavigate();
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        async function fetchProducts() {
            try {
                setLoading(true);
                const res = await api.get("/products");

                // Filter products by category
                const filteredProducts = res.data.filter(
                    (product) =>
                        product.category?.toLowerCase() === categoryName?.toLowerCase()
                );

                setProducts(filteredProducts);
            } catch (err) {
                setError("Failed to load products. Please check backend connection.");
                console.error("Error fetching products:", err);
            } finally {
                setLoading(false);
            }
        }

        if (categoryName) {
            fetchProducts();
        }
    }, [categoryName]);

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

    const handleBackToCategories = () => {
        navigate("/categories");
    };

    if (loading) return <p className="loading">Loading products...</p>;
    if (error) return <p className="error">{error}</p>;

    return (
        <div className="products-page">
            <div className="category-header">
                <button onClick={handleBackToCategories} className="back-button">
                    ← Back to Categories
                </button>
                <h1 className="page-title">
                    {categoryName ? categoryName.charAt(0).toUpperCase() + categoryName.slice(1) : "Category"} Products
                </h1>
                <p className="page-subtitle">
                    {products.length} {products.length === 1 ? "product" : "products"} found
                </p>
            </div>

            {products.length === 0 ? (
                <div className="no-products">
                    <p>No products found in this category.</p>
                    <button onClick={handleBackToCategories} className="back-button">
                        Browse All Categories
                    </button>
                </div>
            ) : (
                <div className="product-grid">
                    {products.map((p) => (
                        <div className="product-card" key={p.id}>
                            <img
                                src={getProductImageUrl(p)}
                                alt={p.name}
                                onError={(e) => handleImageError(e, p)}
                            />
                            <div className="product-info">
                                <h3>{p.name}</h3>
                                <p className="category-badge">{p.category}</p>
                                <p className="price">${p.price.toFixed(2)}</p>
                                <p className="stock-info">{p.stock} in stock</p>
                                <button onClick={() => handleAddToCart(p)}>Add to Cart</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}
