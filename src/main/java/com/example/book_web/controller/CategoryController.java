package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.dto.book.BookDTO;

import com.example.book_web.entity.Category;
import com.example.book_web.request.category.CategoryRequest;
import com.example.book_web.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/categories")
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ResponseEntity<?> getAllCategory() {
        return ResponseConfig.success(categoryService.getAllCategories(),"Thanh cong");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_CATEGORY')")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest request){

            return ResponseConfig.success(categoryService.createCategory(request));

    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_CATEGORY')")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryRequest request, @PathVariable Long id){
            return ResponseConfig.success(categoryService.updateCategory(id ,request),"Thanh cong");

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_CATEGORY')")
    public ResponseEntity<?>deleteCategory(@PathVariable Long id) {

            categoryService.deleteCategory(id);
            return ResponseConfig.success(null,"Thanh cong");


    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ResponseEntity<ResponseDto<List<BookDTO>>> detailCategory(@PathVariable Long id) {
        return ResponseConfig.success(categoryService.getCategoryDetail(id), "Thanh cong");

    }

    @GetMapping("/get-categories/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ResponseEntity<ResponseDto<List<Category>>> getCategories(@PathVariable Long id){
        return ResponseConfig.success(categoryService.getCategoriesByBookId(id),"Thanh cong");

    }

}
