import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import TicketForm from './TicketForm';
import type { TicketFormValues } from './validation';

type Department = {
    id: string;
    name: string;
};

export default function CreateTicketPage() {
    const [departments, setDepartments] = useState<Department[]>([]);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        api.get('/departments').then((res) => {
            setDepartments(res.data);
        });
    }, []);

    const handleSubmit = async (values: TicketFormValues) => {
        setError('');
        setMessage('');

        try {
            const response = await api.post('/tickets', values);
            const createdTicket = response.data.data;

            setMessage('Ticket created successfully! Redirecting...');

            setTimeout(() => {
                navigate(`/tickets/${createdTicket.id}`);
            }, 1200);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to create ticket');
        }
    };

    return (
        <div className="p-6 max-w-4xl mx-auto space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <Link to="/tickets" className="text-blue-600 hover:underline text-sm">
                        ← Back to Tickets
                    </Link>
                    <h1 className="text-3xl font-bold mt-2">Create Ticket</h1>
                </div>
            </div>

            {message && (
                <div className="bg-green-100 text-green-800 border border-green-200 rounded-lg p-4">
                    {message}
                </div>
            )}

            {error && (
                <div className="bg-red-100 text-red-800 border border-red-200 rounded-lg p-4">
                    {error}
                </div>
            )}

            <TicketForm departments={departments} onSubmit={handleSubmit} />
        </div>
    );
}