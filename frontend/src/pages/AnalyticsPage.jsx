import React, { useEffect, useMemo, useState } from "react";
import "../styles/main.css";

const ACCOUNT_TYPE_KEY = "accountType";
const DEFAULT_ACCOUNT_TYPE = "CustomerAccount";

export default function AnalyticsPage() {
  const [accountType, setAccountType] = useState(DEFAULT_ACCOUNT_TYPE);

  useEffect(() => {
    const stored = localStorage.getItem(ACCOUNT_TYPE_KEY);
    if (stored) {
      setAccountType(stored);
    }
  }, []);

  const isStoreAccount = useMemo(
    () => accountType === "StoreAccount",
    [accountType]
  );

  if (!isStoreAccount) {
    return (
      <div className="page-container">
        <header className="page-header">
          <h1>Analytics</h1>
          <p className="page-subtitle">
            Analytics is exclusive to store managers. Switch to a StoreAccount to unlock sales
            insights and performance dashboards.
          </p>
        </header>

        <section className="info-card">
          <h2>Access Restricted</h2>
          <p>
            You are currently signed in as <strong>{accountType}</strong>. Once authentication is
            in place, users tagged as <strong>StoreAccount</strong> will have access to
            comprehensive analytics across inventory, sales velocity, and profitability indicators.
          </p>
        </section>
      </div>
    );
  }

  return (
    <div className="page-container">
      <header className="page-header">
        <h1>Analytics</h1>
        <p className="page-subtitle">
          Track store performance at a glance. These sample cards demonstrate the layout ahead of
          live integrations.
        </p>
      </header>

      <section className="analytics-grid">
        <article className="analytics-card">
          <h2>Daily Revenue</h2>
          <p className="analytics-value">$4,870</p>
          <p className="analytics-trend positive">▲ 12% vs yesterday</p>
        </article>
        <article className="analytics-card">
          <h2>Top Category</h2>
          <p className="analytics-value">Snacks</p>
          <p className="analytics-trend neutral">Consistent week-on-week</p>
        </article>
        <article className="analytics-card">
          <h2>Low Inventory Alerts</h2>
          <p className="analytics-value">8 SKUs</p>
          <p className="analytics-trend warning">Action recommended today</p>
        </article>
        <article className="analytics-card">
          <h2>Repeat Customers</h2>
          <p className="analytics-value">63%</p>
          <p className="analytics-trend positive">▲ 5% month-on-month</p>
        </article>
      </section>
    </div>
  );
}
