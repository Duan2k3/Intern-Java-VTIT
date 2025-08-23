package com.example.book_web.service;

import com.example.book_web.dto.comment.CommentDTO;
import com.example.book_web.dto.comment.CreateCommentDTO;
import com.example.book_web.entity.Comment;
import com.example.book_web.request.comment.CommentRequest;
import com.example.book_web.request.comment.CreateCommentRequest;

import java.util.List;

public interface CommentService {

    Comment createComment(String token , CreateCommentRequest request) ;
    Comment getCommentById(Long id) ;
    Comment updateComment(String token , Long id, CommentRequest request) ;
    void deleteComment(Long id);
    List<CommentDTO> getCommentTreeByPostId(Long postId);
}
