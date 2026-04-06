import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import AccountsPage from './pages/AccountsPage';
import TransferPage from './pages/TransferPage';
import SearchPage from './pages/SearchPage';
import Navbar from './components/Navbar';
import { getToken } from './api/axios';

function PrivateRoute({ children }) {
  return getToken() ? children : <Navigate to="/login" replace />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/*"
          element={
            <PrivateRoute>
              <>
                <Navbar />
                <Routes>
                  <Route path="/dashboard" element={<DashboardPage />} />
                  <Route path="/accounts" element={<AccountsPage />} />
                  <Route path="/transfer" element={<TransferPage />} />
                  <Route path="/search" element={<SearchPage />} />
                  <Route path="*" element={<Navigate to="/dashboard" replace />} />
                </Routes>
              </>
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}
