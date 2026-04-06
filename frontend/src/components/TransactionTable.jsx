import React from 'react';

export default function TransactionTable({ transactions }) {
  if (!transactions || transactions.length === 0) {
    return <p style={styles.empty}>No transactions found.</p>;
  }

  return (
    <table style={styles.table}>
      <thead>
        <tr>
          <th style={styles.th}>ID</th>
          <th style={styles.th}>From</th>
          <th style={styles.th}>To</th>
          <th style={styles.th}>Owner</th>
          <th style={styles.th}>Amount</th>
          <th style={styles.th}>Note</th>
          <th style={styles.th}>Date</th>
        </tr>
      </thead>
      <tbody>
        {transactions.map((tx) => (
          <tr key={tx.id}>
            <td style={styles.td}>{tx.id}</td>
            <td style={styles.td}>{tx.fromAccount}</td>
            <td style={styles.td}>{tx.toAccount}</td>
            <td style={styles.td}>{tx.ownerName}</td>
            <td style={styles.td}>${tx.amount?.toFixed(2)}</td>
            <td style={styles.td}>{tx.note || '-'}</td>
            <td style={styles.td}>{tx.createdAt ? new Date(tx.createdAt).toLocaleDateString() : '-'}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

const styles = {
  empty: { color: '#a8a8b3', fontSize: '14px' },
  table: { width: '100%', borderCollapse: 'collapse' },
  th: { textAlign: 'left', color: '#a8a8b3', padding: '8px', borderBottom: '1px solid #0f3460', fontSize: '13px' },
  td: { padding: '8px', borderBottom: '1px solid #0f3460', color: '#fff', fontSize: '13px' },
};
