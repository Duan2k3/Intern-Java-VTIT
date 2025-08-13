package com.example.book_web.service;

public interface NotificationService {
    void scanAndUpdateOverdueBooks();
    void sendOverdueEmails();
}
