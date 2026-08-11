import { useState } from 'react';
import { updateTicketStatus } from '../api';

type Props = {
  ticketId: string;
  currentStatus: string;
  onUpdated: () => Promise<void>;
};

const statuses = ['OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'];

export default function TicketStatusUpdater({
  ticketId,
  currentStatus,
  onUpdated,
}: Props) {
  const [status, setStatus] = useState(currentStatus);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleUpdate = async () => {
    setLoading(true);
    setMessage('');
    setError('');

    try {
      await updateTicketStatus(ticketId, status);
      setMessage('Status updated successfully');
      await onUpdated();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update status');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow p-6 space-y-4">
      <div className="flex flex-col md:flex-row md:items-end gap-4">
        <div className="flex-1">
          <label className="block text-sm font-medium mb-2">Ticket Status</label>
          <select
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            className="w-full border rounded-lg p-3"
          >
            {statuses.map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </div>

        <button
          onClick={handleUpdate}
          disabled={loading || status === currentStatus}
          className="bg-blue-600 text-white px-5 py-3 rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {loading ? 'Updating...' : 'Update Status'}
        </button>
      </div>

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
    </div>
  );
}