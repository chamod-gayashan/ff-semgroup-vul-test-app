import axios from 'axios';

// VULN-ID: VB-018 | CWE-922 | Severity: MEDIUM
// Sensitive Token in localStorage: JWT is stored in localStorage, making it
// accessible to any JavaScript on the page (including XSS payloads).
// SAFE VERSION: store token in an httpOnly cookie set by the server so that
// JavaScript cannot access it at all.
export const saveToken = (token) => {
  localStorage.setItem('vulnbank_jwt', token);
};

export const getToken = () => localStorage.getItem('vulnbank_jwt');

export const removeToken = () => localStorage.removeItem('vulnbank_jwt');

const api = axios.create({
  baseURL: '/api',
});

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
