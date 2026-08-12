import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';
import AssignTicketCard from './components/AssignTicketCard';
import {
  addComment,
  fetchActivities,
  fetchComments,
  fetchTicket,
} from './api';
import type { Activity, Comment, Ticket } from './types';

import TicketInfoCard from './components/TicketInfoCard';
import TicketStatusActions from './components/TicketStatusActions';
import CommentForm from './components/CommentForm';
import CommentList from './components/CommentList';
import ActivityTimeline from './components/ActivityTimeline';

export default function TicketDetailPage() {
  const { id } = useParams();

  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [activities, setActivities] = useState<Activity[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { role } = useAuth();


  const loadData = async () => {
    if (!id) return;

    setLoading(true);
    setError('');

    try {
      const [ticketData, commentData, activityData] = await Promise.all([
        fetchTicket(id),
        fetchComments(id),
        fetchActivities(id),
      ]);

      setTicket(ticketData);
      setComments(commentData || []);
      setActivities(activityData || []);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load ticket');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [id]);

  const handleAddComment = async (comment: string) => {
    if (!id) return;

    await addComment(id, comment);
    await loadData();
  };

  if (loading) {
    return (
      <div className="p-6">
        <p className="text-gray-600">Loading ticket...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-6">
        <div className="bg-red-100 text-red-800 border border-red-200 rounded-lg p-4">
          {error}
        </div>
      </div>
    );
  }

  if (!ticket) {
    return (
      <div className="p-6">
        <p className="text-gray-600">Ticket not found.</p>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex items-center justify-between">
        <Link
          to="/tickets"
          className="text-blue-600 hover:text-blue-800 hover:underline text-sm font-medium"
        >
          ← Back to Tickets
        </Link>
      </div>

      {/* Ticket Information */}
      <TicketInfoCard ticket={ticket} />

      {/* Status Actions */}
      <TicketStatusActions ticket={ticket} onUpdated={loadData} />
      {role === 'ADMIN' && (
        <AssignTicketCard ticket={ticket} onAssigned={loadData} />
      )}

      {/* Comments + Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Comments */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-semibold">Comments</h2>
            <span className="text-sm text-gray-500">
              {comments.length} comment{comments.length !== 1 ? 's' : ''}
            </span>
          </div>

          <CommentForm onSubmit={handleAddComment} />

          <CommentList comments={comments} />
        </div>

        {/* Activity Timeline */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-semibold">Activity History</h2>
            <span className="text-sm text-gray-500">
              {activities.length} activit{activities.length !== 1 ? 'ies' : 'y'}
            </span>
          </div>

          <ActivityTimeline activities={activities} />
        </div>
      </div>
    </div>
  );
}