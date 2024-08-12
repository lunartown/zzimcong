import React, { useState, useEffect, useCallback } from "react";
import { getProducts } from "../services/api";
import ProductList from "../components/ProductList";
import CategoryList from "../components/CategoryList";
import Modal from "../components/Modal";
import Login from "./Login";
import Signup from "./Signup";

const Home = () => {
  const [products, setProducts] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [category, setCategory] = useState(null);
  const [isLoginOpen, setIsLoginOpen] = useState(false);
  const [isSignupOpen, setIsSignupOpen] = useState(false);

  const fetchProducts = useCallback(async () => {
    try {
      const response = await getProducts(currentPage, 10, "", category);
      setProducts(response);
    } catch (error) {
      console.error("Failed to fetch products:", error);
    }
  }, [currentPage, category]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  return (
    <div className="home">
      <h1>환영합니다</h1>
      <button onClick={() => setIsLoginOpen(true)}>로그인</button>
      <button onClick={() => setIsSignupOpen(true)}>회원가입</button>

      <Modal isOpen={isLoginOpen} onClose={() => setIsLoginOpen(false)}>
        <Login />
      </Modal>

      <Modal isOpen={isSignupOpen} onClose={() => setIsSignupOpen(false)}>
        <Signup />
      </Modal>
      <h1>Welcome to Our Store</h1>
      <CategoryList onSelectCategory={setCategory} />
      <ProductList products={products} />
      <button
        onClick={() => setCurrentPage((prev) => Math.max(0, prev - 1))}
        disabled={currentPage === 0}
      >
        Previous Page
      </button>
      <button onClick={() => setCurrentPage((prev) => prev + 1)}>
        Next Page
      </button>
    </div>
  );
};

export default Home;
