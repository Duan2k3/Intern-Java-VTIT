package com.example.book_web.service;


import com.example.book_web.dto.TestDto;

import java.util.List;

public interface TestService {

    List<TestDto> getTestData(Long userId);
}
