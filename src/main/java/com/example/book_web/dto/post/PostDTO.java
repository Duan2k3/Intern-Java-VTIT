package com.example.book_web.dto.post;

import com.example.book_web.response.GetPostResponse;
import com.example.book_web.utils.MessageKeys;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class PostDTO {

    @NotBlank(message = MessageKeys.POST.POST_CONTENT_NOT_BLANK)
    private String content;

    @JsonProperty("user_id")
    private Long user;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("updated_at")
    private LocalDate updatedAt;

    private List<GetPostResponse> comments;
}
