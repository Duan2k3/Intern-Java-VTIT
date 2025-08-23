package com.example.book_web.service.impl;

import com.example.book_web.dto.TestDto;
import com.example.book_web.repository.BorrowRepository;
import com.example.book_web.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final BorrowRepository  borrowRepository;
    /**
     * @param userId
     * @return
     */
    @Override
    public List<TestDto> getTestData(Long userId) {
//        List<TestDto> data = borrowRepository.getBorrowHistory(userId);
        return List.of();
    }
}
