package com.example.book_web.service.impl;

import com.example.book_web.entity.Book;
import com.example.book_web.entity.BorrowDetail;
import com.example.book_web.entity.Notification;
import com.example.book_web.entity.User;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void sendOverdueEmail(Notification noti) {
        Optional<User> userOpt = userRepository.findById(noti.getUserId());
        Optional<Book> bookOpt = bookRepository.findById(noti.getBookId());

        if (userOpt.isEmpty() || bookOpt.isEmpty()) return;

        User user = userOpt.get();
        Book book = bookOpt.get();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Thông báo quá hạn mượn sách");
        message.setText("Chào " + user.getUsername() + ",\n\n"
                + "Bạn đang quá hạn trả sách: " + book.getTitle() + "\n"
                + "Hạn trả là: " + noti.getDueDate() + "\n\n"
                + "Vui lòng trả sách sớm nhất có thể để tránh bị xử lý.\n\n"
                + "Thư viện.");

        mailSender.send(message);
    }
    public void sendThankYouNotification(BorrowDetail detail) {
        String email = detail.getBorrow().getUser().getEmail();
        String subject = "Cảm ơn bạn đã trả sách";
        String content = String.format("Cảm ơn bạn đã trả sách \"%s\" . Hẹn gặp lại . Lần sau nhớ trả đúng hạn!",
                detail.getBook().getTitle());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(content);
        message.setSubject(subject);
        message.setTo(email);
        mailSender.send(message);
    }

}
