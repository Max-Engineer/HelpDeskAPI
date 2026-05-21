package com.maksym.helpdesk.comment;

import com.maksym.helpdesk.comment.dto.CommentRequest;
import com.maksym.helpdesk.comment.dto.CommentResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/tickets/{ticketId}/comments")
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest request, @PathVariable Long ticketId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(201).body(commentService.addComment(ticketId, request, username));
    }

    @GetMapping("/tickets/{ticketId}/comments")
    public List<CommentResponse> getAllComments(@PathVariable Long ticketId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return commentService.getComments(ticketId, username);
    }
}
