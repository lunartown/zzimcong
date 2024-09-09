import React, { useState, useEffect } from "react";
import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../services/api";

const CategoryManagement = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [editingCategory, setEditingCategory] = useState(null);

  useEffect(() => {
    fetchCategories();
  }, []);

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

  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const categoryData = Object.fromEntries(formData.entries());

    try {
      if (editingCategory) {
        await updateCategory(editingCategory.id, categoryData);
      } else {
        await createCategory(categoryData);
      }
      fetchCategories();
      setEditingCategory(null);
    } catch (err) {
      setError("Failed to save category.");
    }
  };

  const handleEdit = (category) => {
    setEditingCategory(category);
  };

  const handleDelete = async (categoryId) => {
    try {
      await deleteCategory(categoryId);
      fetchCategories();
    } catch (err) {
      setError("Failed to delete category.");
    }
  };

  if (loading) return <div>Loading categories...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div>
      <h3 className="text-xl font-semibold mb-4">Category Management</h3>
      <form onSubmit={handleSubmit} className="mb-8 space-y-4">
        <input
          type="text"
          name="name"
          placeholder="Category Name"
          defaultValue={editingCategory?.name || ""}
          required
          className="w-full px-3 py-2 border rounded"
        />
        <select
          name="parentCategory"
          defaultValue={editingCategory?.parentCategory?.id || ""}
          className="w-full px-3 py-2 border rounded"
        >
          <option value="">No Parent Category</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          {editingCategory ? "Update Category" : "Add Category"}
        </button>
      </form>

      <h4 className="text-lg font-semibold mb-2">Category List</h4>
      <ul className="space-y-4">
        {categories.map((category) => (
          <li key={category.id} className="border p-4 rounded">
            <h5 className="font-semibold">{category.name}</h5>
            {category.parentCategory && (
              <p>Parent: {category.parentCategory.name}</p>
            )}
            <div className="mt-2">
              <button
                onClick={() => handleEdit(category)}
                className="bg-yellow-500 text-white px-2 py-1 rounded mr-2 hover:bg-yellow-600"
              >
                Edit
              </button>
              <button
                onClick={() => handleDelete(category.id)}
                className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
              >
                Delete
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CategoryManagement;
