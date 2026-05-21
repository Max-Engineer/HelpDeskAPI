package com.maksym.helpdesk.ticket.dto;

import com.maksym.helpdesk.ticket.TicketPriority;
import com.maksym.helpdesk.ticket.TicketStatus;
import jakarta.validation.constraints.NotBlank;

public class TicketRequest {

    @NotBlank(message = "Title is required!")
    private String title;

    private String description;
    private TicketStatus status;
    private TicketPriority priority;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}

