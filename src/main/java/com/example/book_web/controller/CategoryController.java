package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.CategoryDTO;
import com.example.book_web.entity.Category;
import com.example.book_web.response.ApiResponse;
import com.example.book_web.response.BaseResponse;
import com.example.book_web.service.CategoryService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private final LocalizationUtils localizationUtils;
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ResponseEntity<?> getAllCategory() {
        return ResponseConfig.success(categoryService.getAllCategories(),"Thanh cong");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_CATEGORY')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO){

            return ResponseConfig.success(categoryService.createCategory(categoryDTO));

    }
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_CATEGORY')")
    public ResponseEntity<?> updateCategory( @RequestBody CategoryDTO categoryDTO){
            return ResponseConfig.success(categoryService.updateCategory(categoryDTO),"Thanh cong");

    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DELETE_CATEGORY')")
    public ResponseEntity<?>deleteCategory(@PathVariable Long id) {

            categoryService.deleteCategory(id);
            return ResponseConfig.success(null,"Thanh cong");


    }

    @GetMapping("/statistics/{id}")
    public ResponseEntity<ResponseDto<List<String>>> detailCategory(@PathVariable Long id) {
        return ResponseConfig.success(categoryService.getCategoryDetail(id), "Thanh cong");

    }

}
