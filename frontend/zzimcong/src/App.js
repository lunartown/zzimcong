import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import Login from "./pages/Login";
import ProductDetail from "./pages/ProductDetail";
import Signup from "./pages/Signup";
import UserProfile from "./pages/UserProfile";
import { AuthProvider } from "./context/AuthContext";

// 네비게이션 컴포넌트 (필요한 경우)
// import Navigation from "./components/Navigation";

const App = () => {
  return (
    <AuthProvider>
      <Router>
        <div className="app">
          {/* <Navigation /> 네비게이션 바 */}
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/admin" element={<Admin />} />
            <Route path="/cart" element={<Cart />} />
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/login" element={<Login />} />
            <Route path="/product/:id" element={<ProductDetail />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/profile" element={<UserProfile />} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
};

export default App;
