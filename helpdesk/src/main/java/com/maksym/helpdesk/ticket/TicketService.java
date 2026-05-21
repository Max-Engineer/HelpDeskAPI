package com.maksym.helpdesk.ticket;

import com.maksym.helpdesk.auth.User;
import com.maksym.helpdesk.auth.UserRepository;
import com.maksym.helpdesk.exception.ResourceNotFoundException;
import com.maksym.helpdesk.ticket.dto.TicketRequest;
import com.maksym.helpdesk.ticket.dto.TicketResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    private TicketResponse toResponse(Ticket ticket){
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus(),
                ticket.getPriority(), ticket.getCreatedAt(), ticket.getUpdatedAt(), ticket.getCreatedBy().getUsername(),
                ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null);
    }
    private User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public TicketResponse create(TicketRequest request, String username) {
        User user = getCurrentUser(username);

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(request.getStatus() != null ? request.getStatus() : TicketStatus.OPEN);
        ticket.setCreatedBy(user);

        return toResponse(ticketRepository.save(ticket));
    }
    public List<TicketResponse> getAll(String username){
        User user = getCurrentUser(username);

        return switch (user.getRole()) {
            case "ADMIN" -> ticketRepository.findAll().stream()
                    .map(this::toResponse).collect(Collectors.toList());
            case "TECHNICIAN" -> ticketRepository.findByAssignedTo(user).stream()
                    .map(this::toResponse).collect(Collectors.toList());
            case "REQUESTER" -> ticketRepository.findByCreatedBy(user).stream()
                    .map(this::toResponse).collect(Collectors.toList());
            default -> List.of();
        };
    }

    public TicketResponse getById(Long id, String username) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));
        User user = getCurrentUser(username);

        boolean canAccess = switch (user.getRole()) {
            case "ADMIN" -> true;
            case "TECHNICIAN" -> ticket.getAssignedTo() != null &&
                    ticket.getAssignedTo().getId().equals(user.getId());
            case "REQUESTER" -> ticket.getCreatedBy().getId().equals(user.getId());
            default -> false;
        };

        if (!canAccess) {
            throw new AccessDeniedException("Access denied!");
        }
        return toResponse(ticket);
    }

    public TicketResponse update(Long id, TicketRequest request, String username) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));

        User user = getCurrentUser(username);

        boolean canAccess = switch (user.getRole()){
            case "ADMIN" -> true;
            case "TECHNICIAN" -> ticket.getAssignedTo() != null &&
                    ticket.getAssignedTo().getId().equals(user.getId());
            default -> false;
        };
        if (!canAccess) {
            throw new AccessDeniedException("Access denied!");
        }

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(request.getStatus());

        return toResponse(ticketRepository.save(ticket));
    }

    public void delete(Long id, String username) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));

        User user = getCurrentUser(username);

        if (!user.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("Access denied!");
        }

        ticketRepository.delete(ticket);
    }

    public TicketResponse updateStatus(Long id, TicketStatus status, String username) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));

        User user = getCurrentUser(username);

        boolean canAccess = switch (user.getRole()){
            case "ADMIN" -> true;
            case "TECHNICIAN" -> ticket.getAssignedTo() != null &&
                    ticket.getAssignedTo().getId().equals(user.getId());
            default -> false;
        };
        if (!canAccess) {
            throw new AccessDeniedException("Access denied!");
        }
        ticket.setStatus(status);

        return toResponse(ticketRepository.save(ticket));
    }

    public TicketResponse assign(Long id, Long technicianId, String username) {
        User user = getCurrentUser(username);
        if (!user.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("Access denied!");
        }
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));
        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found!"));
        if (!technician.getRole().equals("TECHNICIAN")) {
            throw new IllegalArgumentException("User is not a technician!");
        }
        ticket.setAssignedTo(technician);
        return toResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> filterByStatus(TicketStatus status, String username) {
        User user = getCurrentUser(username);
        List<Ticket> tickets = ticketRepository.findByStatus(status);

        return tickets.stream()
                .filter(ticket -> switch (user.getRole()) {
                    case "ADMIN" -> true;
                    case "TECHNICIAN" -> ticket.getAssignedTo() != null &&
                            ticket.getAssignedTo().getId().equals(user.getId());
                    case "REQUESTER" -> ticket.getCreatedBy().getId().equals(user.getId());
                    default -> false;
                })
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
