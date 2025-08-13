package com.example.book_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/library/books")
@RequiredArgsConstructor
public class EmailController {
    private final JavaMailSender mailSender;


    @PostMapping("/email")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BOOK')")
    public String sendEmail(){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("duanvuxa@gmail.com");
            message.setTo("duanvuxa@gmail.com");
            message.setSubject("Test send email");
            message.setText("Hello Duan2k3");
            mailSender.send(message);
            return "Send message successfully";
        }
        catch (MailException e){
            return e.getMessage();
        }

    }




}
