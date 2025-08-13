package com.example.book_web.controller;

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
    public ResponseEntity<ApiResponse> getAllCategory() throws Exception{
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.DATA_EXISTING))
                            .code(200)
                            .data(categories)
                    .build());

        }catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message("Load field")


                    .build());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_CATEGORY')")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryDTO categoryDTO){
        try {
            Category category = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY))
                            .build()
            );
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }
    }
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_CATEGORY')")
    public ResponseEntity<ApiResponse> updateCategory( @RequestBody CategoryDTO categoryDTO){
        try {
            Category category = categoryService.updateCategory(categoryDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY))
                            .data(category)
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY))
                    .build());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DELETE_CATEGORY')")
    public ResponseEntity<ApiResponse>deleteCategory(@PathVariable Long id) throws Exception{
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY))
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                            .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/statistics/{id}")
    public ResponseEntity<List<String>> detailCategory(@PathVariable Long id) throws Exception{
        try {
            return ResponseEntity.ok(categoryService.getCategoryDetail(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(Collections.singletonList(e.getMessage()));
        }
    }



}
