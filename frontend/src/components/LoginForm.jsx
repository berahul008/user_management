
import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function LoginForm() {
  const { login } = useAuth();
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const onSubmit = async e => {
    e.preventDefault();
    setError('');
    try { await login(form); navigate('/'); } catch (err) {
      setError(err.response?.data?.message || err.message);
    }
  };

  return (
    <form onSubmit={onSubmit}>
      <h2>Login</h2>
      <input placeholder="Username" value={form.username}
             onChange={e=>setForm({ ...form, username: e.target.value })} />
      <input placeholder="Password" type="password" value={form.password}
             onChange={e=>setForm({ ...form, password: e.target.value })} />
      <button type="submit">Sign in</button>
      {error && <p style={{color:'red'}}>{error}</p>}
    </form>
  );
}

