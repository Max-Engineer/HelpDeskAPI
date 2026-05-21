package com.maksym.helpdesk.comment.dto;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String author;

    public CommentResponse(Long id, String content, LocalDateTime createdAt, String author) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getAuthor() {
        return author;
    }
}
