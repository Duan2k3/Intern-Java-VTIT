package com.example.book_web.dto;

import com.example.book_web.entity.User;
import com.example.book_web.response.GetPostResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

    private String content;


    @JsonProperty("user_id")
    private Long user;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("updated_at")
    private LocalDate updatedAt;

    private List<GetPostResponse> comments;
}
