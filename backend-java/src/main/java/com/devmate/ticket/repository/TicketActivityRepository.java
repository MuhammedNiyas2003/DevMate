package com.devmate.ticket.repository;

import com.devmate.ticket.entity.TicketActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketActivityRepository extends JpaRepository<TicketActivity, UUID> {
    List<TicketActivity> findByTicketIdOrderByCreatedAtAsc(UUID ticketId);
}