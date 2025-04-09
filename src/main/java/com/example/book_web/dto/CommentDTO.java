package com.example.book_web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("user_id")
    private Long userId;

    private String userName;

    @JsonProperty("post_id")
    private Long postId;


    @JsonProperty("parent_id")
    private Long parentId;
    private LocalDate createdAt;
    private List<CommentDTO> replies;
}
