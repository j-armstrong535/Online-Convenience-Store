import React, { useCallback, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { getReceiptById } from "../services/receipt";
import "../styles/receipt.css";

export default function ReceiptPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const [receipt, setReceipt] = useState(location.state?.receipt || null);
  const [paymentSummary, setPaymentSummary] = useState(location.state?.payment || null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const parsedIdFromQuery = useCallback(() => {
    const params = new URLSearchParams(location.search || "");
    return params.get("id");
  }, [location.search]);

  useEffect(() => {
    if (receipt) {
      return;
    }

    const cached = sessionStorage.getItem("latestReceipt");
    if (cached) {
      try {
        const parsed = JSON.parse(cached);
        if (parsed?.receipt) {
          setReceipt(parsed.receipt);
        }
        if (parsed?.payment) {
          setPaymentSummary(parsed.payment);
        }
        return;
      } catch (err) {
        console.error("Failed to parse cached receipt", err);
      }
    }

    const stateReceipt = location.state?.receipt;
    if (stateReceipt) {
      setReceipt(stateReceipt);
      setPaymentSummary(location.state?.payment || null);
      return;
    }

    const receiptId =
      location.state?.receiptId || parsedIdFromQuery();
    if (!receiptId) {
      setError("We couldn't find a recent receipt. Please check your transaction history.");
      return;
    }

    async function fetchReceipt() {
      setLoading(true);
      try {
        const data = await getReceiptById(receiptId);
        setReceipt(data);
      } catch (err) {
        console.error("Failed to load receipt", err);
        setError("Unable to load receipt details. Please try again later.");
      } finally {
        setLoading(false);
      }
    }

    fetchReceipt();
  }, [location.state, parsedIdFromQuery, receipt]);

  const handleViewOrders = () => {
    navigate("/transactions");
  };

  const handleContinueShopping = () => {
    navigate("/");
  };

  if (error) {
    return (
      <div className="receipt-page">
        <h1>Receipt</h1>
        <p className="error">{error}</p>
        <button type="button" onClick={handleContinueShopping} className="receipt-action">
          Continue Shopping
        </button>
      </div>
    );
  }

  if (loading || !receipt) {
    return (
      <div className="receipt-page">
        <h1>Receipt</h1>
        <p className="loading">Loading receipt details...</p>
      </div>
    );
  }

  const items = receipt.items || [];
  const totals = {
    subtotal: receipt.subtotal || 0,
    tax: receipt.tax || 0,
    delivery: receipt.deliverySurcharge || 0,
    total: receipt.totalCost || 0,
  };

  return (
    <div className="receipt-page">
      <header className="receipt-header">
        <h1>Thank you for your purchase!</h1>
        <p>Receipt #{receipt.id}</p>
        <p className="receipt-issued">Issued {new Date(receipt.issuedAt).toLocaleString()}</p>
      </header>

      <section className="receipt-summary">
        <h2>Order Summary</h2>
        <ul>
          {items.map((item, index) => (
            <li key={`${item.productId}-${index}`}>
              <span>{item.productName}</span>
              <span>
                {item.quantity} × ${item.unitPrice.toFixed(2)} = ${item.lineTotal.toFixed(2)}
              </span>
            </li>
          ))}
        </ul>
        <div className="receipt-totals">
          <div>
            <span>Subtotal</span>
            <span>${totals.subtotal.toFixed(2)}</span>
          </div>
          <div>
            <span>Tax</span>
            <span>${totals.tax.toFixed(2)}</span>
          </div>
          {totals.delivery > 0 && (
            <div>
              <span>Delivery</span>
              <span>${totals.delivery.toFixed(2)}</span>
            </div>
          )}
          <div className="receipt-total">
            <span>Total</span>
            <span>${totals.total.toFixed(2)}</span>
          </div>
        </div>
      </section>

      {paymentSummary && (
        <section className="receipt-payment">
          <h2>Payment Details</h2>
          <p>
            Method: <strong>{paymentSummary.method || "Unknown"}</strong>
          </p>
          {paymentSummary.reference && (
            <p>
              Reference: <strong>{paymentSummary.reference}</strong>
            </p>
          )}
          {paymentSummary.processedAt && (
            <p>
              Processed: {new Date(paymentSummary.processedAt).toLocaleString()}
            </p>
          )}
        </section>
      )}

      <div className="receipt-actions">
        <button type="button" className="receipt-action" onClick={handleContinueShopping}>
          Continue Shopping
        </button>
        <button type="button" className="receipt-action" onClick={handleViewOrders}>
          View Transaction History
        </button>
      </div>
    </div>
  );
}
