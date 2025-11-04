import React from "react";
import { Navigate } from "react-router-dom";
import { toast } from "react-toastify";

/**
 * Protects routes that require a specific account type or login status.
 *
 * @param {ReactNode} children  - The page component to render if allowed.
 * @param {string[]} allowedTypes - Array of allowed account types (e.g., ["StoreAccount"]).
 * @param {boolean} requireLogin - Whether login is required at all.
 */
export default function ProtectedRoute({
  children,
  allowedTypes = [],
  requireLogin = true,
}) {
  const user = localStorage.getItem("user");
  const accountType = localStorage.getItem("accountType");

  // 🧩 Not logged in
  if (requireLogin && !user) {
    toast.error("Please log in to access this page.");
    return <Navigate to="/login" replace />;
  }

  // 🚫 Wrong account type
  if (allowedTypes.length > 0 && !allowedTypes.includes(accountType)) {
    toast.error("Access denied: store accounts only.");
    return <Navigate to="/" replace />;
  }

  // ✅ Passed all checks
  return children;
}
