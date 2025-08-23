package com.example.book_web.dto.comment;

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
public class CreateCommentDTO {
    private Long postId;

    private Long parentId;

    private Long userId;

    private String content;
}
