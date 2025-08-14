package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.dto.CategoryDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.entity.Category;
import com.example.book_web.repository.CategoryRepository;
import com.example.book_web.service.CategoryService;
import com.example.book_web.utils.MessageKeys;
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
       private final MessageCommon messageCommon;

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
            throw new DataExistingException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NAME_EXISTING),"400" );
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();

        return categoryRepository.save(category);

    }

    @Override
    public Category updateCategory(Long id ,CategoryDTO categoryDTO)  {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST),"400");
        }

        Optional<Category> optionalCategory = categoryRepository.findCategoryByName(categoryDTO.getName());
        if(!optionalCategory.isEmpty()){
            throw new DataExistingException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NAME_EXISTING),"400" );
        }

        Category existingCategory = category.get();
        existingCategory.setName(categoryDTO.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id)  {
       Optional<Category> category = categoryRepository.findById(id);
       if(category.isEmpty()){
           throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NAME_NOT_EXIST),"400");
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
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST), "400");
        }
        Category category = existing.get();

        return category.getBooks().stream().map(Book::getTitle)
                .collect(Collectors.toList());

    }
}
