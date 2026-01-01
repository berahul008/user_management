
import { createContext, useContext, useState } from 'react';
import api from '../api/axios';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  async function register({ username, email, password }) {
    const { data } = await api.post('/api/auth/register', { username, email, password });
    localStorage.setItem('token', data.token);
    setUser({ id: data.userId, username: data.username, email: data.email });
  }

  async function login({ username, password }) {
    const { data } = await api.post('/api/auth/login', { username, password });
    localStorage.setItem('token', data.token);
    setUser({ id: data.userId, username: data.username, email: data.email });
  }

  function logout() {
    localStorage.removeItem('token');
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, register, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
