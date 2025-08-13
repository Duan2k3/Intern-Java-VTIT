package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.CategoryDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.entity.Category;
import com.example.book_web.repository.CategoryRepository;
import com.example.book_web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
       private final CategoryRepository categoryRepository;
    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }


    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findCategoryByName(categoryDTO.getName());
        if(!existingCategory.isEmpty()){
            throw new DataExistingException("Category da ton tai ","400");
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();

        return categoryRepository.save(category);

    }

    @Override
    public Category updateCategory(CategoryDTO categoryDTO)  {
        Optional<Category> category = categoryRepository.findById(categoryDTO.getId());


        if(category.isEmpty()){
            throw new DataNotFoundException("Category not existing","400");
        }
        Optional<Category> optionalCategory = categoryRepository.findCategoryByName(categoryDTO.getName());
        if(!optionalCategory.isEmpty()){
            throw new DataExistingException("Category has existing","400");
        }

        Category existingCategory = category.get();
        existingCategory.setName(categoryDTO.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id)  {
       Optional<Category> category = categoryRepository.findById(id);
       if(category.isEmpty()){
           throw new DataNotFoundException("Category not existing","400");
       }
         categoryRepository.deleteById(id);
    }

    /**
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public List<String> getCategoryDetail(Long id)  {
        Optional<Category> existing = categoryRepository.findById(id);
        if(existing.isEmpty()){
            throw new DataNotFoundException("Category not existing","400");
        }
        Category category = existing.get();

        return category.getBooks().stream().map(Book::getTitle)
                .collect(Collectors.toList());

    }
}
