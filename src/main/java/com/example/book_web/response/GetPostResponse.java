package com.example.book_web.response;

import com.example.book_web.dto.CommentDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostResponse {
    private Long id;
    private String content;
    private String userName;

    private List<GetPostResponse> replies;
}
