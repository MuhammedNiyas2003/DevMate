import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from '../pages/LoginPage';
import DashboardPage from '../pages/DashboardPage';
import ProtectedRoute from '../components/ProtectedRoute';
import TicketListPage from '../features/tickets/TicketListPage';
import TicketDetailPage from '../features/tickets/TicketDetailPage';
import CreateTicketPage from '../features/tickets/CreateTicketPage';

export default function AppRoutes() {
  return (
    <BrowserRouter future={{ v7_relativeSplatPath: true }}>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route path="/" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />

        <Route path="/tickets" element={<ProtectedRoute><TicketListPage /></ProtectedRoute>} />
        <Route path="/tickets/:id" element={<ProtectedRoute> <TicketDetailPage /></ProtectedRoute>} />
        <Route path="/tickets/new" element={<ProtectedRoute> <CreateTicketPage /></ProtectedRoute>} />
      </Routes>
    </BrowserRouter>
  );
}