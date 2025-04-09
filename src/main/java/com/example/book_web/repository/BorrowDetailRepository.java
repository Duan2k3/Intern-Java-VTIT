package com.example.book_web.repository;

import com.example.book_web.entity.BorrowDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowDetailRepository extends JpaRepository<BorrowDetail,Long> {
//    List<BorrowDetail> findAllById(List<Long> ids);
    List<BorrowDetail> findByBorrowIdIn(List<Long> borrowIds);


}
