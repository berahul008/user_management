
import { useState } from 'react';
import { useAuth } from '../context/AuthContext';

export default function RegisterForm() {
  const { register } = useAuth();
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [error, setError] = useState('');

  const onSubmit = async e => {
    e.preventDefault();
    setError('');
    try { await register(form); } catch (err) {
      setError(err.response?.data?.message || err.message);
    }
  };

  return (
    <form onSubmit={onSubmit}>
      <h2>Register</h2>
      <input placeholder="Username" value={form.username}
             onChange={e=>setForm({ ...form, username: e.target.value })} />
      <input placeholder="Email" value={form.email}
             onChange={e=>setForm({ ...form, email: e.target.value })} />
      <input placeholder="Password" type="password" value={form.password}
             onChange={e=>setForm({ ...form, password: e.target.value })} />
      <button type="submit">Sign up</button>
      {error && <p style={{color:'red'}}>{error}</p>}
    </form>
  );
}
