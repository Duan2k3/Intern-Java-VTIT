package com.example.book_web.dto.comment;

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
public class CommentDTO {

    private Long id;

    private String content;

    private Long userId;

    private String userName;

    private Long postId;

    private Long parentId;

    private LocalDate createdAt;

    private List<CommentDTO> replies;
}
