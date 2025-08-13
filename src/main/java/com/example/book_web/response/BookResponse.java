package com.example.book_web.response;

import com.example.book_web.entity.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String description;

//    @JsonProperty("category_id")
//    private Long categoryId;

    public static BookResponse getBook(Book book){
        BookResponse bookResponse = BookResponse.builder()
                .author(book.getAuthors())
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .build();

        return bookResponse;




    }
}
