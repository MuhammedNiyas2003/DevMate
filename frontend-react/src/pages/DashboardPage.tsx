import { useEffect, useState } from 'react';
import api from '../api/axios';
import type { DashboardStats } from '../types/dashboard';
import { useAuth } from '../auth/AuthContext';

export default function DashboardPage() {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const { logout } = useAuth();

  useEffect(() => {
    api.get('/dashboard/stats').then((res) => setStats(res.data.data));
  }, []);

  if (!stats) return <p className="p-6">Loading...</p>;

  const cards = [
    ['Total Tickets', stats.totalTickets],
    ['Open', stats.openTickets],
    ['In Progress', stats.inProgressTickets],
    ['Resolved', stats.resolvedTickets],
    ['Closed', stats.closedTickets],
    ['Users', stats.totalUsers],
    ['Departments', stats.totalDepartments],
  ];

  return (
    <div className="p-6 space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">DevMate Dashboard</h1>
        <button onClick={logout} className="bg-red-500 text-white px-4 py-2 rounded-lg">
          Logout
        </button>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {cards.map(([label, value]) => (
          <div key={label} className="bg-white p-6 rounded-2xl shadow">
            <p className="text-gray-500 text-sm">{label}</p>
            <p className="text-3xl font-bold mt-2">{value}</p>
          </div>
        ))}
      </div>
    </div>
  );
}