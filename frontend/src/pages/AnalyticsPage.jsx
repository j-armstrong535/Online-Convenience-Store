import React, { useEffect, useState, useMemo, useCallback } from "react";
import axios from "axios";
import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from "recharts";
import { FaSyncAlt } from "react-icons/fa";
import "../styles/analytics.css";

const ACCOUNT_TYPE_KEY = "accountType";
const DEFAULT_ACCOUNT_TYPE = "CustomerAccount";

export default function AnalyticsPage() {
  const [accountType, setAccountType] = useState(DEFAULT_ACCOUNT_TYPE);
  const [summary, setSummary] = useState(null);
  const [weeklyRevenue, setWeeklyRevenue] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [refreshing, setRefreshing] = useState(false); // new state for refresh animation

  // Check account type
  useEffect(() => {
    const stored = localStorage.getItem(ACCOUNT_TYPE_KEY);
    if (stored) {
      setAccountType(stored);
    }
  }, []);

  const isStoreAccount = useMemo(
    () => accountType === "StoreAccount" || accountType === "store_manager" || accountType === "store_admin",
    [accountType]
  );

  // Function to fetch analytics
  const fetchAnalytics = useCallback(async () => {
    if (!isStoreAccount) return;
    try {
      setRefreshing(true);
      setError(null);
      const [summaryRes, revenueRes] = await Promise.all([
        axios.get("http://localhost:8081/api/analytics/summary"),
        axios.get("http://localhost:8081/api/analytics/revenue"),
      ]);

      setSummary(summaryRes.data);
      const chartData = Object.entries(revenueRes.data).map(([date, total]) => ({
        date,
        revenue: total,
      }));
      setWeeklyRevenue(chartData);
    } catch (err) {
      console.error("Error fetching analytics:", err);
      setError("Failed to load analytics data. Please try again later.");
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  }, [isStoreAccount]);

  // Initial fetch
  useEffect(() => {
    fetchAnalytics();
  }, [fetchAnalytics]);

  if (!isStoreAccount) {
    return (
      <div className="analytics-container restricted">
        <h1>Analytics Access Restricted</h1>
        <p>
          You are currently signed in as a <strong>{accountType}</strong>.  
          Only Store Managers or Admins can view performance analytics.
        </p>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="analytics-container loading">
        <h2>Loading analytics data...</h2>
      </div>
    );
  }

  if (error) {
    return (
      <div className="analytics-container error">
        <h2>{error}</h2>
      </div>
    );
  }

  return (
    <div className="analytics-container">
      <header className="analytics-header">
        <div className="analytics-header-content">
          <div>
            <h1>Store Analytics Dashboard</h1>
            <p>Monitor key performance metrics and recent revenue trends.</p>
          </div>
          <button
            className={`refresh-button ${refreshing ? "spinning" : ""}`}
            onClick={fetchAnalytics}
            disabled={refreshing}
          >
            <FaSyncAlt className = "refresh-icon" />
            {refreshing ? "Refreshing..." : "Refresh Data"}
          </button>
        </div>
      </header>

      <section className="analytics-grid">
        <div className="analytics-card">
          <h2>Daily Revenue</h2>
          <p className="analytics-value">${summary.dailyRevenue?.toLocaleString()}</p>
          <p className="analytics-subtext">Revenue generated today</p>
        </div>

        <div className="analytics-card">
          <h2>Top Category</h2>
          <p className="analytics-value">{summary.topCategory}</p>
          <p className="analytics-subtext">Most purchased category</p>
        </div>

        <div className="analytics-card">
          <h2>Repeat Customers</h2>
          <p className="analytics-value">{summary.repeatCustomers}%</p>
          <p className="analytics-subtext">Returning customer rate</p>
        </div>

        <div className="analytics-card warning">
          <h2>Low Inventory</h2>
          <p className="analytics-value">{summary.lowInventory}</p>
          <p className="analytics-subtext">Items below safety stock</p>
        </div>
      </section>

      <section className="analytics-chart">
        <h2>Weekly Revenue Overview</h2>
        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={weeklyRevenue} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="revenue" stroke="#00bcd4" strokeWidth={3} dot={{ r: 4 }} />
          </LineChart>
        </ResponsiveContainer>
      </section>
    </div>
  );
}
