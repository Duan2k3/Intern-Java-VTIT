package com.example.book_web.service;

import com.example.book_web.dto.CommentDTO;
import com.example.book_web.dto.CreateCommentDTO;
import com.example.book_web.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(String token ,CreateCommentDTO comment) throws Exception;
    Comment getCommentById(Long id) throws Exception;
    Comment updateComment(String token ,Long id, CommentDTO comment) throws Exception;
    void deleteComment(Long id);
    List<CommentDTO> getCommentTreeByPostId(Long postId);
}
