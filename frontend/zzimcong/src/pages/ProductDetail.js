import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { getProductById } from "../services/api";

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const data = await getProductById(id);
        setProduct(data);
        setLoading(false);
      } catch (err) {
        setError("Failed to load product details.");
        setLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

  if (loading) return <div className="text-center mt-10">Loading...</div>;
  if (error)
    return <div className="text-center mt-10 text-red-500">{error}</div>;
  if (!product)
    return <div className="text-center mt-10">Product not found.</div>;

  return (
    <div className="max-w-2xl mx-auto mt-10">
      <h2 className="text-3xl font-bold mb-5">{product.name}</h2>
      <img
        src={product.image}
        alt={product.name}
        className="w-full h-64 object-cover mb-5"
      />
      <p className="text-xl mb-3">Price: {product.price.toLocaleString()} 원</p>
      {product.sale > 0 && (
        <p className="text-red-500 text-lg mb-3">Sale: {product.sale}% off</p>
      )}
      <p className="mb-3">Available: {product.count} in stock</p>
      <p className="text-gray-700 mb-5">{product.content}</p>
      <button className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600">
        Add to Cart
      </button>
    </div>
  );
};

export default ProductDetail;
