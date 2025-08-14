package com.example.book_web.common;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * The class represents the common handling for multiple language.
 */
@Service
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MessageCommon {


    /**
     * Source message using get message from message properties file.
     */
    private MessageSource messageSource;
    private final MessageKeys messageKeys ;


    /**
     * Constructor create instance LoggerService.
     *
     * @param messageSource Source message
     */
    public MessageCommon(MessageSource messageSource, MessageKeys messageKeys) {
        this.messageSource = messageSource;
        this.messageKeys = messageKeys;
    }

    /**
     * get information value by message code.
     *
     * @param messageCode message use to get message corresponding
     * @return message information corresponding to the message code
     */
    public String getValueByMessageCode(String messageCode) {
        return messageSource.getMessage(messageCode, null, new Locale("vi"));
    }

    /**
     * get information value by message code.
     *
     * @param messageCode message use to get message corresponding
     * @return message information corresponding to the message code
     */
    public String getValueByMessageCode(String messageCode, Locale locale) {
        return messageSource.getMessage(messageCode, null, locale);
    }

    /**
     * get information value by message code and format with params.
     *
     * @param messageCode message use to get message corresponding
     * @param params      parameter with format
     * @return message information corresponding to the message code
     */
    public String getMessage(String messageCode, Object... params) {
        try {
            return String.format(getValueByMessageCode(messageCode), params);
        } catch (Exception e) {
            return messageCode;
        }
    }
}