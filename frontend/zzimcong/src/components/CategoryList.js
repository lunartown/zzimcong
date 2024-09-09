import React, { useState, useEffect } from "react";
import { getCategories } from "../services/api";

const CategoryList = React.memo(({ onSelectCategory }) => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await getCategories();
        setCategories(data);
        setLoading(false);
      } catch (err) {
        setError("Failed to load categories.");
        setLoading(false);
      }
    };
    fetchCategories();
  }, []);

  if (loading) return <div>Loading categories...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div className="mb-6">
      <h3 className="text-lg font-semibold mb-2">Categories</h3>
      <ul className="space-y-2">
        <li>
          <button
            onClick={() => onSelectCategory(null)}
            className="text-blue-500 hover:underline"
          >
            All Products
          </button>
        </li>
        {categories.map((category) => (
          <li key={category.id}>
            <button
              onClick={() => onSelectCategory(category.id)}
              className="text-blue-500 hover:underline"
            >
              {category.name}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
});

export default CategoryList;
