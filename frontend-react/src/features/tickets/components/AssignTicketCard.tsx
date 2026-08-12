import { useEffect, useState } from 'react';
import api from '../../../api/axios';
import { assignTicket } from '../api';
import type { Ticket } from '../types';

type User = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  roleName: string;
};

type Props = {
  ticket: Ticket;
  onAssigned: () => Promise<void>;
};

export default function AssignTicketCard({ ticket, onAssigned }: Props) {
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUserId, setSelectedUserId] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/users').then((res) => {
      setUsers(res.data.data || []);
    }).catch(() => {
      setError('Failed to load users');
    });
  }, []);

  const handleAssign = async () => {
    if (!selectedUserId) {
      setError('Please select a user');
      return;
    }

    setLoading(true);
    setError('');
    setMessage('');

    try {
      await assignTicket(ticket.id, selectedUserId);
      setMessage('Ticket assigned successfully');
      await onAssigned();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to assign ticket');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow p-6 space-y-4">
      <h3 className="text-lg font-semibold">Assign Ticket</h3>

      <p className="text-sm text-gray-600">
        Current assignee: <span className="font-medium">{ticket.assignedToEmail || 'Unassigned'}</span>
      </p>

      {message && (
        <div className="bg-green-100 text-green-800 border border-green-200 rounded-lg p-3 text-sm">
          {message}
        </div>
      )}

      {error && (
        <div className="bg-red-100 text-red-800 border border-red-200 rounded-lg p-3 text-sm">
          {error}
        </div>
      )}

      <div className="space-y-3">
        <select
          value={selectedUserId}
          onChange={(e) => setSelectedUserId(e.target.value)}
          className="w-full border rounded-lg p-3"
        >
          <option value="">Select user</option>
          {users.map((user) => (
            <option key={user.id} value={user.id}>
              {user.firstName} {user.lastName} ({user.roleName})
            </option>
          ))}
        </select>

        <button
          onClick={handleAssign}
          disabled={loading}
          className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 disabled:opacity-50"
        >
          {loading ? 'Assigning...' : 'Assign Ticket'}
        </button>
      </div>
    </div>
  );
}