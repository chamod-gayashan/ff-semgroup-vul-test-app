// VULN-ID: VB-020 | CWE-346 | Severity: MEDIUM
// postMessage Without Origin Check: message events are processed without
// validating event.origin, allowing any cross-origin page to trigger actions.
// SAFE VERSION:
//   if (event.origin !== 'https://vulnbank.internal') return;
function processAction(action, payload) {
  if (action === 'navigate') {
    window.location.href = payload.url;
  } else if (action === 'setToken') {
    localStorage.setItem('vulnbank_jwt', payload.token);
  }
}

window.addEventListener('message', (event) => {
  // Missing: if (event.origin !== 'https://vulnbank.internal') return;
  const { action, payload } = event.data;
  processAction(action, payload);
});

export { processAction };
