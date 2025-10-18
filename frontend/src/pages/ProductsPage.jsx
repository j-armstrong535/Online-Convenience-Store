import React, { useState } from "react";
import ProductList from "../components/ProductList";
import ProductForm from "../components/ProductForm";

export default function ProductsPage() {
  const [refresh, setRefresh] = useState(false);
  return (
    <div>
      <ProductForm onAdd={() => setRefresh(!refresh)} />
      <ProductList key={refresh} />
    </div>
  );
}
