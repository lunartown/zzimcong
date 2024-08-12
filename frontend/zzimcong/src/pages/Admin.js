import React, { useState, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import ProductManagement from "../components/ProductManagement";
import CategoryManagement from "../components/CategoryManagement";

const Admin = () => {
  const { user } = useContext(AuthContext);
  const [activeTab, setActiveTab] = useState("products");

  // Check if the user has admin privileges
  if (!user || user.role !== "ADMIN") {
    return (
      <div className="text-center mt-10">
        You don't have permission to access this page.
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto mt-10">
      <h2 className="text-2xl font-bold mb-5">Admin Dashboard</h2>
      <div className="mb-4">
        <button
          onClick={() => setActiveTab("products")}
          className={`mr-4 px-4 py-2 rounded ${
            activeTab === "products" ? "bg-blue-500 text-white" : "bg-gray-200"
          }`}
        >
          Manage Products
        </button>
        <button
          onClick={() => setActiveTab("categories")}
          className={`px-4 py-2 rounded ${
            activeTab === "categories"
              ? "bg-blue-500 text-white"
              : "bg-gray-200"
          }`}
        >
          Manage Categories
        </button>
      </div>
      {activeTab === "products" ? (
        <ProductManagement />
      ) : (
        <CategoryManagement />
      )}
    </div>
  );
};

export default Admin;
