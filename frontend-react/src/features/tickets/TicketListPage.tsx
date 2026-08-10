import { useEffect, useState } from 'react';
import { fetchTickets } from './api';
import type { PagedResponse, Ticket } from './types';
import { useAuth } from '../../auth/AuthContext';

export default function TicketListPage() {
  const [tickets, setTickets] = useState<PagedResponse<Ticket> | null>(null);
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [status, setStatus] = useState('');
  const [priority, setPriority] = useState('');
  const [loading, setLoading] = useState(false);
  const { logout } = useAuth();

  const loadTickets = async () => {
    setLoading(true);
    try {
      const data = await fetchTickets({
        page,
        size: 5,
        search: search || undefined,
        status: status || undefined,
        priority: priority || undefined,
      });
      setTickets(data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTickets();
  }, [page, status, priority]);

  return (
    <div className="p-6 space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Tickets</h1>
        <button onClick={logout} className="bg-red-500 text-white px-4 py-2 rounded-lg">
          Logout
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search title..."
          className="border rounded-lg p-3"
        />

        <select value={status} onChange={(e) => setStatus(e.target.value)} className="border rounded-lg p-3">
          <option value="">All Status</option>
          <option value="OPEN">OPEN</option>
          <option value="IN_PROGRESS">IN_PROGRESS</option>
          <option value="RESOLVED">RESOLVED</option>
          <option value="CLOSED">CLOSED</option>
        </select>

        <select value={priority} onChange={(e) => setPriority(e.target.value)} className="border rounded-lg p-3">
          <option value="">All Priority</option>
          <option value="LOW">LOW</option>
          <option value="MEDIUM">MEDIUM</option>
          <option value="HIGH">HIGH</option>
          <option value="CRITICAL">CRITICAL</option>
        </select>

        <button
          onClick={() => {
            setPage(0);
            loadTickets();
          }}
          className="bg-blue-600 text-white rounded-lg px-4 py-3 hover:bg-blue-700"
        >
          Search
        </button>
      </div>

      <div className="bg-white rounded-2xl shadow overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-100">
              <tr>
                <th className="text-left p-4">Title</th>
                <th className="text-left p-4">Status</th>
                <th className="text-left p-4">Priority</th>
                <th className="text-left p-4">Department</th>
                <th className="text-left p-4">Assignee</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr>
                  <td colSpan={5} className="p-6 text-center">Loading...</td>
                </tr>
              )}

              {!loading && tickets?.content.length === 0 && (
                <tr>
                  <td colSpan={5} className="p-6 text-center text-gray-500">
                    No tickets found
                  </td>
                </tr>
              )}

              {tickets?.content.map((ticket) => (
                <tr key={ticket.id} className="border-t hover:bg-gray-50">
                  <td className="p-4 font-medium">{ticket.title}</td>
                  <td className="p-4">
                    <span className="px-2 py-1 rounded-full text-xs bg-blue-100 text-blue-800">
                      {ticket.status}
                    </span>
                  </td>
                  <td className="p-4">{ticket.priority}</td>
                  <td className="p-4">{ticket.departmentName}</td>
                  <td className="p-4">{ticket.assignedToEmail || 'Unassigned'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {tickets && (
        <div className="flex justify-between items-center">
          <p className="text-sm text-gray-600">
            Page {tickets.page + 1} of {tickets.totalPages} ({tickets.totalElements} tickets)
          </p>

          <div className="flex gap-2">
            <button
              disabled={page === 0}
              onClick={() => setPage((p) => p - 1)}
              className="border px-4 py-2 rounded-lg disabled:opacity-50"
            >
              Previous
            </button>

            <button
              disabled={tickets.last}
              onClick={() => setPage((p) => p + 1)}
              className="border px-4 py-2 rounded-lg disabled:opacity-50"
            >
              Next
            </button>
          </div>
        </div>
      )}
    </div>
  );
}