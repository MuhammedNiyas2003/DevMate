package com.devmate.ticket.repository;

import com.devmate.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import com.devmate.ticket.entity.TicketStatus;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    long countByStatus(TicketStatus status);
}