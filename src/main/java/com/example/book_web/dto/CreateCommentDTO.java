package com.example.book_web.dto;

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
