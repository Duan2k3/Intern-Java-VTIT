package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.dto.book.BookDTO;
import com.example.book_web.dto.category.CategoryDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.entity.Category;
import com.example.book_web.repository.CategoryRepository;
import com.example.book_web.request.category.CategoryRequest;
import com.example.book_web.service.CategoryService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
       private final CategoryRepository categoryRepository;
       private final MessageCommon messageCommon;
       private final ModelMapper modelMapper;

    @Override
    @Transactional
    public List<Category> getAllCategories() {
        log.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }


    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryRequest request) {
        log.info("Creating category with name: {}", request.getName());
        Optional<Category> existingCategory = categoryRepository.findCategoryByName(request.getName());
        if(!existingCategory.isEmpty()){
            throw new DataExistingException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NAME_EXISTING),"400" );
        }
        Category category = modelMapper.map(request,Category.class);
        Category saveCategory = categoryRepository.save(category);

        return modelMapper.map(request,CategoryDTO.class);

    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id , CategoryRequest request)  {
        log.info("Updating category with id: {}", id);
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST),"400");
        }

        Optional<Category> optionalCategory = categoryRepository.findCategoryByName(request.getName());
        if(!optionalCategory.isEmpty()){
            throw new DataExistingException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NAME_EXISTING),"400" );
        }

        Category existingCategory = category.get();
        existingCategory.setName(request.getName());
         categoryRepository.save(existingCategory);
         return modelMapper.map(request,CategoryDTO.class);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id)  {
        log.info("Deleting category with id: {}", id);
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
    public List<BookDTO> getCategoryDetail(Long id)  {
        log.info("Fetching category details for id: {}", id);
        Optional<Category> existing = categoryRepository.findById(id);
        if(existing.isEmpty()){
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.CATEGORY.CATEGORY_NOT_EXIST), "400");
        }
        Category category = existing.get();

        List<Book> books = category.getBooks();
        if(books.isEmpty()){
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST), "400");
        }
        return books.stream()
                .map(book -> BookDTO.builder()
                        .title(book.getTitle())
                        .authors(book.getAuthors())
                        .quantity(book.getQuantity())
                        .image(book.getImage())
                        .description(book.getDescription())
                        .categoriesIds(book.getCategories().stream().map(Category::getId).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public List<Category> getCategoriesByBookId(Long id) {
        return categoryRepository.findCategoriesByBookId(id);
    }
}
