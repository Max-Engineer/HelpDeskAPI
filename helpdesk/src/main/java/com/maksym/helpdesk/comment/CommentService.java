package com.maksym.helpdesk.comment;

import com.maksym.helpdesk.auth.User;
import com.maksym.helpdesk.auth.UserRepository;
import com.maksym.helpdesk.comment.dto.CommentRequest;
import com.maksym.helpdesk.comment.dto.CommentResponse;
import com.maksym.helpdesk.exception.ResourceNotFoundException;
import com.maksym.helpdesk.ticket.Ticket;
import com.maksym.helpdesk.ticket.TicketRepository;
import com.maksym.helpdesk.ticket.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, TicketService ticketService,
                          TicketRepository ticketRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getAuthor().getUsername()
        );
    }

    public CommentResponse addComment(Long ticketId, CommentRequest request, String username) {
        ticketService.getById(ticketId, username);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        Comment comment = new Comment(request.getContent(), author, ticket);
        return toResponse(commentRepository.save(comment));
    }

    public List<CommentResponse> getComments(Long ticketId, String username) {
        ticketService.getById(ticketId, username);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return commentRepository.findByTicket(ticket)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
