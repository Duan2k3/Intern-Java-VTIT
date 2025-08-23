package com.example.book_web.request.comment;

import com.example.book_web.utils.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentRequest {
    @NotNull(message = MessageKeys.COMMENT.POST_ID_NOT_BLANK)
    private Long postId;
    private Long parentId;

    @NotBlank(message = MessageKeys.COMMENT.USER_ID_NOT_BLANK)
    private Long userId;

    @NotBlank(message = MessageKeys.COMMENT.CONTENT_NOT_BLANK)
    private String content;
}
