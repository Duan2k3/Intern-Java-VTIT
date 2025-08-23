package com.example.book_web.response;

import lombok.*;

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
