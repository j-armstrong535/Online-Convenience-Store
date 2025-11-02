import React, { useEffect, useState } from "react";
import "../styles/main.css";

const ACCOUNT_TYPE_KEY = "accountType";
const DEFAULT_ACCOUNT_TYPE = "CustomerAccount";

export default function ProfilePage() {
  const [accountType, setAccountType] = useState(DEFAULT_ACCOUNT_TYPE);

  useEffect(() => {
    const stored = localStorage.getItem(ACCOUNT_TYPE_KEY);
    if (stored) {
      setAccountType(stored);
    }
  }, []);

  return (
    <div className="page-container">
      <header className="page-header">
        <h1>Profile</h1>
        <p className="page-subtitle">
          Manage account preferences, contact information, and delivery details.
        </p>
      </header>

      <section className="info-card">
        <h2>Account Overview</h2>
        <p>
          <strong>Account Type:</strong> {accountType}
        </p>
        <p>
          Update your account type through the account settings workflow once authentication is
          connected. For now, we’ll use this to customise navigation and analytics access.
        </p>
      </section>

      <section className="info-card">
        <h2>Contact Details</h2>
        <p>
          Store your favourite delivery addresses, contact numbers, and payment preferences here.
          This section will integrate with the backend account services.
        </p>
      </section>
    </div>
  );
}
