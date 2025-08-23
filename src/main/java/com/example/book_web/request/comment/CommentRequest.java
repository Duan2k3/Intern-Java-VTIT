package com.example.book_web.request.comment;

import com.example.book_web.dto.comment.CommentDTO;
import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private Long id;

    @NotBlank(message = MessageKeys.COMMENT.CONTENT_NOT_BLANK)
    private String content;

    @NotNull(message = MessageKeys.COMMENT.USER_ID_NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageKeys.COMMENT.USER_NAME_NOT_BLANK)
    private String userName;

    @NotNull(message = MessageKeys.COMMENT.POST_ID_NOT_NULL)
    private Long postId;

    private Long parentId;

    private LocalDate createdAt;

    private List<CommentDTO> replies;
}
