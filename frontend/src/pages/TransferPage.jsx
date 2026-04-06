import React, { useState } from 'react';
import api from '../api/axios';
import { calculateFormula } from '../utils/formulaHelper';

export default function TransferPage() {
  const [fromAccount, setFromAccount] = useState('');
  const [toAccount, setToAccount] = useState('');
  const [amount, setAmount] = useState('');
  const [note, setNote] = useState('');
  const [formula, setFormula] = useState('');
  const [feeResult, setFeeResult] = useState(null);
  const [message, setMessage] = useState('');

  const handleTransfer = async (e) => {
    e.preventDefault();
    setMessage('');
    try {
      await api.post('/transactions', {
        fromAccount: parseInt(fromAccount),
        toAccount: parseInt(toAccount),
        ownerName: 'Bob User',
        amount: parseFloat(amount),
        note,
      });
      setMessage('Transfer submitted successfully!');
    } catch {
      setMessage('Transfer failed. Please try again.');
    }
  };

  // VULN-ID: VB-019 | CWE-95 | Severity: CRITICAL
  // eval() used to compute user-supplied fee formula — see formulaHelper.js
  const handleCalculateFee = () => {
    try {
      const result = calculateFormula(formula);
      setFeeResult(result);
    } catch (err) {
      setFeeResult('Error: ' + err.message);
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Transfer Funds</h2>

      <div style={styles.card}>
        <h3 style={styles.cardTitle}>New Transfer</h3>
        <form onSubmit={handleTransfer} style={styles.form}>
          <input
            value={fromAccount}
            onChange={(e) => setFromAccount(e.target.value)}
            placeholder="From Account ID"
            style={styles.input}
            required
          />
          <input
            value={toAccount}
            onChange={(e) => setToAccount(e.target.value)}
            placeholder="To Account ID"
            style={styles.input}
            required
          />
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="Amount"
            style={styles.input}
            required
          />
          <input
            value={note}
            onChange={(e) => setNote(e.target.value)}
            placeholder="Note (optional)"
            style={styles.input}
          />
          <button type="submit" style={styles.button}>Submit Transfer</button>
          {message && <p style={styles.msg}>{message}</p>}
        </form>
      </div>

      <div style={styles.card}>
        <h3 style={styles.cardTitle}>Fee Calculator (eval-based)</h3>
        <p style={styles.meta}>Enter a custom formula to calculate transfer fee (e.g., amount * 0.02)</p>
        <div style={styles.row}>
          <input
            value={formula}
            onChange={(e) => setFormula(e.target.value)}
            placeholder="e.g. 500 * 0.02"
            style={styles.input}
          />
          <button onClick={handleCalculateFee} style={styles.button}>Calculate</button>
        </div>
        {feeResult !== null && (
          <p style={styles.result}>Fee: {String(feeResult)}</p>
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
  meta: { color: '#a8a8b3', fontSize: '13px' },
  form: { display: 'flex', flexDirection: 'column', gap: '12px' },
  row: { display: 'flex', gap: '12px', alignItems: 'center', marginTop: '8px' },
  input: { background: '#0f3460', border: '1px solid #e94560', borderRadius: '6px', padding: '8px 12px', color: '#fff', fontSize: '14px' },
  button: { background: '#e94560', color: '#fff', border: 'none', borderRadius: '6px', padding: '8px 16px', cursor: 'pointer' },
  msg: { color: '#4caf50', margin: 0 },
  result: { color: '#4caf50', marginTop: '12px' },
};
