package com.example.book_web.repository.impl;

import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.dto.user.FilterUserDto;
import com.example.book_web.repository.custom.UserCustomRepository;
import com.example.book_web.request.book.SearchBookRequest;
import com.example.book_web.request.user.SearchUserRequest;
import com.example.book_web.request.user.UserRequest;
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
public class UserRepositoryCustom implements UserCustomRepository {
    @PersistenceContext
    EntityManager entityManager;

    /**
     * @param
     * @param pageable
     * @return
     */
    @Override
    public Page<FilterUserDto> searchUser(SearchUserRequest request, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        setQuerySearch(sqlBuilder, request, mapParams);
        SqlNativeUtils.appendPage(sqlBuilder, mapParams, pageable);
        List<FilterUserDto> listResult = SqlNativeUtils.getList(entityManager, sqlBuilder.toString(),
                mapParams, FilterUserDto.class);
        return new PageImpl<>(listResult, pageable, !listResult.isEmpty() ? listResult.get(0).getTotalCount() : 0);
    }

    private void setQuerySearch(StringBuilder sqlBuilder, SearchUserRequest request, Map<String, Object> mapParams){
        searchUserCommon(sqlBuilder,request,mapParams);
    }

    private void searchUserCommon(StringBuilder sqlBuilder, SearchUserRequest request, Map<String, Object> mapParams){
        sqlBuilder.append("SELECT u.EMAIL, u.FULLNAME,u.PHONE_NUMBER,u.IDENTITY_NUMBER,u.DATE_OF_BIRTH,u.ADDRESS\n");
        sqlBuilder.append(", COUNT(1) OVER(PARTITION BY 1) total_count \n");
        sqlBuilder.append("FROM users u \n");
        sqlBuilder.append("WHERE u.active = 1 \n");
        SqlNativeUtils.findWord(sqlBuilder, request.getEmail(), mapParams, "u.EMAIL") ;
        SqlNativeUtils.findWord(sqlBuilder, request.getFullname(), mapParams, "u.FULLNAME");
        SqlNativeUtils.findWord(sqlBuilder, request.getPhoneNumber(), mapParams, "u.PHONE_NUMBER");
        SqlNativeUtils.findWord(sqlBuilder, request.getIdentityNumber(), mapParams, "u.IDENTITY_NUMBER");
        SqlNativeUtils.findWord(sqlBuilder, request.getAddress(), mapParams, "u.ADDRESS");

    }
}