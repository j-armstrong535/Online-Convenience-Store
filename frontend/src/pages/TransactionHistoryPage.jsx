import React from "react";
import "../styles/main.css";

const SAMPLE_TRANSACTIONS = [
  {
    id: "INV-1042",
    date: "12 Feb 2025",
    total: 68.4,
    status: "Delivered",
    items: 6,
  },
  {
    id: "INV-1039",
    date: "08 Feb 2025",
    total: 32.1,
    status: "Dispatched",
    items: 3,
  },
  {
    id: "INV-1032",
    date: "30 Jan 2025",
    total: 120.95,
    status: "Delivered",
    items: 11,
  },
];

export default function TransactionHistoryPage() {
  return (
    <div className="page-container">
      <header className="page-header">
        <h1>Transaction History</h1>
        <p className="page-subtitle">
          A snapshot of recent purchases. Live transaction data will appear once order services
          are wired up.
        </p>
      </header>

      <div className="transaction-list">
        {SAMPLE_TRANSACTIONS.map((transaction) => (
          <article key={transaction.id} className="transaction-card">
            <div>
              <h2>{transaction.id}</h2>
              <p className="transaction-meta">
                <span>{transaction.date}</span>
                <span>{transaction.items} items</span>
              </p>
            </div>
            <div className="transaction-summary">
              <span className={`status-chip status-${transaction.status.toLowerCase()}`}>
                {transaction.status}
              </span>
              <span className="transaction-total">${transaction.total.toFixed(2)}</span>
            </div>
          </article>
        ))}
      </div>
    </div>
  );
}
