import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

const Header = () => {
  const { user, logoutUser } = useContext(AuthContext);

  return (
    <header className="bg-blue-500 text-white p-4">
      <div className="container mx-auto flex justify-between items-center">
        <Link to="/" className="text-2xl font-bold">
          찜콩 Store
        </Link>
        <nav>
          <ul className="flex space-x-4">
            <li>
              <Link to="/" className="hover:text-blue-200">
                Home
              </Link>
            </li>
            {user ? (
              <>
                <li>
                  <Link to="/profile" className="hover:text-blue-200">
                    Profile
                  </Link>
                </li>
                <li>
                  <button onClick={logoutUser} className="hover:text-blue-200">
                    Logout
                  </button>
                </li>
              </>
            ) : (
              <>
                <li>
                  <Link to="/login" className="hover:text-blue-200">
                    Login
                  </Link>
                </li>
                <li>
                  <Link to="/signup" className="hover:text-blue-200">
                    Sign Up
                  </Link>
                </li>
              </>
            )}
          </ul>
        </nav>
      </div>
    </header>
  );
};

export default Header;
