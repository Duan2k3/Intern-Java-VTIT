package com.example.book_web.controller;

import com.example.book_web.dto.CommentDTO;
import com.example.book_web.dto.CreateCommentDTO;
import com.example.book_web.entity.Comment;
import com.example.book_web.response.BaseResponse;
import com.example.book_web.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_COMMENT')")
    public ResponseEntity<BaseResponse> createComment(@RequestBody CreateCommentDTO dto) {
        try {
            Comment createdComment = commentService.createComment(dto);
            return ResponseEntity.ok(BaseResponse.builder()
                            .data(createdComment.getContent())
                            .message("Create Comment successfully")

                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
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
    @PreAuthorize("hasAuthority('ROLE_UPDATE_COMMENT')")
    public ResponseEntity<BaseResponse> updateComment(@PathVariable Long id , @RequestBody CommentDTO commentDTO)
            throws Exception{
        try {
            commentService.updateComment(id,commentDTO);
            return ResponseEntity.ok(BaseResponse.builder()
                            .data(commentDTO.getContent())
                            .message("Update successfully")
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                            .message(e.getMessage())
                    .build());
        }

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_COMMENT')")
    public ResponseEntity<BaseResponse> deleteResponse(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok(BaseResponse.builder()
                        .message("Delete successfully")
                .build());

    }
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ResponseEntity<BaseResponse> getCommentDetail(@PathVariable Long id) throws Exception{
        try {
          Comment comment =   commentService.getCommentById(id);
            return ResponseEntity.ok(BaseResponse.builder()
                            .data(comment.getContent())

                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                            .message(e.getMessage())
                    .build());
        }


    }



}
