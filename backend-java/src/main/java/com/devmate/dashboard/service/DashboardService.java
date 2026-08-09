package com.devmate.dashboard.service;

import com.devmate.dashboard.dto.DashboardStatsResponse;
import com.devmate.department.repository.DepartmentRepository;
import com.devmate.ticket.entity.TicketStatus;
import com.devmate.ticket.repository.TicketRepository;
import com.devmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public DashboardStatsResponse getStats() {
        return DashboardStatsResponse.builder()
                .totalTickets(ticketRepository.count())
                .openTickets(ticketRepository.countByStatus(TicketStatus.OPEN))
                .inProgressTickets(ticketRepository.countByStatus(TicketStatus.IN_PROGRESS))
                .resolvedTickets(ticketRepository.countByStatus(TicketStatus.RESOLVED))
                .closedTickets(ticketRepository.countByStatus(TicketStatus.CLOSED))
                .totalUsers(userRepository.count())
                .totalDepartments(departmentRepository.count())
                .build();
    }
}