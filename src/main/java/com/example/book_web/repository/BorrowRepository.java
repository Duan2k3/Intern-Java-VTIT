package com.example.book_web.repository;


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

    @Query("SELECT b FROM Borrow b WHERE b.id = :id and b.user.id = :userId")
    Borrow checkMatch(@Param("id") Long id,@Param("userId") Long userId);

    @Query("SELECT b FROM Borrow  b Where b.id = :borrowId ")
    void deleteByBorrowId(@Param("borrowId") Long borrowId);


}
