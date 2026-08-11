import api from '../../api/axios';
import type { PagedResponse, Ticket } from './types';

type SearchParams = {
  page: number;
  size: number;
  search?: string;
  status?: string;
  priority?: string;
};

export async function fetchTickets(params: SearchParams) {
  const response = await api.get('/tickets/search', { params });
  return response.data.data as PagedResponse<Ticket>;
}

export async function fetchTicket(id: string) {
  const response = await api.get(`/tickets/${id}`);
  return response.data.data as Ticket;
}

export async function fetchComments(id: string) {
  const response = await api.get(`/tickets/${id}/comments`);
  return response.data.data as Comment[];
}

export async function addComment(id: string, comment: string) {
  const response = await api.post(`/tickets/${id}/comments`, { comment });
  return response.data.data as Comment;
}

export async function fetchActivities(id: string) {
  const response = await api.get(`/tickets/${id}/activities`);
  return response.data.data as Activity[];
}

export async function updateTicketStatus(id: string, status: string) {
  const response = await api.put(`/tickets/${id}/status`, { status });
  return response.data.data;
}