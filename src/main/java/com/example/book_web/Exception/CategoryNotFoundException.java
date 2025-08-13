package com.example.book_web.Exception;

import java.util.List;

public class CategoryNotFoundException extends RuntimeException {
    private List<Long> missingIds;

    public CategoryNotFoundException(List<Long> missingIds) {
        super("Các Category ID sau không tồn tại trong hệ thống: " + missingIds);
        this.missingIds = missingIds;
    }

    public List<Long> getMissingIds() {
        return missingIds;
    }
}
