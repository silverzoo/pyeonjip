package com.team5.pyeonjip.comment.controller;

import com.team5.pyeonjip.comment.entity.Comment;
import com.team5.pyeonjip.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Comment>> getCommentsByProductId(@PathVariable("productId") Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentRepository.save(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long id,
            @RequestBody Comment updatedComment) {
        return commentRepository.findById(id)
                .map(existingComment -> {
                    existingComment.setContent(updatedComment.getContent());
                    existingComment.setRating(updatedComment.getRating());
                    existingComment.setTitle(updatedComment.getTitle());
                    existingComment.setCreatedAt(LocalDateTime.now());
                    Comment savedComment = commentRepository.save(existingComment);
                    return ResponseEntity.ok(savedComment);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
