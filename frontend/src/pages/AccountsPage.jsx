import React, { useEffect, useState } from 'react';
import api from '../api/axios';

export default function AccountsPage() {
  const [accounts, setAccounts] = useState([]);
  const [selectedId, setSelectedId] = useState('');
  const [balance, setBalance] = useState(null);

  useEffect(() => {
    api.get('/accounts').then((res) => setAccounts(res.data)).catch(() => {});
  }, []);

  const fetchBalance = async () => {
    if (!selectedId) return;
    try {
      // VULN-ID: VB-002 | CWE-639 — IDOR: any ID can be queried, no ownership check
      const res = await api.get(`/accounts/${selectedId}/balance`);
      setBalance(res.data.balance);
    } catch {
      setBalance('Error fetching balance');
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Accounts</h2>

      <div style={styles.card}>
        <h3 style={styles.cardTitle}>All Accounts</h3>
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>ID</th>
              <th style={styles.th}>Account No</th>
              <th style={styles.th}>Owner</th>
              <th style={styles.th}>Balance</th>
            </tr>
          </thead>
          <tbody>
            {accounts.map((acc) => (
              <tr key={acc.id}>
                <td style={styles.td}>{acc.id}</td>
                <td style={styles.td}>{acc.accountNo}</td>
                <td style={styles.td}>{acc.ownerName}</td>
                <td style={styles.td}>${acc.balance?.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div style={styles.card}>
        <h3 style={styles.cardTitle}>Check Balance by Account ID (IDOR Demo)</h3>
        <div style={styles.row}>
          <input
            type="number"
            value={selectedId}
            onChange={(e) => setSelectedId(e.target.value)}
            placeholder="Enter any account ID"
            style={styles.input}
          />
          <button onClick={fetchBalance} style={styles.button}>Check Balance</button>
        </div>
        {balance !== null && (
          <p style={styles.result}>Balance: ${typeof balance === 'number' ? balance.toFixed(2) : balance}</p>
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
  table: { width: '100%', borderCollapse: 'collapse' },
  th: { textAlign: 'left', color: '#a8a8b3', padding: '8px', borderBottom: '1px solid #0f3460' },
  td: { padding: '8px', borderBottom: '1px solid #0f3460', color: '#fff' },
  row: { display: 'flex', gap: '12px', alignItems: 'center' },
  input: { background: '#0f3460', border: '1px solid #e94560', borderRadius: '6px', padding: '8px 12px', color: '#fff', fontSize: '14px' },
  button: { background: '#e94560', color: '#fff', border: 'none', borderRadius: '6px', padding: '8px 16px', cursor: 'pointer' },
  result: { color: '#4caf50', marginTop: '12px' },
};
