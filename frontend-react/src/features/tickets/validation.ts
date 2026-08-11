export type TicketFormValues = {
  title: string;
  description: string;
  departmentId: string;
  priority: string;
};

export function validateTicket(values: TicketFormValues) {
  const errors: Partial<Record<keyof TicketFormValues, string>> = {};

  if (!values.title.trim()) {
    errors.title = 'Title is required';
  } else if (values.title.length < 5) {
    errors.title = 'Title must be at least 5 characters';
  }

  if (!values.description.trim()) {
    errors.description = 'Description is required';
  } else if (values.description.length < 10) {
    errors.description = 'Description must be at least 10 characters';
  }

  if (!values.departmentId) {
    errors.departmentId = 'Department is required';
  }

  if (!values.priority) {
    errors.priority = 'Priority is required';
  }

  return errors;
}