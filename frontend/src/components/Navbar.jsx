import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { removeToken } from '../api/axios';

export default function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    removeToken();
    // Delegates to /api/auth/logout which has VB-013 Open Redirect
    window.location.href = '/api/auth/logout?next=/login';
  };

  return (
    <nav style={styles.nav}>
      <span style={styles.brand}>🏦 VulnBank</span>
      <div style={styles.links}>
        <Link to="/dashboard" style={styles.link}>Dashboard</Link>
        <Link to="/accounts" style={styles.link}>Accounts</Link>
        <Link to="/transfer" style={styles.link}>Transfer</Link>
        <Link to="/search" style={styles.link}>Search</Link>
        <button onClick={handleLogout} style={styles.logout}>Logout</button>
      </div>
    </nav>
  );
}

const styles = {
  nav: {
    background: '#16213e',
    padding: '12px 24px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    borderBottom: '2px solid #e94560',
  },
  brand: { color: '#e94560', fontWeight: 'bold', fontSize: '1.2rem', fontFamily: 'Arial, sans-serif' },
  links: { display: 'flex', gap: '16px', alignItems: 'center' },
  link: { color: '#a8a8b3', textDecoration: 'none', fontSize: '14px' },
  logout: {
    background: 'transparent',
    color: '#e94560',
    border: '1px solid #e94560',
    borderRadius: '4px',
    padding: '4px 12px',
    cursor: 'pointer',
    fontSize: '14px',
  },
};
