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