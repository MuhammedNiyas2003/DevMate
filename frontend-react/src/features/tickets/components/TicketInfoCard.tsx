import type { Ticket } from '../types';

export default function TicketInfoCard({ ticket }: { ticket: Ticket }) {
  return (
    <div className="bg-white rounded-2xl shadow p-6 space-y-4">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
        <h2 className="text-2xl font-bold">{ticket.title}</h2>

        <div className="flex gap-2">
          <span className="px-3 py-1 rounded-full text-sm bg-blue-100 text-blue-800">
            {ticket.status}
          </span>

          <span className="px-3 py-1 rounded-full text-sm bg-red-100 text-red-800">
            {ticket.priority}
          </span>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
        <div>
          <p className="text-gray-500">Department</p>
          <p className="font-medium">{ticket.departmentName}</p>
        </div>

        <div>
          <p className="text-gray-500">Created By</p>
          <p className="font-medium">{ticket.createdByEmail}</p>
        </div>

        <div>
          <p className="text-gray-500">Assigned To</p>
          <p className="font-medium">{ticket.assignedToEmail || 'Unassigned'}</p>
        </div>

        <div>
          <p className="text-gray-500">Created At</p>
          <p className="font-medium">{new Date(ticket.createdAt).toLocaleString()}</p>
        </div>
      </div>

      <div>
        <p className="text-gray-500 mb-2">Description</p>
        <p className="whitespace-pre-wrap">{ticket.description}</p>
      </div>
    </div>
  );
}