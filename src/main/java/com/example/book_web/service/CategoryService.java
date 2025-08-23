package com.example.book_web.service;

import com.example.book_web.dto.book.BookDTO;
import com.example.book_web.dto.category.CategoryDTO;
import com.example.book_web.entity.Category;
import com.example.book_web.request.category.CategoryRequest;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    CategoryDTO createCategory(CategoryRequest request) ;
    CategoryDTO updateCategory( Long id ,CategoryRequest request) ;
    void deleteCategory(Long id) ;
    List<BookDTO> getCategoryDetail(Long id) ;

}
