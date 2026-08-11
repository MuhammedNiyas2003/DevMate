import { useState } from 'react';
import { validateTicket, type TicketFormValues } from './validation';

type Department = {
  id: string;
  name: string;
};

type Props = {
  departments?: Department[];
  onSubmit: (values: TicketFormValues) => Promise<void>;
};

export default function TicketForm({ departments = [], onSubmit }: Props) {  const [values, setValues] = useState<TicketFormValues>({
    title: '',
    description: '',
    departmentId: '',
    priority: 'MEDIUM',
  });

  const [errors, setErrors] = useState<Partial<Record<keyof TicketFormValues, string>>>({});
  const [loading, setLoading] = useState(false);

  const handleChange = (field: keyof TicketFormValues, value: string) => {
    setValues((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const validationErrors = validateTicket(values);
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length > 0) return;

    setLoading(true);
    try {
      await onSubmit(values);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white rounded-2xl shadow p-6 space-y-5">
      <div>
        <label className="block text-sm font-medium mb-2">Title</label>
        <input
          value={values.title}
          onChange={(e) => handleChange('title', e.target.value)}
          className="w-full border rounded-lg p-3"
          placeholder="Enter ticket title"
        />
        {errors.title && <p className="text-red-500 text-sm mt-1">{errors.title}</p>}
      </div>

      <div>
        <label className="block text-sm font-medium mb-2">Description</label>
        <textarea
          value={values.description}
          onChange={(e) => handleChange('description', e.target.value)}
          className="w-full border rounded-lg p-3 min-h-[140px]"
          placeholder="Describe the issue in detail"
        />
        {errors.description && <p className="text-red-500 text-sm mt-1">{errors.description}</p>}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium mb-2">Department</label>
          <select
            value={values.departmentId}
            onChange={(e) => handleChange('departmentId', e.target.value)}
            className="w-full border rounded-lg p-3"
          >
            <option value="">Select department</option>
            {departments.map((dept) => (
              <option key={dept.id} value={dept.id}>
                {dept.name}
              </option>
            ))}
          </select>
          {errors.departmentId && <p className="text-red-500 text-sm mt-1">{errors.departmentId}</p>}
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">Priority</label>
          <select
            value={values.priority}
            onChange={(e) => handleChange('priority', e.target.value)}
            className="w-full border rounded-lg p-3"
          >
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
            <option value="CRITICAL">CRITICAL</option>
          </select>
        </div>
      </div>

      <div className="flex justify-end gap-3">
        <button
          type="submit"
          disabled={loading}
          className="bg-blue-600 text-white px-5 py-3 rounded-lg hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? 'Creating...' : 'Create Ticket'}
        </button>
      </div>
    </form>
  );
}