
import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function FriendsList() {
  const [friends, setFriends] = useState([]);
  const [users, setUsers] = useState([]);

  useEffect(() => {
    api.get('/api/friends/me').then(res => setFriends(res.data));
    api.get('/api/users').then(res => setUsers(res.data));
  }, []);

  const addFriend = async (friendId) => {
    await api.post(`/api/friends/${friendId}`);
    const { data } = await api.get('/api/friends/me');
    setFriends(data);
  };

  const removeFriend = async (friendId) => {
    await api.delete(`/api/friends/${friendId}`);
    const { data } = await api.get('/api/friends/me');
    setFriends(data);
  };

  const friendIds = new Set(friends.map(f => f.id));

  return (
    <div>
      <h3>My Friends</h3>
      <ul>
        {friends.map(f => (
          <li key={f.id}>
            {f.username} <button onClick={()=>removeFriend(f.id)}>Remove</button>
          </li>
        ))}
      </ul>

      <h4>Add Friend</h4>
      <ul>
        {users.filter(u => !friendIds.has(u.id)).map(u => (
          <li key={u.id}>
            {u.username} <button onClick={()=>addFriend(u.id)}>Add</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

