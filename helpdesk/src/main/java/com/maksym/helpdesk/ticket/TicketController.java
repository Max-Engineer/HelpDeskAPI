package com.maksym.helpdesk.ticket;

import com.maksym.helpdesk.ticket.dto.TicketRequest;
import com.maksym.helpdesk.ticket.dto.TicketResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/tickets")
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(201).body(ticketService.create(request, username));
    }

    @GetMapping("/tickets")
    public List<TicketResponse> getAllTickets(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ticketService.getAll(username);
    }

    @GetMapping("/tickets/status")
    public List<TicketResponse> getTicketStatus(@RequestParam TicketStatus status){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ticketService.filterByStatus(status, username);
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ticketService.getById(id, username));
    }

    @PutMapping("/tickets/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ticketService.update(id, request, username));
    }

    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ticketService.delete(id, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tickets/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(@PathVariable Long id, @RequestParam TicketStatus status){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ticketService.updateStatus(id, status, username));
    }

    @PatchMapping("/tickets/{id}/assign")
    public ResponseEntity<TicketResponse> assignTicket(@PathVariable Long id, @RequestParam Long technicianId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ticketService.assign(id, technicianId, username));
    }
}
