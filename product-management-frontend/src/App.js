import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navigation from './components/layout/Navigation';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import ProductList from './components/products/ProductList';
import PrivateRoute from './components/layout/PrivateRoute';
import './App.css';

function App() {
  return (
      <Router>
        <div className="App">
          <Navigation />
          <div className="container mt-4">
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/products" element={
                <PrivateRoute>
                  <ProductList />
                </PrivateRoute>
              } />
              <Route path="/" element={<Navigate to="/products" replace />} />
            </Routes>
          </div>
        </div>
      </Router>
  );
}

export default App;