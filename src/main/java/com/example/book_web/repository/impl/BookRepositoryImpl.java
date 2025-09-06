package com.example.book_web.repository.impl;

import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.repository.custom.BookCustomRepository;
import com.example.book_web.request.book.SearchBookRequest;
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
public class BookRepositoryImpl implements BookCustomRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<FilterBookDTO> searchBook(SearchBookRequest request, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        setQuerySearch(sqlBuilder, request, mapParams);
        SqlNativeUtils.appendPage(sqlBuilder, mapParams, pageable);
        List<FilterBookDTO> listResult = SqlNativeUtils.getList(entityManager, sqlBuilder.toString(),
                mapParams, FilterBookDTO.class);
        return new PageImpl<>(listResult, pageable, !listResult.isEmpty() ? listResult.get(0).getTotalCount() : 0);
    }


    private void setQuerySearch(StringBuilder sqlBuilder, SearchBookRequest request, Map<String, Object> mapParams) {
        bookSearchCommon(sqlBuilder, request, mapParams);

    }
    private void bookSearchCommon(StringBuilder sqlBuilder, SearchBookRequest request, Map<String, Object> mapParams) {
        sqlBuilder.append("SELECT b.ID, b.TITLE,b.AUTHOR, b.DESCRIPTION,b.QUANTITY,b.IMAGE,b.CREATED_AT ,b.PUBLISH_DATE\n");
        sqlBuilder.append(", COUNT(1) OVER(PARTITION BY 1) total_count \n");
        sqlBuilder.append("FROM books b \n");
        sqlBuilder.append("WHERE 1=1 \n");
        SqlNativeUtils.findWord(sqlBuilder, request.getTitle(), mapParams, "b.TITLE") ;
        SqlNativeUtils.findWord(sqlBuilder, request.getAuthor(), mapParams, "b.AUTHOR");
        sqlBuilder.append("ORDER BY b.id ASC  \n");

    }



}


