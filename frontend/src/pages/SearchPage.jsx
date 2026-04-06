import React, { useState } from 'react';
import api from '../api/axios';

export default function SearchPage() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);

  // VULN-ID: VB-017 | CWE-79 | Severity: HIGH
  // DOM XSS via innerHTML: user search term written directly to DOM via innerHTML,
  // allowing script injection when search term contains HTML/JS payloads.
  // SAFE VERSION: use textContent instead of innerHTML:
  //   document.getElementById('search-label').textContent =
  //     'Showing results for: ' + term;
  const handleSearch = (term) => {
    document.getElementById('search-label').innerHTML =
      'Showing results for: ' + term;
  };

  const doSearch = async () => {
    handleSearch(query);
    try {
      const res = await api.get(`/accounts/search?name=${encodeURIComponent(query)}`);
      setResults(res.data);
    } catch {
      setResults([]);
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Search Accounts</h2>

      <div style={styles.card}>
        <div style={styles.row}>
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search by owner name..."
            style={styles.input}
            onKeyDown={(e) => e.key === 'Enter' && doSearch()}
          />
          <button onClick={doSearch} style={styles.button}>Search</button>
        </div>

        {/* VULN-ID: VB-017 | CWE-79 — innerHTML used here */}
        <p id="search-label" style={styles.label}></p>

        {results.length > 0 && (
          <table style={styles.table}>
            <thead>
              <tr>
                <th style={styles.th}>Account No</th>
                <th style={styles.th}>Owner</th>
                <th style={styles.th}>Balance</th>
              </tr>
            </thead>
            <tbody>
              {results.map((acc, i) => (
                <tr key={i}>
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
  card: { background: '#16213e', borderRadius: '8px', padding: '20px' },
  row: { display: 'flex', gap: '12px', alignItems: 'center' },
  input: { background: '#0f3460', border: '1px solid #e94560', borderRadius: '6px', padding: '8px 12px', color: '#fff', fontSize: '14px', flex: 1 },
  button: { background: '#e94560', color: '#fff', border: 'none', borderRadius: '6px', padding: '8px 16px', cursor: 'pointer' },
  label: { color: '#a8a8b3', fontSize: '13px', marginTop: '12px' },
  table: { width: '100%', borderCollapse: 'collapse', marginTop: '12px' },
  th: { textAlign: 'left', color: '#a8a8b3', padding: '8px', borderBottom: '1px solid #0f3460' },
  td: { padding: '8px', borderBottom: '1px solid #0f3460', color: '#fff' },
};
