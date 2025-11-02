import React from "react";
import "../styles/main.css";

const FEATURED_CATEGORIES = [
  {
    name: "Groceries",
    description: "Fresh produce, pantry staples, and everyday essentials.",
    accent: "🥦",
  },
  {
    name: "Beverages",
    description: "Energising coffees, teas, juices, and chilled drinks.",
    accent: "🥤",
  },
  {
    name: "Snacks",
    description: "Sweet, savoury, and everything in between for quick bites.",
    accent: "🍿",
  },
  {
    name: "Personal Care",
    description: "Health, hygiene, and self-care favourites you rely on.",
    accent: "🧴",
  },
  {
    name: "Household",
    description: "Cleaning, storage, and home helpers to keep things running.",
    accent: "🏠",
  },
  {
    name: "Express Deals",
    description: "Limited offers on bestsellers—updated throughout the day.",
    accent: "⚡",
  },
];

export default function CategoriesPage() {
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
          <article key={category.name} className="category-card-detailed">
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
