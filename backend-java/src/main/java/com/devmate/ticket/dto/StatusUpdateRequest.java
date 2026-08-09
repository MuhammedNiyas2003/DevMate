package com.devmate.ticket.dto;

import com.devmate.ticket.entity.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {

    @NotNull
    private TicketStatus status;
}