import { useState } from 'react';
import { updateTicketStatus } from '../api';
import type { Ticket } from '../types';

type Props = {
  ticket: Ticket;
  onUpdated: () => Promise<void>;
};

export default function TicketStatusActions({ ticket, onUpdated }: Props) {
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleUpdate = async (status: string) => {
    setLoading(true);
    setError('');
    setMessage('');

    try {
      await updateTicketStatus(ticket.id, status);
      setMessage(`Status updated to ${status}`);
      await onUpdated();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update status');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow p-6 space-y-4">
      <h3 className="text-lg font-semibold">Status Actions</h3>

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

      <div className="flex flex-wrap gap-3">
        {ticket.status === 'OPEN' && (
          <button
            disabled={loading}
            onClick={() => handleUpdate('IN_PROGRESS')}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? 'Updating...' : 'Start Progress'}
          </button>
        )}

        {ticket.status === 'IN_PROGRESS' && (
          <button
            disabled={loading}
            onClick={() => handleUpdate('RESOLVED')}
            className="bg-yellow-600 text-white px-4 py-2 rounded-lg hover:bg-yellow-700 disabled:opacity-50"
          >
            {loading ? 'Updating...' : 'Resolve'}
          </button>
        )}

        {ticket.status === 'RESOLVED' && (
          <button
            disabled={loading}
            onClick={() => handleUpdate('CLOSED')}
            className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 disabled:opacity-50"
          >
            {loading ? 'Updating...' : 'Close'}
          </button>
        )}

        {ticket.status === 'CLOSED' && (
          <p className="text-sm text-gray-500">This ticket is closed.</p>
        )}
      </div>
    </div>
  );
}