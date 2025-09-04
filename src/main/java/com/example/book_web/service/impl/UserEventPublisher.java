package com.example.book_web.service.impl;

import com.example.book_web.config.RabbitConfig;
import com.example.book_web.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishUserRegisteredEvent(User user) {
        UserRegisteredEvent event = new UserRegisteredEvent(

                user.getEmail(),
                user.getUsername(),
                user.getKeyActive()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );
    }

    public record UserRegisteredEvent( String email, String username, String keyActive) {}
}

