package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.dto.CommentDTO;
import com.example.book_web.dto.CreateCommentDTO;
import com.example.book_web.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CREATE_COMMENT')")
    public ResponseEntity<?> createComment(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateCommentDTO dto) {
            return ResponseConfig.success(commentService.createComment(authHeader,dto),"Thanh cong");

    }
    @GetMapping("/post/{postId}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_COMMENT')")
    public ResponseEntity<ResponseDto<List<CommentDTO>>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseConfig.success(commentService.getCommentTreeByPostId(postId),"Thanh cong");

    }
    @PutMapping("/update/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_COMMENT')")
    public ResponseEntity<?> updateComment(@PathVariable Long id ,
                                                      @RequestHeader("Authorization") String token,
                                                   @Valid @RequestBody CommentDTO commentDTO)
            {
            return ResponseConfig.success(commentService.updateComment(token,id,commentDTO),"Thanh cong");
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DELETE_COMMENT')")
    public ResponseEntity<?> deleteResponse(@PathVariable Long id){
        return ResponseConfig.success(null,"Thanh cong");

    }
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ResponseEntity<?> getCommentDetail(@PathVariable Long id) {

            return ResponseConfig.success(commentService.getCommentById(id),"Thanh cong");
        }

}
