import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api, { saveToken } from '../api/axios';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await api.post('/auth/login', { email, password });
      saveToken(response.data.token);
      navigate('/dashboard');
    } catch (err) {
      setError('Invalid credentials. Please try again.');
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1 style={styles.title}>VulnBank</h1>
        <p style={styles.subtitle}>Online Banking Portal</p>
        <form onSubmit={handleLogin} style={styles.form}>
          <div style={styles.field}>
            <label style={styles.label}>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={styles.input}
              placeholder="alice@vulnbank.com"
              required
            />
          </div>
          <div style={styles.field}>
            <label style={styles.label}>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={styles.input}
              placeholder="••••••••"
              required
            />
          </div>
          {error && <p style={styles.error}>{error}</p>}
          <button type="submit" style={styles.button}>Sign In</button>
        </form>
        <p style={styles.hint}>
          Demo: alice@vulnbank.com / admin123 | bob@vulnbank.com / user123
        </p>
      </div>
    </div>
  );
}

const styles = {
  container: {
    minHeight: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    background: '#1a1a2e',
    fontFamily: 'Arial, sans-serif',
  },
  card: {
    background: '#16213e',
    borderRadius: '12px',
    padding: '40px',
    width: '380px',
    boxShadow: '0 8px 32px rgba(0,0,0,0.4)',
  },
  title: { color: '#e94560', margin: 0, fontSize: '2rem' },
  subtitle: { color: '#a8a8b3', marginTop: '4px', marginBottom: '24px' },
  form: { display: 'flex', flexDirection: 'column', gap: '16px' },
  field: { display: 'flex', flexDirection: 'column', gap: '6px' },
  label: { color: '#a8a8b3', fontSize: '14px' },
  input: {
    background: '#0f3460',
    border: '1px solid #e94560',
    borderRadius: '6px',
    padding: '10px 12px',
    color: '#fff',
    fontSize: '14px',
  },
  button: {
    background: '#e94560',
    color: '#fff',
    border: 'none',
    borderRadius: '6px',
    padding: '12px',
    fontSize: '16px',
    cursor: 'pointer',
    fontWeight: 'bold',
  },
  error: { color: '#e94560', margin: 0, fontSize: '14px' },
  hint: { color: '#666', fontSize: '11px', marginTop: '16px', textAlign: 'center' },
};
