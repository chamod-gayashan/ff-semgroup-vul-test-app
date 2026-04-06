import React, { useEffect, useState } from 'react';
import api from '../api/axios';

export default function DashboardPage() {
  const [user, setUser] = useState(null);
  const [accounts, setAccounts] = useState([]);

  useEffect(() => {
    // Load the first user as demo current user
    api.get('/users/1').then((res) => setUser(res.data)).catch(() => {
      setUser({
        fullName: 'Bob User',
        email: 'bob@vulnbank.com',
        role: 'ROLE_USER',
        // bio contains HTML to demonstrate the XSS vulnerability
        bio: '<b>Welcome back!</b> Your last login was <i>today</i>.',
      });
    });
    api.get('/accounts').then((res) => setAccounts(res.data)).catch(() => {});
  }, []);

  // VULN-ID: VB-016 | CWE-79 | Severity: HIGH
  // XSS via dangerouslySetInnerHTML: user.bio is rendered without sanitization,
  // allowing stored XSS if an attacker controls the bio field.
  // SAFE VERSION:
  //   import DOMPurify from 'dompurify';
  //   const bio = { __html: DOMPurify.sanitize(user.bio) };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Dashboard</h2>

      {user && (
        <div style={styles.card}>
          <h3 style={styles.cardTitle}>Welcome, {user.fullName || user.email}</h3>
          <p style={styles.meta}>Role: {user.role}</p>
          {/* VULN-ID: VB-016 | CWE-79 | Severity: HIGH */}
          <div
            className="profile-bio"
            dangerouslySetInnerHTML={{ __html: user.bio }}
            style={styles.bio}
          />
        </div>
      )}

      <div style={styles.card}>
        <h3 style={styles.cardTitle}>Your Accounts</h3>
        {accounts.length === 0 ? (
          <p style={styles.meta}>No accounts found.</p>
        ) : (
          <table style={styles.table}>
            <thead>
              <tr>
                <th style={styles.th}>Account No</th>
                <th style={styles.th}>Owner</th>
                <th style={styles.th}>Balance</th>
              </tr>
            </thead>
            <tbody>
              {accounts.map((acc) => (
                <tr key={acc.id}>
                  <td style={styles.td}>{acc.accountNo}</td>
                  <td style={styles.td}>{acc.ownerName}</td>
                  <td style={styles.td}>${acc.balance?.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

const styles = {
  container: { padding: '24px', fontFamily: 'Arial, sans-serif', background: '#1a1a2e', minHeight: '100vh', color: '#fff' },
  heading: { color: '#e94560', marginBottom: '24px' },
  card: { background: '#16213e', borderRadius: '8px', padding: '20px', marginBottom: '20px' },
  cardTitle: { color: '#e94560', marginTop: 0 },
  meta: { color: '#a8a8b3', fontSize: '14px' },
  bio: { color: '#fff', marginTop: '8px', padding: '8px', background: '#0f3460', borderRadius: '4px' },
  table: { width: '100%', borderCollapse: 'collapse' },
  th: { textAlign: 'left', color: '#a8a8b3', padding: '8px', borderBottom: '1px solid #0f3460' },
  td: { padding: '8px', borderBottom: '1px solid #0f3460', color: '#fff' },
};
