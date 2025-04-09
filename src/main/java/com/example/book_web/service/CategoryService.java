package com.example.book_web.service;

import com.example.book_web.dto.CategoryDTO;
import com.example.book_web.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category createCategory(CategoryDTO categoryDTO) throws Exception;
    Category updateCategory(Long id , CategoryDTO categoryDTO) throws Exception;
    void deleteCategory(Long id) throws Exception;
    List<String> getCategoryDetail(Long id) throws Exception;

}
