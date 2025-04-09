package com.example.book_web.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrows")
@Builder
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "returned_date", nullable = false)
    private LocalDate returnDate;


    @Column(name = "note")
    private String note;

    @JsonIgnore
    @OneToMany(mappedBy = "borrow", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BorrowDetail> borrowDetails;

}
