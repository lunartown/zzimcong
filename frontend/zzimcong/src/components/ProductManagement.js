import React, { useState, useEffect } from "react";
import {
  getProducts,
  createProduct,
  updateProduct,
  deleteProduct,
} from "../services/api";

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [editingProduct, setEditingProduct] = useState(null);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const data = await getProducts(0, 1000); // Fetch all products
      setProducts(data);
      setLoading(false);
    } catch (err) {
      setError("Failed to load products.");
      setLoading(false);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const productData = Object.fromEntries(formData.entries());

    try {
      if (editingProduct) {
        await updateProduct(editingProduct.id, productData);
      } else {
        await createProduct(productData);
      }
      fetchProducts();
      setEditingProduct(null);
    } catch (err) {
      setError("Failed to save product.");
    }
  };

  const handleEdit = (product) => {
    setEditingProduct(product);
  };

  const handleDelete = async (productId) => {
    try {
      await deleteProduct(productId);
      fetchProducts();
    } catch (err) {
      setError("Failed to delete product.");
    }
  };

  if (loading) return <div>Loading products...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div>
      <h3 className="text-xl font-semibold mb-4">Product Management</h3>
      <form onSubmit={handleSubmit} className="mb-8 space-y-4">
        <input
          type="text"
          name="name"
          placeholder="Product Name"
          defaultValue={editingProduct?.name || ""}
          required
          className="w-full px-3 py-2 border rounded"
        />
        <input
          type="number"
          name="price"
          placeholder="Price"
          defaultValue={editingProduct?.price || ""}
          required
          className="w-full px-3 py-2 border rounded"
        />
        <input
          type="number"
          name="sale"
          placeholder="Sale Percentage"
          defaultValue={editingProduct?.sale || ""}
          className="w-full px-3 py-2 border rounded"
        />
        <textarea
          name="content"
          placeholder="Product Description"
          defaultValue={editingProduct?.content || ""}
          className="w-full px-3 py-2 border rounded"
        ></textarea>
        <input
          type="number"
          name="count"
          placeholder="Stock Count"
          defaultValue={editingProduct?.count || ""}
          required
          className="w-full px-3 py-2 border rounded"
        />
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          {editingProduct ? "Update Product" : "Add Product"}
        </button>
      </form>

      <h4 className="text-lg font-semibold mb-2">Product List</h4>
      <ul className="space-y-4">
        {products.map((product) => (
          <li key={product.id} className="border p-4 rounded">
            <h5 className="font-semibold">{product.name}</h5>
            <p>Price: {product.price} 원</p>
            <p>Stock: {product.count}</p>
            <div className="mt-2">
              <button
                onClick={() => handleEdit(product)}
                className="bg-yellow-500 text-white px-2 py-1 rounded mr-2 hover:bg-yellow-600"
              >
                Edit
              </button>
              <button
                onClick={() => handleDelete(product.id)}
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

export default ProductManagement;
