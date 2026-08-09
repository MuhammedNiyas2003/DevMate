package com.devmate.ticket.repository;

import com.devmate.ticket.entity.Ticket;
import com.devmate.ticket.entity.TicketStatus;
import com.devmate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketRepository extends JpaRepository<Ticket, UUID>,
        JpaSpecificationExecutor<Ticket> {

    long countByStatus(TicketStatus status);

    List<Ticket> findByCreatedByOrAssignedTo(User createdBy, User assignedTo);
}