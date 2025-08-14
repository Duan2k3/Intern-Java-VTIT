//package com.example.book_web.components;
//
//import com.example.book_web.utils.WebUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.LocaleResolver;
//
//import java.util.Locale;
//
//@Component
//@RequiredArgsConstructor
//public class LocalizationUtils {
//    private final MessageSource messageSource;
//
//    public String getLocalizedMessage(String messageKey, Object... params) {
//        Locale locale = LocaleContextHolder.getLocale();
//        return messageSource.getMessage(messageKey, params, locale);
//    }
//}
