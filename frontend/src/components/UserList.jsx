
import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function UserList() {
  const [users, setUsers] = useState([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    api.get('/api/users').then(res => setUsers(res.data));
  }, []);

  const deleteUser = async (id) => {
    try { await api.delete(`/api/users/${id}`); setUsers(users.filter(u => u.id !== id)); }
    catch (e) { setMessage(e.response?.status === 403 ? 'Forbidden' : 'Error deleting'); }
  };

  return (
    <div>
      <h3>All Users</h3>
      {message && <p>{message}</p>}
      <ul>
        {users.map(u => (
          <li key={u.id}>
            {u.username} ({u.email})
            <button onClick={()=>deleteUser(u.id)} style={{marginLeft:8}}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

