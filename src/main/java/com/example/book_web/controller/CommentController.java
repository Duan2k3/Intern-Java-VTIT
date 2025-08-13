package com.example.book_web.controller;

import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.CommentDTO;
import com.example.book_web.dto.CreateCommentDTO;
import com.example.book_web.entity.Comment;
import com.example.book_web.response.ApiResponse;
import com.example.book_web.response.BaseResponse;
import com.example.book_web.service.CommentService;
import com.example.book_web.utils.MessageKeys;
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
    private final LocalizationUtils localizationUtils;

    @PostMapping("/create")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CREATE_COMMENT')")
    public ResponseEntity<ApiResponse> createComment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateCommentDTO dto) {
        try {
            Comment createdComment = commentService.createComment(authHeader,dto);
            return ResponseEntity.ok(ApiResponse.builder()
                            .data(createdComment)
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.CREATE_COMMENT))

                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }
    }
    @GetMapping("/post/{postId}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_COMMENT')")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentTreeByPostId(postId);
        return ResponseEntity.ok(comments);
    }
    @PutMapping("/update/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_COMMENT')")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable Long id ,
                                                      @RequestHeader("Authorization") String token,
                                                      @RequestBody CommentDTO commentDTO)
            throws Exception{
        try {
            commentService.updateComment(token ,id,commentDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                            .data(commentDTO)
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_COMMENT))
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }

    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DELETE_COMMENT')")
    public ResponseEntity<ApiResponse> deleteResponse(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_COMMENT))
                .build());

    }
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ResponseEntity<ApiResponse> getCommentDetail(@PathVariable Long id) throws Exception{
        try {
          Comment comment =   commentService.getCommentById(id);
            return ResponseEntity.ok(ApiResponse.builder()
                            .data(comment)

                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse
                    .builder()
                            .message(e.getMessage())
                    .build());
        }


    }



}
