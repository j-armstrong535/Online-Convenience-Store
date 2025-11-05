import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/main.css";
import "../styles/ProductCategory.css";

const FEATURED_CATEGORIES = [
  {
    name: "Groceries",
    description: "Fresh produce, pantry staples, and everyday essentials.",
    accent: "🥦",
  },
  {
    name: "Candy",
    description: "Sweet treats, chocolates, and confectionery delights.",
    accent: "🍬",
  },
  {
    name: "Snacks",
    description: "Sweet, savoury, and everything in between for quick bites.",
    accent: "🍿",
  },
  {
    name: "Drinks",
    description: "Energising coffees, teas, juices, and chilled beverages.",
    accent: "🥤",
  },
  {
    name: "Personal Care",
    description: "Health, hygiene, and self-care favourites you rely on.",
    accent: "🧴",
  },
  {
    name: "Alcohol",
    description: "Wine, beer, spirits, and premium alcoholic beverages.",
    accent: "🍷",
  },
  {
    name: "Frozen Food",
    description: "Frozen meals, ice cream, and convenient frozen items.",
    accent: "🧊",
  },
  {
    name: "Desserts",
    description: "Cakes, pastries, ice cream, and sweet indulgences.",
    accent: "🍰",
  },
  {
    name: "Deli",
    description: "Fresh deli meats, cheeses, and prepared foods.",
    accent: "🧀",
  },
  {
    name: "Condiments & Spices",
    description: "Sauces, seasonings, and flavor enhancers for your kitchen.",
    accent: "🧂",
  },
  {
    name: "Household & Cleaning",
    description: "Cleaning supplies, storage, and home maintenance products.",
    accent: "🏠",
  },
  {
    name: "Pet Care",
    description: "Food, treats, and supplies for your furry friends.",
    accent: "🐾",
  },
];

export default function CategoriesPage() {
  const navigate = useNavigate();

  const handleCategoryClick = (categoryName) => {
    navigate(`/categories/${categoryName.toLowerCase()}`);
  };

  return (
    <div className="page-container">
      <header className="page-header">
        <h1>Categories</h1>
        <p className="page-subtitle">
          Find what you need fast with curated collections, tailored to convenience shopping.
        </p>
      </header>

      <section className="category-grid-detailed">
        {FEATURED_CATEGORIES.map((category) => (
          <article
            key={category.name}
            className="category-card-detailed clickable"
            onClick={() => handleCategoryClick(category.name)}
            role="button"
            tabIndex={0}
            onKeyPress={(e) => {
              if (e.key === 'Enter' || e.key === ' ') {
                handleCategoryClick(category.name);
              }
            }}
          >
            <div className="category-card-accent">{category.accent}</div>
            <div>
              <h2>{category.name}</h2>
              <p>{category.description}</p>
            </div>
          </article>
        ))}
      </section>
    </div>
  );
}
