package com.example.book_web.repository.impl;


import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.dto.borrow.InforBorrowDto;
import com.example.book_web.repository.custom.BorrowCustomRepository;
import com.example.book_web.request.book.SearchBookRequest;
import com.example.book_web.request.borrow.FilterBorrowRequest;
import com.example.book_web.utils.SqlNativeUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BorrowRepositoryImpl implements BorrowCustomRepository {

    @PersistenceContext
    EntityManager entityManager;
    @Override
    public Page<InforBorrowDto> listBorrowHistory(Long id ,FilterBorrowRequest request, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        setQuery(id,sqlBuilder, request, mapParams);
        SqlNativeUtils.appendPage(sqlBuilder, mapParams, pageable);
        List<InforBorrowDto> listResult = SqlNativeUtils.getList(entityManager, sqlBuilder.toString(),
                mapParams, InforBorrowDto.class);
        return new PageImpl<>(listResult, pageable, !listResult.isEmpty() ? listResult.get(0).getTotalCount() : 0);

    }

    private void setQuery(Long id ,StringBuilder sqlBuilder, FilterBorrowRequest request, Map<String, Object> mapParams){
          getBorrowCommon(id,sqlBuilder,request,mapParams);


    }

    private void getBorrowCommon(Long id ,StringBuilder sqlBuilder, FilterBorrowRequest request,  Map<String, Object> mapParams){
        sqlBuilder.append("SELECT bk.ID, bk.TITLE, bk.AUTHOR, bd.QUANTITY, u.EMAIL, u.USER_NAME, bd.STATUS \n");
        sqlBuilder.append(", COUNT(1) OVER(PARTITION BY 1) total_count \n");
        sqlBuilder.append("FROM Borrows b ");
        sqlBuilder.append("JOIN borrow_detail bd on b.id = bd.borrow_id  ");
        sqlBuilder.append("JOIN books bk  on bk.id = bd.book_id ");
        sqlBuilder.append("JOIN users u  on u.id = b.user_id \n");
        sqlBuilder.append("WHERE u.active = 1 ");
        SqlNativeUtils.findWord(sqlBuilder,request.getTitle(),mapParams,"bk.TITLE");
        SqlNativeUtils.findWord(sqlBuilder,request.getAuthor(),mapParams,"bk.AUTHOR");
        SqlNativeUtils.findWord(sqlBuilder,request.getEmail(),mapParams,"bk.QUANTITY");
        SqlNativeUtils.findWord(sqlBuilder,request.getUsername(),mapParams,"u.EMAIL");
        SqlNativeUtils.findEqual(sqlBuilder, id, mapParams, "u.id", "userId");
        sqlBuilder.append("ORDER BY bk.id ASC");

    }
}
