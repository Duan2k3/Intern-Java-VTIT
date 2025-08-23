package com.example.book_web.repository;

import com.example.book_web.dto.BorrowHistoryDTO;
import com.example.book_web.dto.TestDto;
import com.example.book_web.dto.borrow.InforBorrowDto;
import com.example.book_web.entity.Borrow;
import com.example.book_web.entity.BorrowDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow,Long> {

    @Query("SELECT new com.example.book_web.dto.borrow.InforBorrowDto(" +
            "bk.id, bk.title, bk.authors, bd.quantity, u.email, u.username) " +
            "FROM Borrow b " +
            "JOIN b.borrowDetails bd " +
            "JOIN bd.book bk " +
            "JOIN b.user u " +
            "WHERE u.id = :userId")
    List<InforBorrowDto> getBorrowHistory(@Param("userId") Long userId);



}
