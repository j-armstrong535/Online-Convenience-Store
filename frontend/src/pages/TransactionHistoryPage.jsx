import React, { useEffect, useState } from "react";
import { listReceipts } from "../services/receipt";
import { getCustomerAccountId } from "../services/user";
import "../styles/main.css";
import "../styles/Trans.css";

export default function TransactionHistoryPage() {
  const [receipts, setReceipts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const accountId = getCustomerAccountId();
    if (!accountId) {
      setError("Sign in or complete a checkout to view your transaction history.");
      setLoading(false);
      return;
    }

    async function fetchReceipts() {
      setLoading(true);
      try {
        const data = await listReceipts(accountId);
        setReceipts(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error("Failed to load receipts", err);
        setError("Unable to load receipts. Please try again later.");
      } finally {
        setLoading(false);
      }
    }

    fetchReceipts();
  }, []);

  const renderContent = () => {
    if (loading) {
      return <p className="loading">Loading your receipts...</p>;
    }

    if (error) {
      return <p className="error">{error}</p>;
    }

    if (receipts.length === 0) {
      return <p className="empty">No receipts found yet. Complete a checkout to see it here.</p>;
    }

    return receipts.map((receipt) => (
      <article key={receipt.id} className="transaction-card">
        <div>
          <h2>{receipt.id}</h2>
          <p className="transaction-meta">
            <span>{new Date(receipt.issuedAt).toLocaleDateString()}</span>
            <span>{(receipt.items || []).length} items</span>
          </p>
        </div>
        <div className="transaction-summary">
          <span className="transaction-total">${receipt.totalCost?.toFixed(2)}</span>
        </div>
      </article>
    ));
  };

  return (
    <div className="page-container">
      <header className="page-header">
        <h1>Transaction History</h1>
        <p className="page-subtitle">
          Review your recent purchases and access receipts any time.
        </p>
      </header>

      <div className="transaction-list">{renderContent()}</div>
    </div>
  );
}
