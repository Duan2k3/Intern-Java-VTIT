package com.example.book_web.dto.borrow;

import com.example.book_web.enums.BorrowStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InforBorrowDto {
    private Long id;
    private String title;
    private String authors;
    private Integer quantity;
    private String email;
    private String username;
    private BorrowStatus status;

    public InforBorrowDto(Long id, String title, String authors, Integer quantity, String email, String username,BorrowStatus status) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.quantity = quantity;
        this.email = email;
        this.username = username;
        this.status = status;
    }
}
