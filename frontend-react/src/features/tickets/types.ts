export type Ticket = {
  id: string;
  title: string;
  description: string;
  status: 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  departmentName: string;
  createdByEmail: string;
  assignedToEmail: string | null;
  createdAt: string;
  updatedAt: string;
};

export type PagedResponse<T> = {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export type Comment = {
  id: string;
  comment: string;
  authorEmail: string;
  createdAt: string;
};

export type Activity = {
  id: string;
  activityType: string;
  description: string;
  performedBy: string;
  createdAt: string;
}