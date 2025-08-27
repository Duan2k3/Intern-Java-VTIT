package com.example.book_web.repository;

import com.example.book_web.entity.BorrowDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowDetailRepository extends JpaRepository<BorrowDetail,Long> {
    List<BorrowDetail> findByBorrowIdIn(List<Long> borrowIds);

    @Query("SELECT bd FROM BorrowDetail bd WHERE bd.borrow.user.id = :userId")
    List<BorrowDetail> getBorrowHistory(@Param("userId") Long userId);

    Optional<BorrowDetail> findByBorrowIdAndBookId(Long borrowId, Long bookId);

    @Query("SELECT bd FROM BorrowDetail bd WHERE bd.actualReturnedDate IS NULL AND bd.borrow.returnDate < CURRENT_DATE")
    List<BorrowDetail> findOverdueUnreturnedDetails();

    @Query("SELECT bd FROM BorrowDetail bd WHERE bd.actualReturnedDate IS NOT NULL AND EXISTS (SELECT n FROM Notification n WHERE n.borrowDetailId = bd.id)")
    List<BorrowDetail> findReturnedWithNotification();


    @Query("SELECT bd FROM BorrowDetail bd WHERE bd.borrow.id = :borrowId AND bd.book.id IN :bookIds")
    List<BorrowDetail> findAllByBorrowIdAndBookIds(@Param("borrowId") Long borrowId, @Param("bookIds") List<Long> bookIds);



}
